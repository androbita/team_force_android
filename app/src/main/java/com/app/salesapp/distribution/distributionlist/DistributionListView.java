package com.app.salesapp.distribution.distributionlist;

import com.app.salesapp.distribution.model.DistributionResponse;

import java.util.List;

public interface DistributionListView {

    void setSwipeRefresh(boolean refresh);

    void onDistributionListSuccess(List<DistributionResponse.DistributionList> data);

    void onDistributionListLoadMoreSuccess(List<DistributionResponse.DistributionList> data);

    void isAuthFailure();

    void showSnakeBar(String errorMessage);

    void showToastMessage(String appErrorMessage);
}
