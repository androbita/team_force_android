package com.app.salesapp.survey;

import android.annotation.SuppressLint;
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

import org.w3c.dom.Text;

import java.util.List;

import javax.inject.Inject;


public class SurveyActivity extends BaseActivity implements SurveyContract {

    private SurveyPresenter surveyPresenter;

    @Inject
    public SalesAppService salesAppService;

    @Inject
    public UserService userService;
    private LinearLayout layoutForm;
    private LinearLayout layoutAction;
    private LinearLayout layoutUtama;
    private Button buttonSubmit;
    private LinearLayout layoutPhoto;
    private Button btnPhoto;
    private ImageView picFrame;
    private ImageView resetImage;
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

        layoutUtama = findViewById(R.id.layout_utama);
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
    public void addLayout(String label, String customFieldId) {
        LinearLayout.LayoutParams parameter = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        parameter.setMargins(0,20,0,20);
        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(parameter);
        layout.setOrientation(LinearLayout.VERTICAL);
//        layout.setPadding(20,20,0,0);
        layout.setId(Integer.parseInt(customFieldId));
        layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        TextView textView = new TextView(this);
        textView.setText(label);
        textView.setTextSize(18);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setBackgroundColor(Color.parseColor("#ff295de6"));
        textView.setPadding(0,20,0,20);
        layout.addView(textView);
        layoutForm.addView(layout);

    }

    @Override
    public void addTextViewVisible(String label, String customFieldId, String childOf) {

        for (int i = 0; i <= layoutForm.getChildCount(); i++) {
            if (layoutForm.getChildAt(i) instanceof LinearLayout) {
                if (Integer.parseInt(childOf) == layoutForm.getChildAt(i).getId()) {
                    LinearLayout layout = (LinearLayout) layoutForm.getChildAt(i);

                    TextInputLayout textInputLayout = new TextInputLayout(this);
                    TextInputEditText editText = new TextInputEditText(this);
                    editText.setId(Integer.parseInt(customFieldId));
                    editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
//        editText.setBackgroundColor(Color.parseColor("#EBECEC"));
                    textInputLayout.addView(editText);
                    TextView textView = new TextView(this);
                    textView.setPadding(0, 20, 0, 0);
                    textView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    textView.setText(label);
                    layout.addView(textView);
                    layout.addView(textInputLayout);
                }
            }
            else {
                Toast.makeText(this, ""+i+" gagal", Toast.LENGTH_SHORT).show();
                continue;
            }
        }
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
        textView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        textView.setText(label);
        textView.setId(Integer.parseInt(childOf));
        layoutAction.addView(textView);
        layoutAction.addView(textInputLayout);
        textView.setVisibility(View.GONE);
        textInputLayout.setVisibility(View.GONE);
    }

    @Override
    public void addDropDownVisible(String label, String customFieldId, List<String> data, String childOf) {

        for (int i = 0; i<= layoutForm.getChildCount(); i++){
            if (layoutForm.getChildAt(i) instanceof LinearLayout){
                if (Integer.parseInt(childOf) == layoutForm.getChildAt(i).getId()) {
    
                    LinearLayout layout = (LinearLayout) layoutForm.getChildAt(i);
    
                    TextView textView = new TextView(this);
                    textView.setPadding(20, 20, 20, 0);
                    textView.setText(label);
    //                textView.setBackgroundColor(Color.parseColor("#FFFFFF"));
    
                    Spinner spinner = new Spinner(this);
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
                    spinner.setAdapter(spinnerArrayAdapter);
                    spinner.setId(Integer.parseInt(customFieldId));

//                    String pilih = "Silahkan Pilih";
//                    int spinnerPosition = spinnerArrayAdapter.getPosition(pilih);
//                    spinner.setSelection(spinnerPosition);

                    layout.addView(textView);
                    layout.addView(spinner);
                }
            }
        }
    }

    @Override
    public void addDropDownInvisible(String label, String customFieldId, List<String> data, String childOf) {

        for (int i = 0; i < layoutForm.getChildCount(); i++){
            if (layoutForm.getChildAt(i).getId() == Integer.parseInt(childOf)){
                LinearLayout layout = (LinearLayout) layoutForm.getChildAt(i);

                TextView textView = new TextView(this);
                textView.setPadding(20, 20, 20, 0);
                textView.setText(label);
                textView.setId(Integer.parseInt(childOf));

                Spinner spinner = new Spinner(this);
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
                spinner.setAdapter(spinnerArrayAdapter);
                spinner.setId(Integer.parseInt(customFieldId));



                layout.addView(textView);
                layout.addView(spinner);
                textView.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void addSwitch(final String label, final String customFieldId, final List<String> data, String childOf) {

        LinearLayout.LayoutParams parameter = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        parameter.setMargins(0, 0, 0, 20);
        LinearLayout layoutAllSwitch = new LinearLayout(this);
        layoutAllSwitch.setLayoutParams(parameter);
        layoutAllSwitch.setOrientation(LinearLayout.VERTICAL);
        layoutAllSwitch.setBackgroundColor(Color.parseColor("#FFFFFF"));

        LinearLayout layoutHeaderSwitch = new LinearLayout(this);
        layoutHeaderSwitch.setLayoutParams(parameter);
        layoutHeaderSwitch.setOrientation(LinearLayout.HORIZONTAL);
        layoutHeaderSwitch.setPadding(0,20,0,20);
        layoutHeaderSwitch.setBackgroundColor(Color.parseColor("#ff295de6"));

        final TextView textView = new TextView(this);
        textView.setPadding(0,20,0,0);
        textView.setText(label);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTextSize(15);

        final Switch sw = new Switch(this);
        final LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(100,0,0,0);
        sw.setLayoutParams(layoutParams);
        sw.setPadding(20,0,0,0);


        layoutHeaderSwitch.addView(textView);
        layoutHeaderSwitch.addView(sw);
        layoutAllSwitch.setId(Integer.parseInt(customFieldId));
        layoutAllSwitch.addView(layoutHeaderSwitch);
        layoutForm.addView(layoutAllSwitch);

        final int customId = Integer.parseInt(customFieldId)+1000;

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int idLayout = 0;
                LinearLayout layout = null;


                for (int i = 0; i <= layoutForm.getChildCount(); i++) {
                    if (layoutForm.getChildAt(i) instanceof LinearLayout) {
                        idLayout = layoutForm.getChildAt(i).getId();
                        idLayout += 1000 + i;

                        int idSwitch = Integer.parseInt(customFieldId);
                        idSwitch += 1000 + i;
                        sw.setId(idSwitch);


                    if (isChecked == true) {
                        if (idLayout == sw.getId()) {
                            layout = (LinearLayout) layoutForm.getChildAt(i);
                            for (int a = 0; a <= layout.getChildCount(); a++) {
                                if (layout.getChildAt(a) instanceof TextView) {
                                    TextView textView1 = (TextView) layout.getChildAt(a);
                                    String text = textView1.getText().toString();

                                    textView1.setVisibility(View.VISIBLE);
                                } else if (layout.getChildAt(a) instanceof Spinner) {
                                    Spinner spinner = (Spinner) layout.getChildAt(a);
                                    String text = spinner.getSelectedItem().toString();

//                                    spinner.removeViewAt(0);
                                    spinner.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            continue;
                        }
                    } else if (isChecked == false) {
                        if (idLayout == sw.getId()) {
                            layout = (LinearLayout) layoutForm.getChildAt(i);
                            for (int a = 0; a <= layout.getChildCount(); a++) {
                                if (layout.getChildAt(a) instanceof TextView) {
                                    TextView textView1 = (TextView) layout.getChildAt(a);
                                    String text = textView1.getText().toString();

                                    textView1.setVisibility(View.GONE);
                                } else if (layout.getChildAt(a) instanceof Spinner) {
                                    Spinner spinner = (Spinner) layout.getChildAt(a);
                                    String text = spinner.getSelectedItem().toString();

                                    spinner.setSelection(0);
                                    spinner.setVisibility(View.GONE);

                                }
                            }
                        }
                    }
                }
                }
            }
        });
    }

    @Override
    public void addDropdownSuggestion(String label, String customFieldId, List<String> data, String childOf) {
        for (int i = 0; i <= layoutForm.getChildCount(); i++) {
            if (layoutForm.getChildAt(i) instanceof LinearLayout) {
                if (Integer.parseInt(childOf) == layoutForm.getChildAt(i).getId()) {


                    LinearLayout layout = (LinearLayout) layoutForm.getChildAt(i);

                    TextView textView = new TextView(this);
                    textView.setPadding(20, 20, 20, 20);
                    textView.setText(label);
                    textView.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    AutoCompleteTextView atv = new AutoCompleteTextView(this);
                    ArrayAdapter<String> atvArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
                    atv.setAdapter(atvArrayAdapter);
                    atv.setId(Integer.parseInt(customFieldId));
//                    atv.setBackgroundResource(R.drawable.custom_border);
                    atv.setPadding(20,20,20,20);

                    layout.addView(textView);
                    layout.addView(atv);
                } else {
                    continue;
                }
            }
        }
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
    public void errorSubmitEmptyField(String message){
        loadingDialog.dismiss();
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
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
    public void addTextIntViewVisible(String label, String customFieldId, String childOf) {
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

    @Override
    public void showToast(String message){
        Toast.makeText(this, ""+message, Toast.LENGTH_SHORT).show();
    }
}
