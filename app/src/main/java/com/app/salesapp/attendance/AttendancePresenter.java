package com.app.salesapp.attendance;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class AttendancePresenter implements AttendanceContract.Presenter {

    int maxPage = 1;

    CompositeSubscription subscriptions;
    AttendanceContract.View mView;
    SalesAppService salesAppService;

    public AttendancePresenter(AttendanceContract.View view, SalesAppService salesAppService) {
        this.mView = view;
        this.salesAppService = salesAppService;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void getListAttendance(String token, String programId, int page) {
        if (page > maxPage) return;

        ListAttendanceRequestModel param = new ListAttendanceRequestModel(token, programId, String.valueOf(page));
        Subscription subscription = salesAppService.getListAttendance(param, new SalesAppService.ServiceCallback<Response<ListAttendanceResponseModel>>() {

            @Override
            public void onSuccess(Response<ListAttendanceResponseModel> response) {
                if (response.isSuccess()) {
                    mView.showListAttendance(response.data);
                    maxPage = response.data.total_page;
                }
            }

            @Override
            public void onError(NetworkError error) {

            }
        });

        subscriptions.add(subscription);
    }

    @Override
    public void onDestroy() {
        subscriptions.clear();
    }
}
