package com.app.salesapp.search.model;

import com.app.salesapp.schedule.model.ScheduleResponse;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zcky on 3/11/18.
 */

public class SearchingRequestResponseModel {
        @SerializedName("lat")
        public double latitude;
        @SerializedName("lng")
        public double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
