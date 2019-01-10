package com.app.salesapp.distribution.distributionlist.adapter;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.app.salesapp.R;
import com.app.salesapp.distribution.model.DistributionResponse;
import com.app.salesapp.util.Util;

public class DistributionListItemViewModel extends BaseObservable {

    private static final String TIME_PATTERN_TIMELINE = "MMM d, HH:mm";
    private final DistributionResponse.DistributionList distributionList;

    private Context context;

    public DistributionListItemViewModel(Context context, DistributionResponse.DistributionList distributionList) {
        this.context = context;
        this.distributionList = distributionList;
    }

    @Bindable
    public String getProductName() {
        String productName = TextUtils.isEmpty(distributionList.materialName) ? "" : distributionList.materialName + " - ";
        String channelName = TextUtils.isEmpty(distributionList.channelName) ? "" : distributionList.channelName;
        return productName + channelName;
    }

    @Bindable
    public String getUserName() {
        String name = TextUtils.isEmpty(distributionList.username) ? "" : distributionList.username;
        return name;
    }

    @Bindable
    public String getChannel() {
        String channelName = TextUtils.isEmpty(distributionList.channelName) ? "" : distributionList.channelName +" - ";
        String productName = TextUtils.isEmpty(distributionList.materialName) ? "" : distributionList.materialName + " - ";
        String quantity = TextUtils.isEmpty(distributionList.quantity) ? "" : distributionList.quantity +" pcs";
        return channelName+ productName + quantity;
    }

    @Bindable
    public String getDate() {
        String date = TextUtils.isEmpty(distributionList.dateTime) ? "" : distributionList.dateTime;
        return date;
    }

    @Bindable
    public String getQuantity() {
        return distributionList.quantity + "pcs";
    }

}
