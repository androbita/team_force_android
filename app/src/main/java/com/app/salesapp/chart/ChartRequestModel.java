package com.app.salesapp.chart;

import com.google.gson.annotations.SerializedName;

public class ChartRequestModel {

    @SerializedName("token")
    public String token;

    @SerializedName("program_id")
    public String programId;

    @SerializedName("user_select")
    public String userSelect;

    @SerializedName("view_by")
    public String viewBy;

    public ChartRequestModel() {

    }

    public ChartRequestModel(String token, String programId, String userSelect, String viewBy) {
        this.token = token;
        this.programId = programId;
        this.userSelect = userSelect;
        this.viewBy = viewBy;
    }
}
