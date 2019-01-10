package com.app.salesapp.notification;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.fcm.NotificationModel;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.user.UserService;
import com.app.salesapp.util.Util;

import java.util.ArrayList;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class NotificationPresenter {

    private final SalesAppService salesAppService;
    private final NotificationView notificationView;
    private final UserService userService;
    private final CompositeSubscription compositeSubscription;

    public NotificationPresenter(NotificationView notificationView, SalesAppService salesAppService, UserService userService) {
        this.notificationView = notificationView;
        this.userService = userService;
        this.salesAppService = salesAppService;
        this.compositeSubscription = new CompositeSubscription();
    }

    public void onDestroy() {
    }

    public void getListNotification(int currentPage) {
        if(!userService.getNotifications().isEmpty()){
            notificationView.showListNotification(userService.getNotifications(),false);
        }else
        {
            NotificationRequest notificationRequest = new NotificationRequest(userService.getAccessToken(),
                    userService.getCurrentProgram(),String.valueOf(currentPage));
            Subscription subscription = salesAppService.getNotificationList(notificationRequest, new SalesAppService.ServiceCallback<Response<NotificationResponse>>() {
                @Override
                public void onSuccess(Response<NotificationResponse> response) {
                    notificationView.showLoading(false);
                    if(response.isSuccess()) {
                        ArrayList<NotificationModel> data = new ArrayList<NotificationModel>();
                        for(NotificationResponse.ListNotif temp : response.data.notification_list){
                            try {
                                data.add(new NotificationModel(Util.formatDateFromAPI(temp.created_date),temp.description,true,"COMMENT",temp.timeline_id));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        notificationView.showListNotification(data,false);

                    }
                }

                @Override
                public void onError(NetworkError error) {
                    notificationView.showLoading(false);
                    error.printStackTrace();
                }
            });
            compositeSubscription.add(subscription);
        }
    }

    public void postAnnouncement(String announcement) {
        if(announcement.equals(""))
            notificationView.showToastError("Pesan harus diisi !");
        else{
            PostAnnouncementRequest postAnnouncementRequest = new PostAnnouncementRequest(userService.getAccessToken(),userService.getCurrentProgram(),announcement);
            Subscription subscribe = salesAppService.postAnnouncement(postAnnouncementRequest, new SalesAppService.ServiceCallback<Response<String>>() {

                @Override
                public void onSuccess(Response<String> response) {
                    notificationView.showLoading(false);

                    if (response.isSuccess())
                        notificationView.showDialogSuccess(response.message);
                    else
                        notificationView.showToastError(response.message);
                }

                @Override
                public void onError(NetworkError error) {
                    notificationView.showLoading(false);
                    error.printStackTrace();
                    notificationView.showToastError(error.getAppErrorMessage());
                }
            });
            compositeSubscription.add(subscribe);
        }
    }
}
