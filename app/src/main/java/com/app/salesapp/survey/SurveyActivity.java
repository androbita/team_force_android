package com.app.salesapp.survey;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.salesapp.BaseActivity;
import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.network.Response;
import com.app.salesapp.user.UserService;
import com.app.salesapp.util.Util;
import com.mindorks.paracamera.Camera;

import java.util.List;

import javax.inject.Inject;


public class SurveyActivity extends BaseActivity implements SurveyContract {

    private SurveyPresenter surveyPresenter;

    @Inject
    public SalesAppService salesAppService;

    @Inject
    public UserService userService;
    private LinearLayout layoutForm;
    private Button buttonSubmit;
    private LinearLayout layoutPhoto;
    private Button btnPhoto;
    private ImageView picFrame;
    private ImageView resetImage;

    private String photos = "";
    private Camera camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);

        surveyPresenter = new SurveyPresenter(this, salesAppService);
        loadingDialog.show();
        surveyPresenter.getFromData(new SurveyRequestModel(userService.getUserPreference().token, userService.getCurrentProgram()));

        layoutForm = findViewById(R.id.layout_form);
        buttonSubmit = findViewById(R.id.button_submit);
        layoutPhoto = findViewById(R.id.layout_photo);
        btnPhoto = findViewById(R.id.btnPhoto);
        resetImage = findViewById(R.id.resetImage);
        picFrame = findViewById(R.id.pic_frame);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                surveyPresenter.submitData(layoutForm, photos);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Survey");
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        resetImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photos = null;
                picFrame.setImageBitmap(null);
                picFrame.setVisibility(View.GONE);
                resetImage.setVisibility(View.GONE);
            }
        });


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

        initCamera();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            Bitmap bitmap = camera.getCameraBitmap();
            if (bitmap != null) {
                btnPhoto.setText("RETAKE PHOTO");
                picFrame.setVisibility(View.VISIBLE);
                resetImage.setVisibility(View.VISIBLE);
                Util.movePhotoToGallery(getApplicationContext(), camera.getCameraBitmapPath(),"Teamforce_" + System.currentTimeMillis()+".jpg");
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
    public void onSuccessGetFromData(Response<List<DataSurveyModel>> response) {
        surveyPresenter.setSurveyForm(response.data);
        loadingDialog.dismiss();
    }

    @Override
    public void onShowErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        loadingDialog.dismiss();
    }

    @Override
    public void addTextView(String label, String customFieldId) {
        TextInputLayout textInputLayout = new TextInputLayout(this);
        TextInputEditText editText = new TextInputEditText(this);
        editText.setId(Integer.parseInt(customFieldId));
        textInputLayout.addView(editText);
        TextView textView = new TextView(this);
        textView.setPadding(0, 20, 0, 0);
        textView.setText(label);
        layoutForm.addView(textView);
        layoutForm.addView(textInputLayout);
    }

    @Override
    public void addDropDown(String label, String customFieldId, List<String> data) {
        TextView textView = new TextView(this);
        textView.setPadding(0, 20, 0, 0);
        textView.setText(label);
        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setId(Integer.parseInt(customFieldId));
        layoutForm.addView(textView);
        layoutForm.addView(spinner);
    }

    @Override
    public void errorSubmit(String message) {
        loadingDialog.dismiss();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void successSubmit() {
        loadingDialog.dismiss();
        Toast.makeText(this, R.string.success_submit, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public String getProgramId() {
        return userService.getCurrentProgram();
    }

    @Override
    public String getToken() {
        return userService.getUserPreference().token;
    }

    @Override
    public void addTextIntView(String label, String customFieldId) {
        TextInputLayout textInputLayout = new TextInputLayout(this);
        TextInputEditText editText = new TextInputEditText(this);
        editText.setId(Integer.parseInt(customFieldId));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        textInputLayout.addView(editText);
        TextView textView = new TextView(this);
        textView.setPadding(0, 20, 0, 0);
        textView.setText(label);
        layoutForm.addView(textView);
        layoutForm.addView(textInputLayout);
    }

    @Override
    public void showPhotoButton() {
        layoutPhoto.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
