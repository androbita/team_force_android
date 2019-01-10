package com.app.salesapp.survey.submit;

import java.io.Serializable;
import java.util.List;

public class SubmitSurveyRequestModel implements Serializable {
    private List<CustomFieldModel> customFieldList;
    private String programId;
    private String userId;

    public SubmitSurveyRequestModel(List<CustomFieldModel> customFieldList, String programId, String userId) {
        this.customFieldList = customFieldList;
        this.programId = programId;
        this.userId = userId;
    }

    public List<CustomFieldModel> getCustomFieldList() {
        return customFieldList;
    }

    public void setCustomFieldList(List<CustomFieldModel> customFieldList) {
        this.customFieldList = customFieldList;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
