package com.app.salesapp.distribution.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReceivedResponse {
    @SerializedName("list_received")
    public List<ReceivedResponse.ReceivedList> receivedLists;

    public class ReceivedList {
        @SerializedName("outbound_id")
        public String outboundId;
        @SerializedName("material_name")
        public String materialName;
    }
}
