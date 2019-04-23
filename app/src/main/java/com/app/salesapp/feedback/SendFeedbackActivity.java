package com.app.salesapp.feedback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.salesapp.BaseActivity;
import com.app.salesapp.Gps.ConfigRequestModel;
import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.feedback.model.TypeListResponseModel;
import com.app.salesapp.network.Response;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;

import javax.inject.Inject;

public class SendFeedbackActivity extends BaseActivity implements SendFeedbackView {
    public final int REQUEST_CODE = 1;
    private static String SHARE_STATUS = "Share Status";

    String type = SHARE_STATUS;
    private String picture;
    private String image_path = "";
    EditText feedbackET;
    private Spinner typeEditText;
    ImageView imagePhoto, btnSubmit;
    LinearLayout btnCameraPhoto;
    TextView location;

    String[] itemDropdown ;

    private SendFeedbackPresenter presenter;
    private RelativeLayout progressLayout;

    RecyclerView feedbackList;

    ArrayList<PostFeedbackRequest> postFeedbackRequests;
    DraftFeedbackAdapter draftFeedbackAdapter;
    LinearLayout feedbackDraftLayout;
    int selectedDraft = -1;

    private ListChannelResponseModel.ChannelList selectedChannel;

    @Inject
    SalesAppService salesAppService;

    @Inject
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(SHARE_STATUS);
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);

        TextView title = (TextView)findViewById(R.id.titleToolbar);
        title.setText(SHARE_STATUS);

        String userLevel = userService.getUserPreference().level;
        typeEditText = (Spinner) findViewById(R.id.typelET);
        progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
        feedbackET = (EditText) findViewById(R.id.feedbackET);
        imagePhoto = (ImageView) findViewById(R.id.imagePhoto);
        btnCameraPhoto = (LinearLayout) findViewById(R.id.btnCameraPhoto);
        btnSubmit = (ImageView) findViewById(R.id.btnSubmit);
        location = (TextView) findViewById(R.id.curLocation);

        selectedChannel = userService.getPostAttendanceChannel();
        if(selectedChannel != null){
            if(!selectedChannel.name.equals("")){
                location.setText(selectedChannel.name);
            }else{
                location.setText("Please Check in");
            }
        }else{
            location.setText("Please Check in");
        }

        presenter = new SendFeedbackPresenter(this, salesAppService,userService);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedbackET.getText().toString().equals("")) {
                    feedbackET.setError("Cannot be empty!");
                } else {
                    presenter.postFeedback(new PostFeedbackRequest(userService.getAccessToken(), userService.getCurrentProgram(), feedbackET.getText().toString(),image_path, type));
                }
            }
        });

        postFeedbackRequests = new ArrayList<>();
        draftFeedbackAdapter = new DraftFeedbackAdapter(getApplicationContext(), postFeedbackRequests, new DraftFeedbackAdapter.OnDraftListener() {

            @Override
            public void onDraftClicked(PostFeedbackRequest postFeedbackRequest, int pos) {
                selectedDraft = pos;
                if(!postFeedbackRequest.getImagePath().equals("") && postFeedbackRequest.getImagePath() !=null) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(postFeedbackRequest.getImagePath());
                    imagePhoto.setImageBitmap(myBitmap);
                }
                feedbackET.setText(postFeedbackRequest.getDescription());
            }
        });
        feedbackDraftLayout = (LinearLayout)findViewById(R.id.feedbackDraftLayout);
        feedbackList = (RecyclerView)findViewById(R.id.feedbackList);
        feedbackList.setLayoutManager(new LinearLayoutManager(this));
        feedbackList.setNestedScrollingEnabled(false);
        feedbackList.setHasFixedSize(true);
        feedbackList.setAdapter(draftFeedbackAdapter);

        btnCameraPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendFeedbackActivity.this, FeedBackActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        itemDropdown = new String[]{"Select Type"};
        initDropdown();

        if(userLevel.equals("low")){
            typeEditText.setVisibility(View.GONE);
        }

        presenter.getTypeList(new ConfigRequestModel(userService.getAccessToken(), userService.getCurrentProgram()));
    }

    public void initDropdown(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_view_resource, itemDropdown){
            @Override
            public boolean isEnabled(int position){
                if(position == 0) {
                    return false;
                }
                else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        typeEditText.setAdapter(adapter);
        typeEditText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = itemDropdown[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDraftFeedback();
    }

    private void getDraftFeedback() {
        if(postFeedbackRequests!=null)
            postFeedbackRequests.clear();
        postFeedbackRequests = userService.getDraftFeedback();
        if(postFeedbackRequests.size()== 0)
            showDraftFeedback(false);
        else
            showDraftFeedback(true);
        draftFeedbackAdapter.resetData(postFeedbackRequests);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                image_path = data.getStringExtra("IMAGE_PATH");
                Bitmap myBitmap = BitmapFactory.decodeFile(image_path);
                imagePhoto.setVisibility(View.VISIBLE);
                imagePhoto.setImageBitmap(myBitmap);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showLoading(boolean show) {
        if (show)
            progressLayout.setVisibility(View.VISIBLE);
        else
            progressLayout.setVisibility(View.GONE);
    }

    @Override
    public void onErrorPostFeedback(String message) {
        AlertDialog dialog = new AlertDialog.Builder(SendFeedbackActivity.this)
                .setTitle("Post Feedback")
                .setMessage("Something wrong, " + message)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getDraftFeedback();
                        dialog.dismiss();
                    }
                }).create();

        dialog.show();
    }

    @Override
    public void onSuccesPostFeedback(Response<String> response) {
        if(selectedDraft>=0) {
            postFeedbackRequests = userService.getDraftFeedback();
            postFeedbackRequests.remove(selectedDraft);
            userService.saveDraftFeedback(postFeedbackRequests);
            draftFeedbackAdapter.replaceData(postFeedbackRequests);
            selectedDraft = -1;
        }
        AlertDialog dialog = new AlertDialog.Builder(SendFeedbackActivity.this)
                .setTitle("Post Feedback")
                .setMessage("Success")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                }).create();

        dialog.show();
    }

    @Override
    public void showDraftFeedback(boolean show) {
        if(show)
            feedbackDraftLayout.setVisibility(View.VISIBLE);
        else
            feedbackDraftLayout.setVisibility(View.GONE);
    }

    @Override
    public void onSuccesGetTypeList(Response<TypeListResponseModel> response) {
        itemDropdown = new String[response.data.type.length+1];
        itemDropdown[0] = "Select Type";
        for(int i = 0 ; i < response.data.type.length; i++){
            itemDropdown[i+1] = response.data.type[i];
        }
        initDropdown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
