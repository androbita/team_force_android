package com.app.salesapp.survey;


import com.app.salesapp.network.Response;

import java.util.List;

public interface SurveyContract {
    void onSuccessGetFromData(Response<List<DataSurveyModel>> response);

    void onShowErrorMessage(String message);

    void addTextView(String label, String customFieldId);

    void addDropDown(String label, String customFieldId, List<String> data);
}
