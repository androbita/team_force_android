package com.app.salesapp.notification;

import android.text.TextUtils;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.fcm.NotificationModel;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.timeline.comment.PostCommentRequest;
import com.app.salesapp.user.UserService;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class DetailCommentPresenter {

    private final DetailCommentView view;
    private final UserService userService;
    CompositeSubscription subscriptions;
    SalesAppService salesAppService;

    public DetailCommentPresenter(DetailCommentView view, SalesAppService salesAppService, UserService userService) {
        this.view = view;
        subscriptions = new CompositeSubscription();
        this.userService = userService;
        this.salesAppService = salesAppService;
    }


    public void getDetailTimeline(NotificationModel notificationModel) {
        view.setLoading(true);
        DetailTimelineRequest detailTimelineRequest =  new DetailTimelineRequest(userService.getAccessToken(),Integer.parseInt(notificationModel.requestId));
        Subscription subscription = salesAppService.getDetailTimeline(detailTimelineRequest, new SalesAppService.ServiceCallback<Response<DetailTimelineModel>>() {
            @Override
            public void onSuccess(Response<DetailTimelineModel> response) {
                view.setLoading(false);
                view.showDetailComment(response.data);
            }

            @Override
            public void onError(NetworkError error) {
                view.setLoading(false);
                error.printStackTrace();
            }
        });
        subscriptions.add(subscription);
    }


    public void destroy() {
        subscriptions.clear();
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

        view.setLoading(true);
        Subscription subscription = salesAppService.postComment(postCommentRequest,
                new SalesAppService.ServiceCallback<Response<String>>() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        view.setLoading(false);
                        view.showSnakeBar(response.message);

                        if (response.isSuccess()) {
                            view.postCommentSuccess();
                        } else {
                            view.showToastMessage(response.message);
                        }
                    }

                    @Override
                    public void onError(NetworkError error) {
                        view.setLoading(false);

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
}
