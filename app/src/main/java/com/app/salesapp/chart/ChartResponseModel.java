package com.app.salesapp.chart;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChartResponseModel {

    @SerializedName("title")
    public String title;

    @SerializedName("label")
    public List<String> labels;

    @SerializedName("value")
    public List<Integer> values;
}
