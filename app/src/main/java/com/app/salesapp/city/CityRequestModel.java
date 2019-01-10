package com.app.salesapp.city;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zcky on 3/11/18.
 */

public class CityRequestModel {

    @SerializedName("token")
    public String token;
    @SerializedName("program_id")
    public String programId;

    public CityRequestModel(String token, String programId){
        this.token = token;
        this.programId = programId;
    }


}
