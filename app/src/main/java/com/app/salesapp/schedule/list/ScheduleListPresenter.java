package com.app.salesapp.schedule.list;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.login.LoginResponseModel;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.schedule.model.ScheduleRequest;
import com.app.salesapp.schedule.model.ScheduleResponse;
import com.app.salesapp.user.UserService;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.app.salesapp.common.SalesAppConstant.FIRST_PAGE;

public class ScheduleListPresenter {

    private final ScheduleListView view;
    private final SalesAppService salesAppService;
    private final UserService userService;
    private final ScheduleListViewModel viewModel;
    private int maxPage = 1;
    private CompositeSubscription subscriptions;

    public ScheduleListPresenter(ScheduleListView view, SalesAppService salesAppService,
                                 UserService userService, ScheduleListViewModel viewModel) {
        this.view = view;
        this.salesAppService = salesAppService;
        this.userService = userService;
        this.viewModel = viewModel;

        subscriptions = new CompositeSubscription();
    }

    public void getSchedule(final int page) {
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
        ScheduleRequest ScheduleRequest = new ScheduleRequest(loginResponseModel.token,
                userService.getCurrentProgram(), page);

        if (page == FIRST_PAGE) {
            view.setSwipeRefresh(true);
        }

        viewModel.setLoading(true);

        Subscription subscription = salesAppService.getSchedule(ScheduleRequest,
                new SalesAppService.ServiceCallback<Response<ScheduleResponse>>() {
                    @Override
                    public void onSuccess(Response<ScheduleResponse> response) {
                        maxPage = response.data.totalPage;
                        viewModel.setLoading(false);
                        view.setSwipeRefresh(false);

                        if (response.isSuccess()) {
                            List<ScheduleResponse.ScheduleList> data = response.data.scheduleLists;

                            if (page > FIRST_PAGE) {
                                view.onScheduleListLoadMoreSuccess(data);
                            } else {
                                view.onScheduleListSuccess(data);
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
