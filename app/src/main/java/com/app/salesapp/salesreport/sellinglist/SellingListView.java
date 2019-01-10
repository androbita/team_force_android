package com.app.salesapp.salesreport.sellinglist;

import com.app.salesapp.salesreport.sellinglist.model.SellingResponse;

import java.util.List;

public interface SellingListView {

    void setSwipeRefresh(boolean refresh);

    void onSellingListSuccess(List<SellingResponse.SellingList> data);

    void onSellingListLoadMoreSuccess(List<SellingResponse.SellingList> data);

    void isAuthFailure();

    void showSnakeBar(String errorMessage);

    void showToastMessage(String appErrorMessage);
}
