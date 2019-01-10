package com.app.salesapp.notification;

public interface DetailCommentView {
    void showDetailComment(DetailTimelineModel response);

    void showToastMessage(String s);

    void setLoading(boolean b);

    void showSnakeBar(String message);

    void postCommentSuccess();

    void isAuthFailure();
}
