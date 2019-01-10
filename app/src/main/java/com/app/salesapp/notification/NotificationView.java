package com.app.salesapp.notification;

import com.app.salesapp.attendance.ListAttendanceResponseModel;
import com.app.salesapp.fcm.NotificationModel;
import com.app.salesapp.network.Response;

import java.util.ArrayList;

public interface NotificationView {

    void showListNotification(ArrayList<NotificationModel> data,boolean loadMore);

    void showToastError(String msg);

    void showLoading(boolean b);

    void showDialogSuccess(String message);
}
