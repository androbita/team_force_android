package com.app.salesapp.distribution.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by soeltanzaki_r on 4/2/18.
 */

public class DistributionListRequest {
    @SerializedName("token")
    public String token;
    @SerializedName("program_id")
    public String programId;
    @SerializedName("users_organization_id")
    public String usersId;

    public DistributionListRequest(String token, String programId, String id) {
        this.token = token;
        this.programId = programId;
        this.usersId = id;
    }
}
