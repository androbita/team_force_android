package com.app.salesapp.distribution.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DistributionPostResponseModel {

    @SerializedName("list_distribution")
    public List<DistributionPostResponseModel.DistributionList> listDistribution;

    public class DistributionList {
        @SerializedName("merchandise_distribution_id")
        public String merchantdiseDistributionId;
        @SerializedName("material_name")
        public String materialname;

    }
}
