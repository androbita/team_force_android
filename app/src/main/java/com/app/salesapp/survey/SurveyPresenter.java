package com.app.salesapp.survey;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.survey.submit.CustomFieldModel;
import com.app.salesapp.survey.submit.SubmitSurveyRequestModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class SurveyPresenter {
    public static final String TEXT = "text";
    public static final String INT = "int";
    public static final String DROPDOWN = "dropdown";
    private SalesAppService salesAppService;
    private CompositeSubscription subscriptions;
    private SurveyContract contract;
    private List<DataSurveyModel> surveyForm;

    public SurveyPresenter (SurveyContract contract, SalesAppService salesAppService) {
        this.contract = contract;
        this.salesAppService = salesAppService;
        this.subscriptions = new CompositeSubscription();
    }

    public void getFromData(SurveyRequestModel requestModel) {

        Subscription subscribe = salesAppService.getFormData(requestModel, new SalesAppService.ServiceCallback<Response<List<DataSurveyModel>>>() {
            @Override
            public void onSuccess(Response<List<DataSurveyModel>> response) {
                contract.onSuccessGetFromData(response);
            }
            @Override
            public void onError(NetworkError error) {
                contract.onShowErrorMessage(error.getMessage());
            }
        });
        subscriptions.add(subscribe);
    }


    public void setSurveyForm(List<DataSurveyModel> surveyForm) {
        this.surveyForm = surveyForm;

        for (DataSurveyModel dataSurveyModel: surveyForm) {
            if (TEXT.equalsIgnoreCase(dataSurveyModel.type)) {
                contract.addTextView(dataSurveyModel.label, dataSurveyModel.customFieldId);
            } else if (INT.equalsIgnoreCase(dataSurveyModel.type)) {
                contract.addTextView(dataSurveyModel.label, dataSurveyModel.customFieldId);
            } else if (DROPDOWN.equalsIgnoreCase(dataSurveyModel.type)) {
                if (dataSurveyModel.data != null && !dataSurveyModel.data.isEmpty()) {
                    contract.addDropDown(dataSurveyModel.label, dataSurveyModel.customFieldId, dataSurveyModel.data);
                }
            }
        }
    }

    public void submitData(LinearLayout layoutForm) {
        List<CustomFieldModel> customFieldModels = new ArrayList<>();
        for (int i = 0; i < layoutForm.getChildCount(); i++) {
            CustomFieldModel customFieldModel = new CustomFieldModel();

            String id = "";
            String value = "";
            if (layoutForm.getChildAt(i) instanceof TextInputLayout) {
                FrameLayout frameLayout = (FrameLayout) ((TextInputLayout) layoutForm.getChildAt(i)).getChildAt(0);
                TextInputEditText textInputEditText = (TextInputEditText) frameLayout.getChildAt(0);

                id = String.valueOf(textInputEditText.getId());
                value = String.valueOf(textInputEditText.getText());
            } else if (layoutForm.getChildAt(i) instanceof Spinner) {
                id = String.valueOf(layoutForm.getChildAt(i).getId());
                value = ((Spinner) layoutForm.getChildAt(i)).getSelectedItem().toString();
            } else {
                continue;
            }

            customFieldModel.setId(id);
            customFieldModel.setValue(value);
            customFieldModels.add(customFieldModel);
        }

        SubmitSurveyRequestModel submitSurveyRequestModel = new SubmitSurveyRequestModel(customFieldModels, "67", "631");
        salesAppService.postFormData(submitSurveyRequestModel, new SalesAppService.ServiceCallback() {
            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onError(NetworkError error) {

            }
        });

    }
}
