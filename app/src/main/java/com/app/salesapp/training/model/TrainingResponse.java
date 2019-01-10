package com.app.salesapp.training.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainingResponse {

    @SerializedName("total_page")
    public Integer totalPage;

    @SerializedName("training_list")
    public List<TrainingList> trainingLists;

    public class TrainingList {
        @SerializedName("training_detail_id")
        public String trainingDetailId;
        @SerializedName("channel_name")
        public String channelName;
        @SerializedName("mobile_phone")
        public String mobilePhone;
        @SerializedName("module_name")
        public String moduleName;
        @SerializedName("training_date")
        public String trainngDate;
        @SerializedName("sales_name")
        public String salesName;
    }
}
