package com.app.salesapp.chart;

import com.google.gson.annotations.SerializedName;

public class UserResponseModel {

    @SerializedName("users_id")
    public String userId;

    @SerializedName("name")
    public String name;

    @Override
    public String toString() {
        return name;
    }
}
