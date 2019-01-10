package com.app.salesapp.distribution.model;

import com.google.gson.annotations.SerializedName;

public class DistributionRequest {

    @SerializedName("page")
    public int page;

    @SerializedName("token")
    public String token;
    @SerializedName("program_id")
    public String programId;

    public DistributionRequest(String token, String programId, int page) {
        this.token = token;
        this.programId = programId;
        this.page = page;
    }
}
