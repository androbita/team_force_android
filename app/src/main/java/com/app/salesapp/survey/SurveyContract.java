package com.app.salesapp.survey;


import com.app.salesapp.network.Response;

import java.util.List;

public interface SurveyContract {
    void onSuccessGetFromData(Response<List<DataSurveyModel>> response);

    void onShowErrorMessage(String message);

    void addTextViewVisible(String label, String customFieldId, List<String> data);

    void addTextViewInvisible (String label, String customFieldId, String childOf);

    void addDropDownVisible(String label, String customFieldId, List<String> data);

    void addDropDownInvisible (String label, String customFieldId, List<String> data, String childOf);

    void addSwitch(String label, String customFieldId, List<String> data);

    void addDropdownSwitch(String label, String customFieldId, List<String> data);

    void addTextViewSwitch(String label, String customFieldId);

    void errorSubmit(String message);

    void successSubmit();

    String getProgramId();

    String getToken();

    void addTextIntViewVisible(String label, String customFieldId);

    void addTextIntViewInvisible (String label, String customFieldId, String childOf);

    void showPhotoButton();

}
