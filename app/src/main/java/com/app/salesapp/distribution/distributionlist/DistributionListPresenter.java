package com.app.salesapp.distribution.distributionlist;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.distribution.model.DistributionRequest;
import com.app.salesapp.distribution.model.DistributionResponse;
import com.app.salesapp.login.LoginResponseModel;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.user.UserService;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.app.salesapp.common.SalesAppConstant.FIRST_PAGE;

public class DistributionListPresenter {

    private int maxPage = 1;

    private final DistributionListView view;
    private final SalesAppService salesAppService;
    private final UserService userService;
    private final DistributionListViewModel viewModel;

    private CompositeSubscription subscriptions;

    public DistributionListPresenter(DistributionListView view, SalesAppService salesAppService,
                                     UserService userService, DistributionListViewModel viewModel) {
        this.view = view;
        this.salesAppService = salesAppService;
        this.userService = userService;
        this.viewModel = viewModel;

        subscriptions = new CompositeSubscription();
    }

    public void getDistribution(final int page) {
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
        DistributionRequest distributionRequest = new DistributionRequest(loginResponseModel.token,
                userService.getCurrentProgram(), page);

        if (page == FIRST_PAGE) {
            view.setSwipeRefresh(true);
        }

        viewModel.setLoading(true);

        Subscription subscription = salesAppService.getDistribution(distributionRequest,
                new SalesAppService.ServiceCallback<Response<DistributionResponse>>() {
                    @Override
                    public void onSuccess(Response<DistributionResponse> response) {
                        maxPage = response.data.totalPage;
                        viewModel.setLoading(false);
                        view.setSwipeRefresh(false);

                        if (response.isSuccess()) {
                            List<DistributionResponse.DistributionList> data = response.data.distributionLists;

                            if (page > FIRST_PAGE) {
                                view.onDistributionListLoadMoreSuccess(data);
                            } else {
                                view.onDistributionListSuccess(data);
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
