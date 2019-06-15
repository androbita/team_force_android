package com.app.salesapp.survey;


import com.app.salesapp.network.Response;

import java.util.List;

public interface SurveyContract {
    void onSuccessGetFromData(Response<List<DataSurveyModel>> response);

    void onShowErrorMessage(String message);

    void addTextView(String label, String customFieldId);

    void addDropDown(String label, String customFieldId, List<String> data);

    void addSwitch(String label, String customFieldId, List<String> data);

    void addDropdownSwitch(String label, String customFieldId, List<String> data);

    void addTextViewSwitch(String label, String customFieldId);

    void errorSubmit(String message);

    void successSubmit();

    String getProgramId();

    String getToken();

    void addTextIntView(String label, String customFieldId);

    void showPhotoButton();
}
