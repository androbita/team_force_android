package com.app.salesapp.survey;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.List;

import javax.inject.Inject;



// TODO : Populate data from dynamic UI and do submit
public class SurveyActivity extends BaseActivity implements SurveyContract {

    private SurveyPresenter surveyPresenter;

    @Inject
    public SalesAppService salesAppService;

    @Inject
    public UserService userService;
    private LinearLayout layoutForm;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);

        surveyPresenter = new SurveyPresenter(this, salesAppService);
        // TODO : Change with real data
        surveyPresenter.getFromData(new SurveyRequestModel("631", "67"));

        layoutForm = findViewById(R.id.layout_form);
        buttonSubmit = findViewById(R.id.button_submit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surveyPresenter.submitData(layoutForm);
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
    }

    @Override
    public void onSuccessGetFromData(Response<List<DataSurveyModel>> response) {
        surveyPresenter.setSurveyForm(response.data);
    }

    @Override
    public void onShowErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addTextView(String label, String customFieldId) {
        TextInputLayout textInputLayout = new TextInputLayout(this);
        textInputLayout.setPadding(0, 20, 0, 0);
        TextInputEditText editText = new TextInputEditText(this);
        editText.setId(Integer.parseInt(customFieldId));
        textInputLayout.addView(editText);
        TextView textView = new TextView(this);
        textView.setText(label);
        layoutForm.addView(textView);
        layoutForm.addView(textInputLayout);
    }

    @Override
    public void addDropDown(String label, String customFieldId, List<String> data) {
        TextView textView = new TextView(this);
        textView.setPadding(5, 30, 0, 0);
        textView.setText(label);
        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setId(Integer.parseInt(customFieldId));
        layoutForm.addView(textView);
        layoutForm.addView(spinner);
    }
}
