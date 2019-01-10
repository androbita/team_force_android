package com.app.salesapp.homeAttendance;

import android.widget.Toast;

import com.app.salesapp.R;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.attendance.createattendance.CreateAttendanceView;
import com.app.salesapp.attendance.createattendance.ListChannelRequestModel;
import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.attendance.createattendance.PostAttendanceRequestModel;
import com.app.salesapp.channel.model.PostChannelRequestModel;
import com.app.salesapp.city.CityRequestModel;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.user.UserService;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zcky on 3/4/18.
 */

public class HomeAttendancePresenter {
    private final UserService userService;
    HomeAttendaceContract.View mView;
    SalesAppService salesAppService;
    CompositeSubscription compositeSubscription;

    public HomeAttendancePresenter(HomeAttendaceContract.View view, SalesAppService salesAppService, UserService userService) {
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
            }
        });
        compositeSubscription.add(subscribe);
    }

    public void postChannel(final PostChannelRequestModel postChannelRequestModel) {
        Subscription subscribe = salesAppService.postChannel(postChannelRequestModel, new SalesAppService.ServiceCallback<Response<String>>() {
            @Override
            public void onSuccess(Response<String> response) {
                mView.onSuccessCreateChannel(response);
            }

            @Override
            public void onError(NetworkError error) {
                mView.onErrorCreateChannel(error.getMessage().toString(),postChannelRequestModel);
            }
        });
        compositeSubscription.add(subscribe);
    }

    public void getListCity(final CityRequestModel cityRequestModel) {
        Subscription subscribe = salesAppService.getListCity(cityRequestModel, new SalesAppService.ServiceCallback<Response<String>>() {
            @Override
            public void onSuccess(Response<String> response) {

            }
            @Override
            public void onError(NetworkError error) {

            }
        });
        compositeSubscription.add(subscribe);
    }

    public void getNewListChannel(ListChannelRequestModel listChannelRequestModel) {
        Subscription subscribe = salesAppService.getListChannel(listChannelRequestModel, new SalesAppService.ServiceCallback<Response<ListChannelResponseModel>>() {
            @Override
            public void onSuccess(Response<ListChannelResponseModel> response) {

                if (response.isSuccess())
                    mView.onListChannelReceived(response.data);
            }

            @Override
            public void onError(NetworkError error) {
                mView.showLoading(false);
            }
        });

        compositeSubscription.add(subscribe);
    }
}
