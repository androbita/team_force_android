package com.app.salesapp.chart;

import com.google.gson.annotations.SerializedName;

public class UserRequestModel {

    @SerializedName("token")
    public String token;

    @SerializedName("program_id")
    public String programId;

    public UserRequestModel() {

    }

    public UserRequestModel(String token, String programId) {
        this.token = token;
        this.programId = programId;
    }
}
