package com.app.salesapp.chart.channelvisitchart;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.chart.ChartRequestModel;
import com.app.salesapp.chart.ChartResponseModel;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.user.UserService;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class ChannelVisitChartPresenter implements ChannelVisitChartContract.Presenter {

    private ChannelVisitChartContract.View view;
    private SalesAppService salesAppService;
    private UserService userService;
    private CompositeSubscription subscriptions;

    public ChannelVisitChartPresenter(ChannelVisitChartContract.View view, SalesAppService salesAppService, UserService userService) {
        this.view = view;
        this.salesAppService = salesAppService;
        this.userService = userService;

        subscriptions = new CompositeSubscription();
    }

    @Override
    public void getChannelVisitChart(String viewBy) {
        view.showLoading();

        String token = userService.getAccessToken();
        String programId = userService.getCurrentProgram();
        String userSelect = view.getActiveUserId();

        ChartRequestModel request = new ChartRequestModel(token, programId, userSelect, viewBy);

        Subscription subscription = salesAppService.postChannelVisitChart(request, new SalesAppService.ServiceCallback<Response<ChartResponseModel>>() {
            @Override
            public void onSuccess(Response<ChartResponseModel> response) {
                view.hideLoading();

                if (response.isSuccess()) {
                    view.showChannelVisitChart(response.data);
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

    public void onDestroy() {
        subscriptions.unsubscribe();
    }
}
