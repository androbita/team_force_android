package com.app.salesapp.login;

import com.app.salesapp.BuildConfig;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.user.UserService;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class LoginPresenter implements LoginContract.Presenter {

    LoginContract.View mView;
    SalesAppService salesAppService;
    UserService userService;
    CompositeSubscription subscriptions;

    public LoginPresenter(LoginContract.View mView, SalesAppService salesAppService, UserService userService) {
        this.mView = mView;
        this.salesAppService = salesAppService;
        this.userService = userService;
        subscriptions = new CompositeSubscription();
    }

    @Override
    public void checkUserSession() {
        if (userService.isLoggedIn()) {
            mView.showSuccessLogin(userService.getUserPreference());
        }
    }

    @Override
    public void doGetLogo() {
        mView.showLoading(true);

    }

    @Override
    public void setVersionName(String versionName) {
        mView.setVersionName(versionName);
    }

    @Override
    public void doLogin(String apikey, String username, String password, String imei, String deviceToken) {
        LoginRequestModel loginRequestModel = new LoginRequestModel(apikey, username, password, imei, "", "",deviceToken);
        Subscription subscription = salesAppService.doLogin(loginRequestModel,
                new SalesAppService.ServiceCallback<Response<LoginResponseModel>>() {
                    @Override
                    public void onSuccess(Response<LoginResponseModel> result) {

                        if (result.isSuccess()) {
                            userService.saveUserPreference(result.data);
                            mView.showSuccessLogin(result.data);
                        } else {
                            mView.showErrorLogin(result.message);
                        }
                    }

                    @Override
                    public void onError(NetworkError error) {
                        mView.showErrorLogin(error.getAppErrorMessage());
                    }
                });
        subscriptions.add(subscription);
    }

    @Override
    public void onDestroy() {
        subscriptions.clear();
    }
}
