package com.app.salesapp.schedule.list.adapter;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.app.salesapp.schedule.model.ScheduleResponse;
import com.app.salesapp.util.Util;

public class ScheduleListItemViewModel extends BaseObservable {

    private static final String TIME_PATTERN_TIMELINE = "MMM d, HH:mm";
    private final ScheduleResponse.ScheduleList ScheduleList;

    private Context context;

    public ScheduleListItemViewModel(Context context, ScheduleResponse.ScheduleList ScheduleList) {
        this.context = context;
        this.ScheduleList = ScheduleList;
    }

    @Bindable
    public String getChannelName() {
        String channelName = TextUtils.isEmpty(ScheduleList.channelName) ? "" : ScheduleList.channelName + " - ";
        return channelName;
    }

    @Bindable
    public String getAddressName() {
        String address = TextUtils.isEmpty(ScheduleList.address) ? "" : ScheduleList.address;
        return address;
    }

    @Bindable
    public String getDateTime() {
        return Util.formatDateFromAPI(ScheduleList.dateTime, TIME_PATTERN_TIMELINE);
    }
}
