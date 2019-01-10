package com.app.salesapp.timeline.model;

import com.app.salesapp.timeline.comment.CommentResponse;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimelineResponse {

    @SerializedName("total_page")
    public Integer totalPage;

    @SerializedName("timeline_list")
    public List<TimelineList> timelineList;

    public List<SearchByList> search_by_list;
    public class SearchByList
    {
        public String key;
        public String name;
    }
    public class TimelineList {
        @SerializedName("timeline_id")
        public String timelineId;
        @SerializedName("username")
        public String username;
        @SerializedName("type")
        public String type;
        @SerializedName("location")
        public String location;
        @SerializedName("datetime")
        public String datetime;
        @SerializedName("city")
        public String city;
        @SerializedName("remark")
        public String remark;
        @SerializedName("photo_profile")
        public String photoProfile;
        @SerializedName("photo")
        public String photo;
        @SerializedName("total_comment")
        public String totalComment;
        @SerializedName("comment")
        public List<CommentResponse> comments;
    }
}
