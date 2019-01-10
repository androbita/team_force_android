package com.app.salesapp.salesreport.sellinglist.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SellingResponse {

    @SerializedName("total_page")
    public Integer totalPage;

    @SerializedName("selling_list")
    public List<SellingList> sellingList;

    public class SellingList {
        @SerializedName("selling_id")
        public String sellingId;
        @SerializedName("channel_name")
        public String channelName;
        @SerializedName("product_name")
        public String productName;
        @SerializedName("selling_date")
        public String sellingDate;
        @SerializedName("quantity")
        public String quantity;
    }
}
