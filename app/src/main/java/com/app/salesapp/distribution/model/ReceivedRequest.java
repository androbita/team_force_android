package com.app.salesapp.distribution.model;

import com.google.gson.annotations.SerializedName;

public class ReceivedRequest {
    @SerializedName("token")
    public String token;
    @SerializedName("program_id")
    public String programId;

    public ReceivedRequest(String token, String programId) {
        this.token = token;
        this.programId = programId;
    }
}
