package com.app.salesapp.training.list;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.login.LoginResponseModel;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.training.model.TrainingRequest;
import com.app.salesapp.training.model.TrainingResponse;
import com.app.salesapp.user.UserService;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.app.salesapp.common.SalesAppConstant.FIRST_PAGE;

public class TrainingListPresenter {

    private final TrainingListView view;
    private final SalesAppService salesAppService;
    private final UserService userService;
    private final TrainingListViewModel viewModel;
    private int maxPage = 1;
    private CompositeSubscription subscriptions;

    public TrainingListPresenter(TrainingListView view, SalesAppService salesAppService,
                                 UserService userService, TrainingListViewModel viewModel) {
        this.view = view;
        this.salesAppService = salesAppService;
        this.userService = userService;
        this.viewModel = viewModel;

        subscriptions = new CompositeSubscription();
    }

    public void getTraining(final int page) {
        if (!userService.isLoggedIn()) {
            userService.clearData();
            view.isAuthFailure();
            return;
        }

        if (page > maxPage) {
            view.setSwipeRefresh(false);
            viewModel.setLoading(false);
            return;
        }

        LoginResponseModel loginResponseModel = userService.getUserPreference();
        TrainingRequest trainingRequest = new TrainingRequest(loginResponseModel.token,
                userService.getCurrentProgram(), page);

        if (page == FIRST_PAGE) {
            view.setSwipeRefresh(true);
        }

        viewModel.setLoading(true);

        Subscription subscription = salesAppService.getTraining(trainingRequest,
                new SalesAppService.ServiceCallback<Response<TrainingResponse>>() {
                    @Override
                    public void onSuccess(Response<TrainingResponse> response) {
                        maxPage = response.data.totalPage;
                        viewModel.setLoading(false);
                        view.setSwipeRefresh(false);

                        if (response.isSuccess()) {
                            List<TrainingResponse.TrainingList> data = response.data.trainingLists;

                            if (page > FIRST_PAGE) {
                                view.onTrainingListLoadMoreSuccess(data);
                            } else {
                                view.onTrainingListSuccess(data);
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

    public void onDestroy() {
        subscriptions.clear();
    }
}
