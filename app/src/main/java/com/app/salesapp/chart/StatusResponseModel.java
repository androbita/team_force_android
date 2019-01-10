package com.app.salesapp.chart;

import com.google.gson.annotations.SerializedName;

public class StatusResponseModel {

    @SerializedName("your_status")
    public ActivityStatusModel activityStatus;

    @SerializedName("channel_status")
    public ChannelStatusModel channelStatus;

    public class ActivityStatusModel {

        @SerializedName("last_channel")
        public String lastChannel;

        @SerializedName("activity")
        public String activity;

        @SerializedName("time")
        public String time;
    }

    public class ChannelStatusModel {

        @SerializedName("distribution")
        public int distribution;

        @SerializedName("training")
        public int training;

        @SerializedName("selling")
        public int selling;
    }
}
