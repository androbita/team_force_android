package com.app.salesapp.distribution.createdistribution;

import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.distribution.model.DistributionPostRequest;
import com.app.salesapp.distribution.model.ReceivedResponse;

import java.util.List;

public interface CreateDistributionView {

    void loadSpinnerData(List<ListChannelResponseModel.ChannelList> data);

    void loadSpinnerMaterialData(List<ReceivedResponse.ReceivedList> data);

    void showMessage(String message);

    void finishActivity();

    void onErrorPost(String appErrorMessage, DistributionPostRequest distributionPostRequest);
}
