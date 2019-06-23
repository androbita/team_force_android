package com.app.salesapp.survey;

import android.database.Cursor;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Layout;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
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
    public static final String DROPDOWN_SUGGESTION = "dropdownSUGGESTION";
    public static final String LAYOUT = "layout";
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
            if (LAYOUT.equalsIgnoreCase(dataSurveyModel.type)){
                contract.addLayout(dataSurveyModel.label, dataSurveyModel.customFieldId);
            } else if (TEXT.equalsIgnoreCase(dataSurveyModel.type)) {
                if (VISIBLE.equalsIgnoreCase(dataSurveyModel.visible)) {
                    contract.addTextViewVisible(dataSurveyModel.label, dataSurveyModel.customFieldId, dataSurveyModel.childOf);
                } else{
                    contract.addTextViewInvisible(dataSurveyModel.label, dataSurveyModel.customFieldId, dataSurveyModel.childOf);
                }
            } else if (INT.equalsIgnoreCase(dataSurveyModel.type)) {
                if (VISIBLE.equalsIgnoreCase(dataSurveyModel.visible)){
                    contract.addTextIntViewVisible(dataSurveyModel.label, dataSurveyModel.customFieldId, dataSurveyModel.childOf);
                }else {
                    contract.addTextIntViewInvisible(dataSurveyModel.label, dataSurveyModel.customFieldId, dataSurveyModel.childOf);
                }
            } else if (DROPDOWN.equalsIgnoreCase(dataSurveyModel.type)) {
                if (dataSurveyModel.data != null && !dataSurveyModel.data.isEmpty()) {
                    if (VISIBLE.equalsIgnoreCase(dataSurveyModel.visible)) {
                            contract.addDropDownVisible(dataSurveyModel.label, dataSurveyModel.customFieldId, dataSurveyModel.data, dataSurveyModel.childOf);
                    } else {
                            contract.addDropDownInvisible(dataSurveyModel.label, dataSurveyModel.customFieldId,
                                    dataSurveyModel.data, dataSurveyModel.childOf);
                    }
                }
            } else if (DROPDOWN_SUGGESTION.equalsIgnoreCase(dataSurveyModel.type)){
                contract.addDropdownSuggestion(dataSurveyModel.label, dataSurveyModel.customFieldId, dataSurveyModel.data, dataSurveyModel.childOf);
            } else if (IMAGE.equalsIgnoreCase(dataSurveyModel.type)) {
                contract.showPhotoButton();
            } else if (SWITCH.equalsIgnoreCase(dataSurveyModel.type)){
                contract.addSwitch(dataSurveyModel.label, dataSurveyModel.customFieldId, dataSurveyModel.data, dataSurveyModel.childOf);
            }
        }
    }

    public void submitData(LinearLayout layoutForm, String photo) {
        CustomFieldModel customFieldModels = new CustomFieldModel();
        final List<String> idList = new ArrayList<>();
        final List<String> idListChild = new ArrayList<>();
        final List<String> valueList = new ArrayList<>();
        List<String> valueListChild = new ArrayList<>();


        String id = "";
        String value = "";

        for (int i = 0; i < layoutForm.getChildCount(); i++) {

            if (layoutForm.getChildAt(i) instanceof LinearLayout){
                LinearLayout layout = (LinearLayout) layoutForm.getChildAt(i);
                for (int a = 0; a <= layout.getChildCount(); a++) {
                    if (layout.getChildAt(a) instanceof LinearLayout) {
                        LinearLayout layoutContent = (LinearLayout) layout.getChildAt(a);
                        for (int b = 0; b <= layoutContent.getChildCount(); b++) {
                            id = String.valueOf(1);
                            value = "<NONE>";
                        }
                    }
                }
            } else if (layoutForm.getChildAt(i) instanceof Switch){
                id = String.valueOf(layoutForm.getChildAt(i).getId());
                value = "on";
            } else if (layoutForm.getChildAt(i) instanceof TextInputLayout) {
                FrameLayout frameLayout = (FrameLayout) ((TextInputLayout) layoutForm.getChildAt(i)).getChildAt(0);
                TextInputEditText textInputEditText = (TextInputEditText) frameLayout.getChildAt(0);

                id = String.valueOf(textInputEditText.getId());
                value = String.valueOf(textInputEditText.getText());
            } else if (layoutForm.getChildAt(i) instanceof Spinner) {
                id = String.valueOf(layoutForm.getChildAt(i).getId());
                value = ((Spinner) layoutForm.getChildAt(i)).getSelectedItem().toString();
            } else if (layoutForm.getChildAt(i) instanceof Switch){
                id = String.valueOf(layoutForm.getChildAt(i).getId());
                value = "<NONE>";
            } else {
                continue;
            }

            LinearLayout layout = (LinearLayout) layoutForm.getChildAt(i);
            for (int a = 0; a <= layout.getChildCount(); a++){
                String idChild = "";
                String valueChild = "";
                String baseValueChild = "Pilih";
                LinearLayout layout1;

                if (layout.getChildAt(a) instanceof AutoCompleteTextView){
                    AutoCompleteTextView atv = (AutoCompleteTextView) layout.getChildAt(a);
                    idChild = String.valueOf(layout.getChildAt(a).getId());
                    valueChild = String.valueOf(atv.getText());
                } else if (layout.getChildAt(a) instanceof Spinner){
                    idChild = String.valueOf(layout.getChildAt(a).getId());
                    if (((Spinner) layout.getChildAt(a)).getSelectedItem().toString().equalsIgnoreCase(baseValueChild)){
                        valueChild = "<NONE>";
                    } else {
                        valueChild = ((Spinner) layout.getChildAt(a)).getSelectedItem().toString();
                    }
                } else if (layout.getChildAt(a) instanceof Switch){
                    idChild = String.valueOf(layout.getChildAt(a).getId());
                    valueChild = "<NONE>";
                } else if (layout.getChildAt(a) instanceof LinearLayout){
                    layout1 = (LinearLayout) layout.getChildAt(a);
                    for (int b = 0; b <= layout1.getChildCount(); b++){
                        if (layout1.getChildAt(b) instanceof Switch){
                            idChild = String.valueOf(layout.getChildAt(b).getId());
                            valueChild = "<NONE>";
                        }
                    }
                } else {
                    continue;
                }
                idListChild.add(idChild);
                valueListChild.add(valueChild);

            }
        }

        idList.add(id);
        for (String loopId : idListChild) {
            idList.add(loopId);
        }

        valueList.add(value);
        for (String loopValue : valueListChild){
            valueList.add(loopValue);
        }

//        for (String loop : idList) {
//            contract.showToast(loop);
//        }
//
//        for (String loop : valueList){
//            contract.showToast(loop);
//        }

//        for (int x = 0; x <= idList.size(); x++){
//            contract.showToast(idList.get(x));
//        }

        customFieldModels.setCustomfieldid(idList);
        customFieldModels.setFieldvalue(valueList);
        customFieldModels.setProgramid(contract.getProgramId());
        customFieldModels.setToken(contract.getToken());
        customFieldModels.setPhoto(photo);

        salesAppService.postFormData(customFieldModels, new SalesAppService.ServiceCallback() {
            @Override
            public void onSuccess(Object response) {
                String value = "";
                int empty = 0;

                for (int i = 0; i < valueList.size(); i++) {
                    value = valueList.get(i).toString();
                    if (value.isEmpty()) {
                        empty++;
                    }
                }

                if (empty == 0) {
                    contract.successSubmit();
                } else {
                    contract.errorSubmitEmptyField("Field Tidak Boleh Kosong");
                }
            }

            @Override
            public void onError(NetworkError error) {
                contract.errorSubmit(error.getMessage());
            }
        });

    }
}
