package com.app.salesapp.survey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DataSurveyModel implements Serializable {
    @SerializedName("custom_field_id")
    public String customFieldId;
    public String label;
    public String field;
    public String type;
    public List<String> data;
}
