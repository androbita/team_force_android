package com.app.salesapp.salesreport;

import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.network.Response;

import java.util.List;

public interface CreateSalesReportView {
    void showLoading(boolean show);

    void loadChannelSpinner(List<ListChannelResponseModel.ChannelList> data);

    void loadProductSpinner(List<ProductModel> data);

    void loadSellingTypeSpinner(List<SellingTypeModel> data);

    void onErrorPostSelling(String message, SellingReportRequestModel sellingReportRequestModel);

    void onSuccessPostSelling(Response<String> response);
}
