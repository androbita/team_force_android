package com.app.salesapp.distribution.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zcky on 4/11/18.
 */

public class MaterialSelected {
    @SerializedName("outbound_id")
    public String outboundId;
    @SerializedName("material_name")
    public String materialName;
    public int input;
    public boolean isSelected;

    public MaterialSelected(){}

    public MaterialSelected(String outboundId, String materialName, boolean isSelected, int input) {
       this.outboundId = outboundId;
       this.materialName = materialName;
       this.isSelected = isSelected;
       this.input = input;
    }
}
