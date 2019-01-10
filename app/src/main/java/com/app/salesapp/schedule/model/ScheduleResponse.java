package com.app.salesapp.schedule.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScheduleResponse {

    @SerializedName("total_page")
    public Integer totalPage;

    @SerializedName("roadmap_list")
    public List<ScheduleList> scheduleLists;

    public class ScheduleList {
        @SerializedName("road_map_id")
        public String roadMapId;
        @SerializedName("channel_name")
        public String channelName;
        @SerializedName("address")
        public String address;
        @SerializedName("datetime")
        public String dateTime;
    }
}
