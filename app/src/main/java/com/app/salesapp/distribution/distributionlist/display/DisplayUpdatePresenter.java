package com.app.salesapp.distribution.distributionlist.display;

import android.text.TextUtils;
import android.util.Log;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.distribution.model.DisplayPostRequest;
import com.app.salesapp.distribution.model.DistributionListRequest;
import com.app.salesapp.distribution.model.DistributionPostResponse;
import com.app.salesapp.distribution.model.DistributionPostResponseModel;
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

public class DisplayUpdatePresenter {
    private final DisplayUpdateView view;
    private final UserService userService;
    private final SalesAppService salesAppService;
    private final DisplayUpdateViewModel viewModel;

    private CompositeSubscription subscription;

    public DisplayUpdatePresenter(DisplayUpdateView view, UserService userService,
                                  SalesAppService salesAppService, DisplayUpdateViewModel viewModel) {
        this.view = view;
        this.userService = userService;
        this.salesAppService = salesAppService;
        this.viewModel = viewModel;

        subscription = new CompositeSubscription();
    }

    public void postDisplayed(int display, int active, String token, String programId, String usersOrganizationId,
                                 String merchandiseDistributionId, String remarks, String picture) {

        if (TextUtils.isEmpty(picture)) {
            view.showMessage("Photo is empty!");
            return;
        }

        DisplayPostRequest displayPostRequest = new DisplayPostRequest();
        displayPostRequest.display = display;
        displayPostRequest.active = active;
        displayPostRequest.token = token;
        displayPostRequest.programId = programId;
        displayPostRequest.usersOrganizationId = usersOrganizationId;
        displayPostRequest.merchandiseDistributionId = merchandiseDistributionId;
        displayPostRequest.remarks = remarks;
        displayPostRequest.picture = picture;

        viewModel.setLoading(true);
        Subscription subscribe = salesAppService.postMaintenance(displayPostRequest,
                new SalesAppService.ServiceCallback<Response<DistributionPostResponse>>() {
                    @Override
                    public void onSuccess(Response<DistributionPostResponse> response) {
                        viewModel.setLoading(false);

                        if (response.isSuccess()) {
                            view.showMessage("success");
                            view.finishActivity();
                        } else {
                            view.showMessage(response.message);
                        }
                    }

                    @Override
                    public void onError(NetworkError error) {
                        viewModel.setLoading(false);
                        view.showMessage(error.getAppErrorMessage());
                    }
                });

        subscription.add(subscribe);
    }


    public void getDistribution(String users_organization_id) {
        LoginResponseModel loginResponseModel = userService.getUserPreference();
        DistributionListRequest distributionRequest = new DistributionListRequest(loginResponseModel.token,
                userService.getCurrentProgram(), users_organization_id);

        Subscription subscribe = salesAppService.getDistributionList(distributionRequest,
                new SalesAppService.ServiceCallback<Response<DistributionPostResponseModel>>() {
                    @Override
                    public void onSuccess(Response<DistributionPostResponseModel> response) {
                         if (response.isSuccess()) {
                            List<DistributionPostResponseModel.DistributionList> data = response.data.listDistribution;
                             Log.d("response", String.valueOf(response.data.listDistribution.size()));
                            view.setMaterialData(data);
                        } else {

                        }
                    }

                    @Override
                    public void onError(NetworkError error) {
                        if (error.isAuthFailure()) {
                            userService.clearData();
                        } else {
                            view.showMessage(error.getMessage().toString());
                        }
                    }
                });
        subscription.add(subscribe);
    }
    public void onDestroy() {
        subscription.clear();
    }
}
