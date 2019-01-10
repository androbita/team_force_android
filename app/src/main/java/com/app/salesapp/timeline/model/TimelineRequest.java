package com.app.salesapp.timeline.model;

import com.google.gson.annotations.SerializedName;

public class TimelineRequest {

    @SerializedName("page")
    public int page;

    @SerializedName("token")
    public String token;
    @SerializedName("program_id")
    public String programId;

    @SerializedName("search_by")
    public String searchBy;
    @SerializedName("search_value")
    public String searchValue;

    public TimelineRequest(String token, String programId, int page) {
        this.token = token;
        this.programId = programId;
        this.page = page;
    }

    public TimelineRequest(int page, String token, String programId, String searchBy, String searchValue) {
        this.page = page;
        this.token = token;
        this.programId = programId;
        this.searchBy = searchBy;
        this.searchValue = searchValue;
    }
}
