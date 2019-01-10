package com.app.salesapp.timeline;

import android.text.TextUtils;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.login.LoginResponseModel;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.timeline.comment.PostCommentRequest;
import com.app.salesapp.timeline.model.TimelineRequest;
import com.app.salesapp.timeline.model.TimelineResponse;
import com.app.salesapp.user.UserService;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.app.salesapp.common.SalesAppConstant.FIRST_PAGE;

public class TimelinePresenter {

    private int maxPage = 1;

    private final TimelineView view;
    private final SalesAppService salesAppService;
    private final UserService userService;
    private final TimelineViewModel viewModel;

    private CompositeSubscription subscriptions;

    public TimelinePresenter(TimelineView view, SalesAppService salesAppService, UserService userService,
                             TimelineViewModel viewModel) {
        this.view = view;
        this.salesAppService = salesAppService;
        this.userService = userService;
        this.viewModel = viewModel;

        subscriptions = new CompositeSubscription();
    }

    public void getTimeline(final int page, String query, String key) {
        if (!userService.isLoggedIn()) {
            userService.clearData();
            view.isAuthFailure();
            return;
        }

        if (page > (maxPage==0?1:maxPage)) {
            view.setSwipeRefresh(false);
            viewModel.setLoading(false);
            return;
        }

        LoginResponseModel loginResponseModel = userService.getUserPreference();
        TimelineRequest timelineRequest;
        if (key.equals("") || query.equals("")) {
            timelineRequest = new TimelineRequest(loginResponseModel.token,
                    userService.getCurrentProgram(), page);
        } else {
            timelineRequest = new TimelineRequest(page, loginResponseModel.token,
                    userService.getCurrentProgram(), key, query);
        }

        if (page == FIRST_PAGE) {
            view.setSwipeRefresh(true);
        }

        viewModel.setLoading(true);

        Subscription subscription = salesAppService.getTimeline(timelineRequest,
                new SalesAppService.ServiceCallback<Response<TimelineResponse>>() {
                    @Override
                    public void onSuccess(Response<TimelineResponse> response) {
                        maxPage = response.data.totalPage;
                        viewModel.setLoading(false);
                        view.setSwipeRefresh(false);

                        if (response.isSuccess()) {
                            List<TimelineResponse.TimelineList> data = response.data.timelineList;

                            if (page > FIRST_PAGE) {
                                view.onTimelineLoadMoreSuccess(data);
                            } else {
                                view.onTimelineSuccess(data, response.data.search_by_list);
                            }
                        } else {
                            view.showSnakeBar(response.message);
                        }
                    }

                    @Override
                    public void onError(NetworkError error) {
                        viewModel.setLoading(false);
                        view.setSwipeRefresh(false);

                        if (error.isAuthFailure()) {
                            userService.clearData();
                            view.isAuthFailure();
                        } else {
                            view.showSnakeBar(error.getAppErrorMessage());
                        }
                    }
                });
        subscriptions.add(subscription);
    }

    public void goToWriteActivity() {
        view.openWriteActivity();
    }

    public void goToCheckInPage() {
        view.openCheckInPage();
    }

    public void goToSellingPage() {
        view.openSellingPage();
    }

    public void goToDistributionPage() {
        view.openDistributionPage();
    }

    public void goToTrainingPage() {
        view.openTrainingPage();
    }

    public void postComment(String timelineId, String commentText) {
        if (TextUtils.isEmpty(commentText)) {
            view.showToastMessage("Please, write your comment");
            return;
        }

        if (TextUtils.isEmpty(timelineId)) {
            view.showToastMessage("Can't add comment");
            return;
        }

        PostCommentRequest postCommentRequest = new PostCommentRequest(userService.getUserPreference().token,
                userService.getCurrentProgram(), timelineId, commentText);

        viewModel.setLoading(true);
        Subscription subscription = salesAppService.postComment(postCommentRequest,
                new SalesAppService.ServiceCallback<Response<String>>() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        viewModel.setLoading(false);
                        view.showToastMessage(response.message);

                        if (response.isSuccess()) {
                            view.postCommentSuccess();
                        } else {
                            view.showToastMessage(response.message);
                        }
                    }

                    @Override
                    public void onError(NetworkError error) {
                        viewModel.setLoading(false);

                        if (error.isAuthFailure()) {
                            userService.clearData();
                            view.isAuthFailure();
                        } else {
                            view.showToastMessage(error.getAppErrorMessage());
                        }
                    }
                });
        subscriptions.add(subscription);
    }

    public void onDestroy() {
        subscriptions.clear();
    }
}
