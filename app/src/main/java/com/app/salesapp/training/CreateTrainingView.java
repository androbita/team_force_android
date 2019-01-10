package com.app.salesapp.training;

import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;

import java.util.List;

public interface CreateTrainingView {
    void showLoading(boolean show);

    void showListModule(List<ModuleModel> response);

    void showErrorMessage(String appErrorMessage);

    void loadChannelSpinner(List<ListChannelResponseModel.ChannelList> data);

    void onSuccessPostTraining();

    void onSuccessPostSales(AudienceModel model);

    void showListSales(List<SalesModel> data);
}
