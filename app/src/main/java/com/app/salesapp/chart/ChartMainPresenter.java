package com.app.salesapp.chart;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.ResponseArray;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class ChartMainPresenter implements ChartMainContract.Presenter {

    private ChartMainContract.View view;
    private SalesAppService salesAppService;
    private UserService userService;
    private CompositeSubscription subscriptions;

    public ChartMainPresenter(ChartMainContract.View view, SalesAppService salesAppService, UserService userService) {
        this.view = view;
        this.salesAppService = salesAppService;
        this.userService = userService;

        subscriptions = new CompositeSubscription();
    }

    @Override
    public void getUsers() {
        view.showLoading();

        String token = userService.getAccessToken();
        String programId = userService.getCurrentProgram();

        UserRequestModel request = new UserRequestModel(token, programId);

        Subscription subscription = salesAppService.postListUsers(request, new SalesAppService.ServiceCallback<ResponseArray<UserResponseModel>>() {
            @Override
            public void onSuccess(ResponseArray<UserResponseModel> response) {
                view.showUsers(response.data);
            }

            @Override
            public void onError(NetworkError error) {
                view.showUsers(new ArrayList<UserResponseModel>());
            }
        });

        subscriptions.add(subscription);
    }

    @Override
    public void getCurrentStatus() {
        view.showLoading();

        String token = userService.getAccessToken();
        String programId = userService.getCurrentProgram();

        StatusRequestModel request = new StatusRequestModel(token, programId);

        Subscription subscription = salesAppService.postCurrentActivity(request, new SalesAppService.ServiceCallback<ResponseArray<StatusResponseModel>>() {
            @Override
            public void onSuccess(ResponseArray<StatusResponseModel> response) {
                view.hideLoading();
                view.showCurrentStatus(response.data);
            }

            @Override
            public void onError(NetworkError error) {
                view.hideLoading();
                view.showEmptyStatus();
            }
        });

        subscriptions.add(subscription);
    }

    public void onDestroy() {
        subscriptions.unsubscribe();
    }
}
