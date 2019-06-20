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
    public static final String IMAGE = "image";
    public static final String SWITCH = "switch";
    public static final String DROPDOWN_SWITCH = "dropdownswitch";
    public static final String TEXT_SWITCH = "textswitch";
    public static final String CHILD_OF = "";
    public static final String VISIBLE = "1";
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
                if (response.data != null && response.data.size() > 0) {
                    contract.onSuccessGetFromData(response);
                } else {
                    contract.onShowErrorMessage("Tidak Ada Data");
                }
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
                if (VISIBLE.equalsIgnoreCase(dataSurveyModel.visible)) {
                    contract.addTextViewVisible(dataSurveyModel.label, dataSurveyModel.customFieldId, dataSurveyModel.data);
                } else{
                    contract.addTextViewInvisible(dataSurveyModel.label, dataSurveyModel.customFieldId, dataSurveyModel.childOf);
                }
            } else if (INT.equalsIgnoreCase(dataSurveyModel.type)) {
                if (VISIBLE.equalsIgnoreCase(dataSurveyModel.visible)){
                    contract.addTextIntViewVisible(dataSurveyModel.label, dataSurveyModel.customFieldId);
                }else {
                    contract.addTextIntViewInvisible(dataSurveyModel.label, dataSurveyModel.customFieldId, dataSurveyModel.childOf);
                }
            } else if (DROPDOWN.equalsIgnoreCase(dataSurveyModel.type)) {
                if (dataSurveyModel.data != null && !dataSurveyModel.data.isEmpty()) {
                    if (VISIBLE.equalsIgnoreCase(dataSurveyModel.visible)) {
                        if (dataSurveyModel.data.size() < 5) {
                            contract.addDropDownVisible(dataSurveyModel.label, dataSurveyModel.customFieldId, dataSurveyModel.data);
                        } else {
                            contract.addTextViewVisible(dataSurveyModel.label, dataSurveyModel.customFieldId, dataSurveyModel.data);
                        }
                    } else {
                            contract.addDropDownInvisible(dataSurveyModel.label, dataSurveyModel.customFieldId,
                                    dataSurveyModel.data, dataSurveyModel.childOf);
                    }
                }
            } else if (IMAGE.equalsIgnoreCase(dataSurveyModel.type)) {
                contract.showPhotoButton();
            } else if (SWITCH.equalsIgnoreCase(dataSurveyModel.type)){
                contract.addSwitch(dataSurveyModel.label, dataSurveyModel.customFieldId, dataSurveyModel.data);
            }
//            else if (DROPDOWN_SWITCH.equalsIgnoreCase(dataSurveyModel.type)){
//                contract.addDropdownSwitch(dataSurveyModel.label, dataSurveyModel.customFieldId, dataSurveyModel.data);
//            }else if (TEXT_SWITCH.equalsIgnoreCase(dataSurveyModel.type)){
//                contract.addTextViewSwitch(dataSurveyModel.label, dataSurveyModel.customFieldId);
//            }
        }
    }

    public void submitData(LinearLayout layoutForm, String photo) {
        CustomFieldModel customFieldModels = new CustomFieldModel();
        List<String> idList = new ArrayList<>();
        List<String> valueList = new ArrayList<>();
        for (int i = 0; i < layoutForm.getChildCount(); i++) {
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
            idList.add(id);
            valueList.add(value);
        }

        customFieldModels.setCustomfieldid(idList);
        customFieldModels.setFieldvalue(valueList);
        customFieldModels.setProgramid(contract.getProgramId());
        customFieldModels.setToken(contract.getToken());
        customFieldModels.setPhoto(photo);

        salesAppService.postFormData(customFieldModels, new SalesAppService.ServiceCallback() {
            @Override
            public void onSuccess(Object response) {
                contract.successSubmit();
            }

            @Override
            public void onError(NetworkError error) {
                contract.errorSubmit(error.getMessage());
            }
        });

    }
}
