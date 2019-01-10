package com.app.salesapp.distribution.model;

import com.google.gson.annotations.SerializedName;

public class DisplayPostRequest {

    @SerializedName("display")
    public int display;
    @SerializedName("active")
    public int active;

    @SerializedName("token")
    public String token;
    @SerializedName("program_id")
    public String programId;
    @SerializedName("users_organization_id")
    public String usersOrganizationId;
    @SerializedName("merchandise_distribution_id")
    public String merchandiseDistributionId;
    @SerializedName("remarks")
    public String remarks;
    @SerializedName("picture")
    public String picture;
}
