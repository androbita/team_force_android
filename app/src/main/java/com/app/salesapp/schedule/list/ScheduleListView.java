package com.app.salesapp.schedule.list;

import com.app.salesapp.schedule.model.ScheduleResponse;

import java.util.List;

public interface ScheduleListView {

    void setSwipeRefresh(boolean refresh);

    void onScheduleListSuccess(List<ScheduleResponse.ScheduleList> data);

    void onScheduleListLoadMoreSuccess(List<ScheduleResponse.ScheduleList> data);

    void isAuthFailure();

    void showSnakeBar(String errorMessage);

    void showToastMessage(String appErrorMessage);
}
