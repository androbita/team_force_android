package com.app.salesapp.channel.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zcky on 3/11/18.
 */

public class PostChannelRequestModel {
    @SerializedName("name")
    public String name;
    @SerializedName("address")
    public String address;
    @SerializedName("token")
    public String token;
    @SerializedName("program_id")
    public String programId;
    @SerializedName("city")
    public String city;
    @SerializedName("picture")
    public String picture;
    @SerializedName("latitude")
    public double latitude;
    @SerializedName("longitude")
    public double longitude;

    public PostChannelRequestModel(String token, String programId, String name, String address, String city,double latitude, double longitude) {
        this.token = token;
        this.programId = programId;
        this.name = name;
        this.address = address;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
