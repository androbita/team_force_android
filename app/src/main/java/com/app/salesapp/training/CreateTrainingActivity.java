package com.app.salesapp.training;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.salesapp.BaseActivity;
import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.training.activity.AudianceActivity;
import com.app.salesapp.training.activity.MaterialActivity;
import com.app.salesapp.training.model.ModuleSelectedModel;
import com.app.salesapp.user.UserService;
import com.app.salesapp.util.Util;
import com.mindorks.paracamera.Camera;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CreateTrainingActivity extends BaseActivity implements CreateTrainingView {

    private String photos = "";

    private Camera camera;
    private RelativeLayout progressLayout;
    private Button btnPhoto;
    private ImageView btnSubmit;
    private ImageView picFrame;
    private CreateTrainingPresenter presenter;
    private Spinner trainingTypeSpinner;

    @Inject
    public UserService userService;
    @Inject
    public SalesAppService salesAppService;

    private ListChannelResponseModel.ChannelList selectedChannel;
    private TextView btnAddAudience;

    private ArrayList<AudienceModel> listAudience;
    private ArrayList<ModuleSelectedModel> listModule;
    private EditText audienceEditText;
    private EditText moduleEditText;
    private AlertDialog listAudienceDialog;
    private EditText remarkEditText;
    private EditText venueEditText;
    private TextView location;
    private ImageView btnAddSelAudiance;
    private ImageView btnAddSelMaterial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_training);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        TextView title = toolbar.findViewById(R.id.titleToolbar);
        title.setText("Training");

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

        initCamera();
        initComponents();

        presenter = new CreateTrainingPresenter(userService, this, salesAppService);
        presenter.getModuleList();
        //get channel on postAttendance
        //presenter.getListChannel();
        if(selectedChannel != null)
        presenter.getSalesList(selectedChannel.users_organization_id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    private void initComponents() {
        progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
        showLoading(false);
        audienceEditText = (EditText) findViewById(R.id.audience_edit_text);
        moduleEditText = (EditText) findViewById(R.id.material);
        venueEditText = (EditText) findViewById(R.id.venue_edit_text);
        remarkEditText = (EditText) findViewById(R.id.remarkET);
        btnSubmit = (ImageView) findViewById(R.id.btnSubmit);
        btnAddAudience = (TextView) findViewById(R.id.btn_add_audience);
        btnAddSelAudiance = (ImageView) findViewById(R.id.addSelAudiance);
        btnAddSelMaterial = (ImageView) findViewById(R.id.addSelMaterial);
        btnPhoto = (Button) findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    camera.takePicture();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        picFrame = (ImageView) findViewById(R.id.pic_frame);

        trainingTypeSpinner = (Spinner) findViewById(R.id.training_type_spinner);
        String[] trainingItem = new String[]{
                "Choose Training Type", "FACE TO FACE - Perorangan", "CLASSROOM - Kelas"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_view_resource, trainingItem){
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
        trainingTypeSpinner.setAdapter(adapter);
        trainingTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        trainingTypeSpinner.setSelection(0);

        btnAddAudience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAudienceDialog();
            }
        });

        listAudience = new ArrayList<>();
        listModule = new ArrayList<>();
       //test add modul
       // ModuleSelectedModel modul = new ModuleSelectedModel("1","test",false);
       // listModule.add(modul);

        btnAddSelAudiance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateTrainingActivity.this, AudianceActivity.class);
                startActivity(i);
            }
        });

        btnAddSelMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateTrainingActivity.this, MaterialActivity.class);
                startActivity(i);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostTrainingRequest postTrainingRequest = new PostTrainingRequest();
                for (AudienceModel data : listAudience) {
                    if (data.isSelected) {
                        SalesModel model = new SalesModel();
                        model.sales_person_id = data.sales_person_id;
                        postTrainingRequest.audience_name.add(model);}
                }
                for (ModuleSelectedModel data : listModule) {
                    if (data.isSelected) postTrainingRequest.module_name.add(data.training_module_id);
                }
                postTrainingRequest.remark = remarkEditText.getText().toString();
                postTrainingRequest.total_audience = String.valueOf(getTotalSelectedAudience());
                postTrainingRequest.venue = venueEditText.getText().toString();
                postTrainingRequest.token = userService.getAccessToken();
                postTrainingRequest.program_id = userService.getCurrentProgram();
                postTrainingRequest.training_type = trainingTypeSpinner.getSelectedItem().toString();
                postTrainingRequest.picture = photos;
                presenter.postTraining(postTrainingRequest);
            }
        });
    }

    private void showAddAudienceDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_audience, null);
        final EditText fullNameET = (EditText) dialogView.findViewById(R.id.fullNameET);
        final EditText emailET = (EditText) dialogView.findViewById(R.id.emailET);
        final EditText phoneET = (EditText) dialogView.findViewById(R.id.phoneET);
        final Button submitBtn = (Button) dialogView.findViewById(R.id.submitBtn);
        final ImageView cancleBtn = (ImageView) dialogView.findViewById(R.id.cancleBtn);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);

        final AlertDialog alertDialog = dialogBuilder.create();

        submitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (fullNameET.getText().toString().equals(""))
                    fullNameET.setError("Harus di isi");
                else if (emailET.getText().toString().equals(""))
                    emailET.setError("Harus di isi");
                else if (phoneET.getText().toString().equals(""))
                    phoneET.setError("Harus di isi");
                else {
                    if(selectedChannel != null) {
                        AudienceModel model = new AudienceModel();
                        model.full_name = fullNameET.getText().toString();
                        model.email = emailET.getText().toString();
                        model.mobile_phone = phoneET.getText().toString();
                        model.organization_id = selectedChannel.users_organization_id;
                        model.token = userService.getAccessToken();
                        model.program_id = userService.getCurrentProgram();
                        presenter.postAudience(model);
                        alertDialog.dismiss();
                    }
                }
            }
        });
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            Bitmap bitmap = camera.getCameraBitmap();
            if (bitmap != null) {
                Util.movePhotoToGallery(getApplicationContext(), camera.getCameraBitmapPath(), "Teamforce_" + System.currentTimeMillis() + ".jpg");
                photos = Util.encodeToBase64(BitmapFactory.decodeFile(camera.getCameraBitmapPath()), Bitmap.CompressFormat.JPEG, 50);
                picFrame.setImageBitmap(BitmapFactory.decodeFile(camera.getCameraBitmapPath()));
            } else {
                Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initCamera() {
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("Teamforce")
                .setName("Teamforce_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000).build(this);
    }

    @Override
    public void showLoading(boolean show) {
        if (show)
            progressLayout.setVisibility(View.VISIBLE);
        else
            progressLayout.setVisibility(View.GONE);
    }

    @Override
    public void showListModule(List<ModuleModel> response) {
        listModule = new ArrayList<>();
        listModule.clear();
        for (ModuleModel temp : response) {
            ModuleSelectedModel modul = new ModuleSelectedModel(temp.training_module_id,temp.name,false);
            listModule.add(modul);
        }
        userService.setModuleTraining(listModule);
        moduleEditText.setText(getTotalSelectedModule()+" Material Selected");
    }

    @Override
    public void showErrorMessage(String appErrorMessage) {
        showToast(appErrorMessage);
    }

    @Override
    public void loadChannelSpinner(List<ListChannelResponseModel.ChannelList> data) {
        if (data.size() <= 0) {
            Toast.makeText(this, "Tidak ada Channel", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onSuccessPostTraining() {
        Toast.makeText(this, "Post Training Berhasil !", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onSuccessPostSales(AudienceModel model) {
        if (listAudienceDialog != null) {
            listAudienceDialog.dismiss();
        }
        presenter.getSalesList(selectedChannel.users_organization_id);
    }

    @Override
    public void showListSales(List<SalesModel> data) {
        ArrayList<AudienceModel> temparry = userService.getAudianceTraining();
        listAudience = new ArrayList<>();
        for (SalesModel temp : data) {
            AudienceModel am = new AudienceModel();
            am.sales_person_id = temp.sales_person_id;
            am.full_name = temp.name;
            am.isSelected = false;
            if(temparry != null){
                for(int i = 0 ; i < temparry.size(); i++){
                    AudienceModel audiance = temparry.get(i);
                    if(temp.name.equals(audiance.full_name)){
                        am.isSelected = audiance.isSelected;
                        break;
                    }
                }
            }
            listAudience.add(am);
        }

        userService.setAudanceTraining(listAudience);
        audienceEditText.setText(getTotalSelectedAudience() + " Audiance selected");
    }

    @Override
    protected void onResume() {
        super.onResume();
        listAudience = userService.getAudianceTraining();
        listModule = userService.getModuleTraining();
        if(listModule != null){
            moduleEditText.setText(getTotalSelectedModule()+" Material Selected");
        }
        if(listAudience != null) {
            audienceEditText.setText(getTotalSelectedAudience() + " Audiance selected");
        }
    }

    private int getTotalSelectedAudience() {
        int i = 0;
        for (AudienceModel data : listAudience) {
            if (data.isSelected) i++;
        }
        return i;
    }

    private int getTotalSelectedModule() {
        int i = 0;
        for (ModuleSelectedModel data : listModule) {
            if (data.isSelected) i++;
        }
        return i;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        userService.setAudanceTraining(new ArrayList<AudienceModel>());
        userService.setModuleTraining(new ArrayList<ModuleSelectedModel>());
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        userService.setAudanceTraining(new ArrayList<AudienceModel>());
        userService.setModuleTraining(new ArrayList<ModuleSelectedModel>());
    }
}
