package com.app.salesapp.salesreport.sellinglist.adapter;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.app.salesapp.R;
import com.app.salesapp.salesreport.sellinglist.model.SellingResponse;
import com.app.salesapp.util.Util;

public class SellingListItemViewModel extends BaseObservable {

    private static final String TIME_PATTERN_TIMELINE = "MMM d, HH:mm";
    private final SellingResponse.SellingList sellingList;
    private String userName;

    private Context context;

    public SellingListItemViewModel(Context context, SellingResponse.SellingList sellingList, String userName) {
        this.context = context;
        this.sellingList = sellingList;
        this.userName = userName;
    }

    @Bindable
    public String getChannelName() {
        String chName = TextUtils.isEmpty(sellingList.channelName) ? "" : sellingList.channelName;
        return "Selling @"+chName;
    }

    @Bindable
    public String getUserName() {
        return this.userName;
    }

    @Bindable
    public String getUniqueCodeAndQty() {
        String uniqueCode = sellingList.productName;
        String qty = sellingList.quantity +" Unit ";
        return qty + uniqueCode;
    }

    @Bindable
    public String getSellingDate() {
        return TextUtils.isEmpty(sellingList.sellingDate) ? "" : sellingList.sellingDate;
    }
}
