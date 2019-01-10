package com.app.salesapp.Gps;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soeltanzaki_r on 2/15/18.
 */

public class ConfigResponseModel {

    public class TrackingConfig {
        @SerializedName("time_start")
        public String timeStart;
        @SerializedName("time_stop")
        public String timeStop;
        @SerializedName("frequency")
        public String frequency;
        @SerializedName("include_saturday")
        public String includeSaturday;
        @SerializedName("include_sunday")
        public String includeSunday;
    }

    public ArrayList<String> menus;

    public List<TrackingConfig> tracking_config;
}
