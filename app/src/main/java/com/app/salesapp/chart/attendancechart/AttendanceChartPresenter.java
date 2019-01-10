package com.app.salesapp.chart.attendancechart;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.chart.ChartRequestModel;
import com.app.salesapp.chart.ChartResponseModel;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.user.UserService;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class AttendanceChartPresenter implements AttendanceChartContract.Presenter {

    private AttendanceChartContract.View view;
    private SalesAppService salesAppService;
    private UserService userService;
    private CompositeSubscription subscriptions;

    public AttendanceChartPresenter(AttendanceChartContract.View view, SalesAppService salesAppService, UserService userService) {
        this.view = view;
        this.salesAppService = salesAppService;
        this.userService = userService;

        subscriptions = new CompositeSubscription();
    }

    @Override
    public void getAttendanceChart(String viewBy) {
        view.showLoading();

        String token = userService.getAccessToken();
        String programId = userService.getCurrentProgram();
        String userSelect = view.getActiveUserId();

        ChartRequestModel request = new ChartRequestModel(token, programId, userSelect, viewBy);

        Subscription subscription = salesAppService.postAttendanceChart(request, new SalesAppService.ServiceCallback<Response<ChartResponseModel>>() {
            @Override
            public void onSuccess(Response<ChartResponseModel> response) {
                view.hideLoading();

                if (response.isSuccess()) {
                    view.showAttendanceChart(response.data);
                } else {
                    view.showError(response.message);
                }
            }

            @Override
            public void onError(NetworkError error) {
                view.hideLoading();
                view.showError(error.getAppErrorMessage());
            }
        });

        subscriptions.add(subscription);
    }

    @Override
    public void onDestroy() {
        subscriptions.clear();
    }
}
