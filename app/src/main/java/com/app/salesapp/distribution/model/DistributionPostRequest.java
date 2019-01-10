package com.app.salesapp.distribution.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DistributionPostRequest {

    @SerializedName("token")
    public String token;
    @SerializedName("program_id")
    public String programId;
    @SerializedName("users_organization_id")
    public String usersOrganizationId;
    @SerializedName("item")
    public ArrayList<MaterialRequestModel> item;
    @SerializedName("picture_before")
    public String pictureBefore;
    @SerializedName("picture_after")
    public String pictureAfter;
    @SerializedName("datetime")
    public String datetime;
}
