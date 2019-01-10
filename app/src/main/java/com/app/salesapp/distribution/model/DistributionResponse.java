package com.app.salesapp.distribution.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DistributionResponse {

    @SerializedName("total_page")
    public Integer totalPage;

    @SerializedName("distribution_list")
    public List<DistributionList> distributionLists;

    public class DistributionList {
        @SerializedName("merchandise_distribution_id")
        public String merchantdiseDistributionId;
        @SerializedName("users_organization_id")
        public String usersOrganizationId;
        @SerializedName("channel_name")
        public String channelName;
        @SerializedName("material_name")
        public String materialName;
        @SerializedName("quantity")
        public String quantity;
        @SerializedName("picture_after")
        public String pictureAfter;
        @SerializedName("username")
        public String username;
        @SerializedName("datetime")
        public String dateTime;
    }
}
