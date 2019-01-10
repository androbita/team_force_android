package com.app.salesapp.training.model;

import com.google.gson.annotations.SerializedName;

public class TrainingRequest {

    @SerializedName("page")
    public int page;

    @SerializedName("token")
    public String token;
    @SerializedName("program_id")
    public String programId;

    public TrainingRequest(String token, String programId, int page) {
        this.token = token;
        this.programId = programId;
        this.page = page;
    }
}
