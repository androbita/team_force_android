package com.app.salesapp.timeline;

import com.app.salesapp.timeline.model.TimelineResponse;

import java.util.List;

public interface TimelineView {

    void setSwipeRefresh(boolean refresh);

    void onTimelineSuccess(List<TimelineResponse.TimelineList> data, List<TimelineResponse.SearchByList> search_by_list);

    void onTimelineLoadMoreSuccess(List<TimelineResponse.TimelineList> data);

    void isAuthFailure();

    void openWriteActivity();

    void openCheckInPage();

    void openSellingPage();

    void openDistributionPage();

    void openTrainingPage();

    void postCommentSuccess();

    void showSnakeBar(String errorMessage);

    void showToastMessage(String appErrorMessage);
}
