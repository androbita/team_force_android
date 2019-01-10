package com.app.salesapp.survey;

import java.io.Serializable;
import java.util.List;

public class SurveyResponseModel implements Serializable {
    public String response;
    public String message;
    public List<DataSurveyModel> data;

}
