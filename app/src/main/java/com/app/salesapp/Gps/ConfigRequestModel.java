package com.app.salesapp.Gps;

import com.google.gson.annotations.SerializedName;

/**
 * Created by soeltanzaki_r on 2/15/18.
 */

public class ConfigRequestModel {
    public String token;
    @SerializedName("program_id")
    public String programId;

    public ConfigRequestModel(String token, String programId) {
        this.token = token;
        this.programId = programId;
    }
}
