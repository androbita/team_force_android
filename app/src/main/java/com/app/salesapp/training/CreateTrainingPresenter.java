package com.app.salesapp.training;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.attendance.createattendance.ListChannelRequestModel;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.user.UserService;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class CreateTrainingPresenter {

    CompositeSubscription subscription;
    UserService userService;
    CreateTrainingView view;
    SalesAppService salesAppService;

    public CreateTrainingPresenter(UserService userService, CreateTrainingView view, SalesAppService salesAppService) {
        this.subscription = new CompositeSubscription();
        this.userService = userService;
        this.view = view;
        this.salesAppService = salesAppService;
    }

    public void getModuleList() {
        Subscription subscriptions = salesAppService.getModule(new ListChannelRequestModel(userService.getAccessToken(), userService.getCurrentProgram()),
                new SalesAppService.ServiceCallback<Response<List<ModuleModel>>>() {
                    @Override
                    public void onSuccess(Response<List<ModuleModel>> response) {
                        if (response.isSuccess()) {
                            view.showListModule(response.data);
                        } else {
                            view.showErrorMessage(response.message);
                        }
                    }

                    @Override
                    public void onError(NetworkError error) {
                        view.showErrorMessage(error.getAppErrorMessage());
                    }
                });

        subscription.add(subscriptions);
    }

    public void getSalesList(String users_organization_id) {
        view.showLoading(true);
        Subscription subscriptions = salesAppService.getSales(new ListSalesRequest(userService.getAccessToken(), userService.getCurrentProgram(),users_organization_id),
                new SalesAppService.ServiceCallback<Response<List<SalesModel>>>() {
                    @Override
                    public void onSuccess(Response<List<SalesModel>> response) {
                        view.showLoading(false);
                        if (response.isSuccess()) {
                            view.showListSales(response.data);
                        } else {
                            view.showErrorMessage(response.message);
                        }
                    }

                    @Override
                    public void onError(NetworkError error) {
                        view.showLoading(false);
                        view.showErrorMessage(error.getAppErrorMessage());
                    }
                });

        subscription.add(subscriptions);
    }
    public void onDestroy() {
        subscription.clear();
        subscription.unsubscribe();
    }

    public void getListChannel() {
        view.loadChannelSpinner(userService.getListChannel());
    }

    public void postTraining(PostTrainingRequest postTrainingRequest) {
        view.showLoading(true);
        Subscription subscribe = salesAppService.postTraining(postTrainingRequest, new SalesAppService.ServiceCallback<Response<String>>() {
            @Override
            public void onSuccess(Response<String> response) {
                view.showLoading(false);
                if (response.isSuccess())
                    view.onSuccessPostTraining();
                else
                    view.showErrorMessage(response.message);
            }

            @Override
            public void onError(NetworkError error) {
                view.showLoading(false);
                view.showErrorMessage(error.getAppErrorMessage());
            }
        });

        subscription.add(subscribe);
    }

    public void postAudience(final AudienceModel model) {
        view.showLoading(true);
        Subscription subscribe = salesAppService.postSales(model, new SalesAppService.ServiceCallback<Response<String>>() {
            @Override
            public void onSuccess(Response<String> response) {
                view.showLoading(false);
                if (response.isSuccess())
                    view.onSuccessPostSales(model);
                else
                    view.showErrorMessage(response.message);
            }

            @Override
            public void onError(NetworkError error) {
                view.showLoading(false);
                view.showErrorMessage(error.getAppErrorMessage());
            }
        });

        subscription.add(subscribe);
    }
}
