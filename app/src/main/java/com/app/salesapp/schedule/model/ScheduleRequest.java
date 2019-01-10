package com.app.salesapp.schedule.model;

import com.google.gson.annotations.SerializedName;

public class ScheduleRequest {

    @SerializedName("page")
    public int page;

    @SerializedName("token")
    public String token;
    @SerializedName("program_id")
    public String programId;

    public ScheduleRequest(String token, String programId, int page) {
        this.token = token;
        this.programId = programId;
        this.page = page;
    }
}
