package com.app.salesapp.training.list.adapter;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.app.salesapp.training.model.TrainingResponse;

public class TrainingListItemViewModel extends BaseObservable {

    private static final String TIME_PATTERN_TIMELINE = "MMM d, HH:mm";
    private final TrainingResponse.TrainingList trainingList;

    private Context context;

    public TrainingListItemViewModel(Context context, TrainingResponse.TrainingList trainingList) {
        this.context = context;
        this.trainingList = trainingList;
    }

    @Bindable
    public String getChannelName() {
        String channelName = TextUtils.isEmpty(trainingList.channelName) ? "" : trainingList.channelName;
        return channelName;
    }

    @Bindable
    public String getTrainingDate() {
        String date = TextUtils.isEmpty(trainingList.trainngDate) ? "" : trainingList.trainngDate;
        return date;
    }

    @Bindable
    public String getMobileNumber() {
        String phone = TextUtils.isEmpty(trainingList.mobilePhone) ? "" : trainingList.mobilePhone ;
        return phone;
    }

    @Bindable
    public String getSalesAndMobileNumber() {
        String name = TextUtils.isEmpty(trainingList.salesName) ? "" : trainingList.salesName ;
        String phone = TextUtils.isEmpty(trainingList.mobilePhone) ? "" : trainingList.mobilePhone ;
        return name +" - "+ phone;
    }

    @Bindable
    public String getModule() {
        String module = TextUtils.isEmpty(trainingList.moduleName) ? "" : trainingList.moduleName ;
        return module;
    }

    @Bindable
    public String getDesc() {
        String productName = TextUtils.isEmpty(trainingList.channelName) ? "" : trainingList.channelName + " - ";
        String channelName = TextUtils.isEmpty(trainingList.moduleName) ? "" : trainingList.moduleName;
        return productName + channelName;
    }
}
