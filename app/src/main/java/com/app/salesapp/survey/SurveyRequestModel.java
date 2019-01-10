package com.app.salesapp.survey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SurveyRequestModel implements Serializable {
    @SerializedName("userid")
    private String userId;
    @SerializedName("programid")
    private String programId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public SurveyRequestModel(String userId, String programId) {
        this.userId = userId;
        this.programId = programId;
    }
}
