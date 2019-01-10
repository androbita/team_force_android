package com.app.salesapp.training.list;

import com.app.salesapp.training.model.TrainingResponse;

import java.util.List;

public interface TrainingListView {

    void setSwipeRefresh(boolean refresh);

    void onTrainingListSuccess(List<TrainingResponse.TrainingList> data);

    void onTrainingListLoadMoreSuccess(List<TrainingResponse.TrainingList> data);

    void isAuthFailure();

    void showSnakeBar(String errorMessage);

    void showToastMessage(String appErrorMessage);
}
