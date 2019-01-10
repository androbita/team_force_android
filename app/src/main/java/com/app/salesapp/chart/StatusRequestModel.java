package com.app.salesapp.chart;

import com.google.gson.annotations.SerializedName;

public class StatusRequestModel {

    @SerializedName("token")
    public String token;

    @SerializedName("program_id")
    public String programId;

    public StatusRequestModel() {

    }

    public StatusRequestModel(String token, String programId) {
        this.token = token;
        this.programId = programId;
    }
}
