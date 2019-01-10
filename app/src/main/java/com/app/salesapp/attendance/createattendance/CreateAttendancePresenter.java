package com.app.salesapp.attendance.createattendance;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.user.UserService;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class CreateAttendancePresenter {

    private final UserService userService;
    CreateAttendanceView mView;
    SalesAppService salesAppService;
    CompositeSubscription compositeSubscription;

    public CreateAttendancePresenter(CreateAttendanceView view, SalesAppService salesAppService, UserService userService) {
        this.mView = view;
        this.salesAppService = salesAppService;
        compositeSubscription =  new CompositeSubscription();
        this.userService = userService;
    }



    public void postAttendance(final PostAttendanceRequestModel postAttendanceRequestModel) {
        mView.showLoading(true);
        Subscription subscribe = salesAppService.postAttendance(postAttendanceRequestModel, new SalesAppService.ServiceCallback<Response<String>>() {

            @Override
            public void onSuccess(Response<String> response) {
                mView.showLoading(false);

                if (response.isSuccess()) {
                    mView.onSuccessPostAttendance(response);
                } else {
                    mView.onErrorPostAttendance(response.message, postAttendanceRequestModel);
                }
            }

            @Override
            public void onError(NetworkError error) {
                mView.showLoading(false);
                mView.onErrorPostAttendance(error.getAppErrorMessage(),postAttendanceRequestModel);
            }
        });
        compositeSubscription.add(subscribe);
    }

    public void onDestroy() {
        compositeSubscription.clear();
    }

    public void getListChannelFromLocalStorage() {
        mView.loadSpinnerData(userService.getListChannel(),userService.getListSubject());
    }

    public void getListChannel(ListChannelRequestModel listChannelRequestModel) {
        mView.showLoading(true);
        Subscription subscribe = salesAppService.getListChannel(listChannelRequestModel, new SalesAppService.ServiceCallback<Response<ListChannelResponseModel>>() {
            @Override
            public void onSuccess(Response<ListChannelResponseModel> response) {
                mView.showLoading(false);

                if (response.isSuccess()) {
                    mView.loadSpinnerData(response.data.channel_list, response.data.subject_list);
                    userService.saveChannelList(response.data.channel_list);
                    userService.saveSubjectList(response.data.subject_list);
                }
            }

            @Override
            public void onError(NetworkError error) {
                getListChannelFromLocalStorage();
                mView.showLoading(false);
            }
        });

        compositeSubscription.add(subscribe);


    }
}
