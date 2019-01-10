package com.app.salesapp.posttracking;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zcky on 2/18/18.
 */

public class PostTrackingRequestModel {
    public String token;
    @SerializedName("program_id")
    public String programId;
    public Double latitude;
    public Double longitude;
    public String datetime;
    public String location;

    public PostTrackingRequestModel(String token, String programId, Double latitude, Double longitude, String datetime, String location){
        this.token = token;
        this.programId = programId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.datetime = datetime;
        this.location = location;
    }

}
