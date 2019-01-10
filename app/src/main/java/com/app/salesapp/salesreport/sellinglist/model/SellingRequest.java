package com.app.salesapp.salesreport.sellinglist.model;

import com.google.gson.annotations.SerializedName;

public class SellingRequest {

    @SerializedName("page")
    public int page;

    @SerializedName("token")
    public String token;
    @SerializedName("program_id")
    public String programId;

    public SellingRequest(String token, String programId, int page) {
        this.token = token;
        this.programId = programId;
        this.page = page;
    }
}
