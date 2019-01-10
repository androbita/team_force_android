package com.app.salesapp.distribution.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zcky on 4/11/18.
 */

public class MaterialRequestModel {
    @SerializedName("outbound_id")
    public String outboundId;
    @SerializedName("quantity")
    public int quantity;
    @SerializedName("display")
    public int display;
    @SerializedName("remarks")
    public String remarks;

    public MaterialRequestModel (String outbondId, int quantity, int display, String remark){
     this.outboundId = outbondId;
     this.quantity = quantity;
     this.display = display;
     this.remarks = remark;
    }

}
