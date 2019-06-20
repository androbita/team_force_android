package com.app.salesapp.survey;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
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
    private LinearLayout layoutId;
    private LinearLayout layoutAction;
    private Button buttonSubmit;
    private LinearLayout layoutPhoto;
    private Button btnPhoto;
    private ImageView picFrame;
    private ImageView resetImage;
    private LinearLayout layoutHeaderSwitch;
    private LinearLayout layoutContentSwitch;

    private String photos = "";
    private Camera camera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AutoCompleteTextView atv;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);

        surveyPresenter = new SurveyPresenter(this, salesAppService);
        loadingDialog.show();
        surveyPresenter.getFromData(new SurveyRequestModel(userService.getUserPreference().token, userService.getCurrentProgram()));

        layoutId = findViewById(R.id.layout_identity);
        layoutAction = findViewById(R.id.layout_action);

        buttonSubmit = findViewById(R.id.button_submit);
        layoutPhoto = findViewById(R.id.layout_photo);
        btnPhoto = findViewById(R.id.btnPhoto);
        resetImage = findViewById(R.id.resetImage);
        picFrame = findViewById(R.id.pic_frame);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                surveyPresenter.submitData(layoutId, photos);
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
    public void addTextViewVisible(String label, String customFieldId, List<String> data) {
        TextInputLayout textInputLayout = new TextInputLayout(this);
        TextInputEditText editText = new TextInputEditText(this);
        editText.setId(Integer.parseInt(customFieldId));
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        editText.setBackgroundColor(Color.parseColor("#EBECEC"));
        textInputLayout.addView(editText);
        TextView textView = new TextView(this);
        textView.setPadding(0, 20, 0, 0);
        textView.setText(label);
        layoutId.addView(textView);
        layoutId.addView(textInputLayout);
    }

    @Override
    public void addTextViewInvisible(String label, String customFieldId, String childOf) {
        TextInputLayout textInputLayout = new TextInputLayout(this);
        final TextInputEditText editText = new TextInputEditText(this);
        editText.setId(Integer.parseInt(customFieldId));
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        editText.setBackgroundColor(Color.parseColor("#EBECEC"));

        textInputLayout.addView(editText);
        TextView textView = new TextView(this);
        textView.setPadding(0, 20, 0, 0);
        textView.setText(label);
        textView.setId(Integer.parseInt(childOf));
        layoutAction.addView(textView);
        layoutAction.addView(textInputLayout);
        textView.setVisibility(View.GONE);
        textInputLayout.setVisibility(View.GONE);
    }

    @Override
    public void addDropDownVisible(String label, String customFieldId, List<String> data) {
        TextView textView = new TextView(this);
        textView.setPadding(0, 20, 0, 0);
        textView.setText(label);
        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setId(Integer.parseInt(customFieldId));
        layoutId.addView(textView);
        layoutId.addView(spinner);
    }

    @Override
    public void addDropDownInvisible(String label, String customFieldId, List<String> data, String childOf) {
        TextView textView = new TextView(this);
        textView.setPadding(0, 20, 0, 0);
        textView.setText(label);
        textView.setId(Integer.parseInt(childOf));
        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setId(Integer.parseInt(customFieldId));
        layoutAction.addView(textView);
        layoutAction.addView(spinner);
//        textView.setVisibility(View.GONE);
//        spinner.setVisibility(View.GONE);
    }

    @Override
    public void addSwitch(final String label, final String customFieldId, final List<String> data) {

        layoutHeaderSwitch = new LinearLayout(this);
        layoutHeaderSwitch.setOrientation(LinearLayout.HORIZONTAL);
        layoutHeaderSwitch.setPadding(0,20,0,0);

        layoutContentSwitch = new LinearLayout(this);
        layoutContentSwitch.setOrientation(LinearLayout.VERTICAL);

        final TextView txt1 = new TextView(this);
        txt1.setPadding(0,20,0,0);
        txt1.setText(label);

        final TextView txt2 = new TextView(this);
        txt2.setPadding(0,20,0,0);

        final Switch sw = new Switch(this);
        final LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(100,0,0,0);
        sw.setLayoutParams(layoutParams);
        sw.setPadding(20,0,0,0);
        sw.setId(Integer.parseInt(customFieldId));

        layoutHeaderSwitch.addView(txt1);
        layoutHeaderSwitch.addView(sw);
        layoutAction.addView(layoutHeaderSwitch);

//        layoutId.removeView(layoutContentSwitch);
//
//        layoutId.addView(layoutContentSwitch);
//        layoutContentSwitch.setVisibility(View.GONE);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true){
                    int fieldId = Integer.parseInt(customFieldId);

                    for (int i = 0; i < layoutAction.getChildCount(); i++){
                        int id = layoutAction.getChildAt(i).getId();
//                        TextView textView = (TextView) findViewById(layoutForm.getChildAt(i).getId());
//                        textView.setId(Integer.parseInt(customFieldId));

                        if (id == sw.getId()){
                            if (layoutAction.getChildAt(i) instanceof TextView){
                                i++;
                                sw.setId(Integer.parseInt(customFieldId)+100+i);
                                TextView textView = (TextView) findViewById(layoutAction.getChildAt(i+1).getId());
                                Toast.makeText(SurveyActivity.this, ""+layoutAction.getChildAt(i+1).getId()+"."+i, Toast.LENGTH_SHORT).show();
                                textView.setVisibility(View.VISIBLE);
                            }else if (layoutAction.getChildAt(i) instanceof Spinner){
                                i++;
                                sw.setId(Integer.parseInt(customFieldId)+100+i);
                                Spinner spinner = (Spinner) findViewById(layoutAction.getChildAt(i+1).getId());
                                spinner.setVisibility(View.VISIBLE);
                            }
                        }else {
                            if (layoutAction.getChildAt(i) instanceof TextView){
//                                TextView textView = (TextView) findViewById(layoutForm.getChildAt(i).getId());
//                                textView.setVisibility(View.VISIBLE);
//                                Toast.makeText(SurveyActivity.this, ""+layoutForm.getChildAt(i).getId()+"."+i, Toast.LENGTH_SHORT).show();
                            }else
                            if (layoutAction.getChildAt(i) instanceof Spinner){
                                Spinner spinner = (Spinner) findViewById(layoutAction.getChildAt(i).getId());
                                spinner.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    sw.setId(Integer.parseInt(customFieldId));
                }else if (isChecked == false){
                    TextView textView;
                    for (int i = 0; i < layoutAction.getChildCount(); i++) {
                        int id = layoutAction.getChildAt(i).getId();

                        if (id == sw.getId()){

                            if (layoutAction.getChildAt(i) instanceof TextView) {

                                sw.setId(Integer.parseInt(customFieldId) + 1000+i);
                                textView = (TextView) findViewById(layoutAction.getChildAt(i).getId());
                                textView.setVisibility(View.GONE);
                                textView.setId(Integer.parseInt(customFieldId) + 1000+i);
//                                Spinner spinner = (Spinner) findViewById(layoutForm.getChildAt(i+1).getId());
//                                spinner.setVisibility(View.GONE);
                                sw.setId(Integer.parseInt(customFieldId));
                            } else if (layoutAction.getChildAt(i) instanceof Spinner) {
//                                Spinner spinner = (Spinner) findViewById(id);
//                                spinner.setVisibility(View.GONE);
                                Toast.makeText(SurveyActivity.this, "ini spinner", Toast.LENGTH_SHORT).show();
                            }else if (layoutAction.getChildAt(i) instanceof Switch){
                                Toast.makeText(SurveyActivity.this, "ini switch", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(SurveyActivity.this, "tidak ketemu", Toast.LENGTH_SHORT).show();
                                continue;
                            }
                        }else {
                            if (layoutAction.getChildAt(i) instanceof Spinner){
                                Spinner spinner = (Spinner) findViewById(layoutAction.getChildAt(i).getId());
                                spinner.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void addDropdownSwitch(String label, String customFieldId, List<String> data) {

        TextView textView = new TextView(this);
        textView.setPadding(0, 20, 0, 0);
        textView.setText(label);
        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setId(Integer.parseInt(customFieldId));

        layoutContentSwitch = new LinearLayout(this);
        layoutContentSwitch.setOrientation(LinearLayout.VERTICAL);
        layoutContentSwitch.setId(Integer.parseInt(customFieldId));
        layoutContentSwitch.addView(textView);
        layoutContentSwitch.addView(spinner);
        layoutAction.addView(layoutContentSwitch);
    }

    @Override
    public void addTextViewSwitch(String label, String customFieldId) {
        TextInputLayout textInputLayout = new TextInputLayout(this);
        TextInputEditText editText = new TextInputEditText(this);
        editText.setId(Integer.parseInt(customFieldId));
        textInputLayout.addView(editText);
        TextView textView = new TextView(this);
        textView.setPadding(0, 20, 0, 0);
        textView.setText(label);
//        layoutContentSwitch.removeView(textView);
//        layoutContentSwitch.removeView(textInputLayout);
        layoutContentSwitch.addView(textView);
        layoutContentSwitch.addView(textInputLayout);
        layoutAction.addView(layoutContentSwitch);
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
    public void addTextIntViewVisible(String label, String customFieldId) {
        TextInputLayout textInputLayout = new TextInputLayout(this);
        TextInputEditText editText = new TextInputEditText(this);
        editText.setId(Integer.parseInt(customFieldId));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        textInputLayout.addView(editText);
        TextView textView = new TextView(this);
        textView.setPadding(0, 20, 0, 0);
        textView.setText(label);
        layoutId.addView(textView);
        layoutId.addView(textInputLayout);
    }

    @Override
    public void addTextIntViewInvisible(String label, String customFieldId, String childOf) {
        TextInputLayout textInputLayout = new TextInputLayout(this);
        TextInputEditText editText = new TextInputEditText(this);
        editText.setId(Integer.parseInt(customFieldId));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        textInputLayout.addView(editText);
        TextView textView = new TextView(this);
        textView.setPadding(0, 20, 0, 0);
        textView.setText(label);
        layoutAction.addView(textView);
        layoutAction.addView(textInputLayout);
        textView.setVisibility(View.INVISIBLE);
        textInputLayout.setVisibility(View.INVISIBLE);
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
