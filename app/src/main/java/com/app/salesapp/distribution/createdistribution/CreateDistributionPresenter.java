package com.app.salesapp.distribution.createdistribution;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.attendance.createattendance.ListChannelRequestModel;
import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.distribution.model.DistributionPostRequest;
import com.app.salesapp.distribution.model.DistributionPostResponse;
import com.app.salesapp.distribution.model.MaterialRequestModel;
import com.app.salesapp.distribution.model.ReceivedRequest;
import com.app.salesapp.distribution.model.ReceivedResponse;
import com.app.salesapp.distribution.model.RequestDistributionModel;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.user.UserService;
import com.app.salesapp.util.Util;

import java.util.ArrayList;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class CreateDistributionPresenter {

    private final CreateDistributionView view;
    private final UserService userService;
    private final SalesAppService salesAppService;
    private final CreateDistributionViewModel viewModel;

    private CompositeSubscription subscription;

    public CreateDistributionPresenter(CreateDistributionView view, UserService userService,
                                       SalesAppService salesAppService, CreateDistributionViewModel viewModel) {
        this.view = view;
        this.userService = userService;
        this.salesAppService = salesAppService;
        this.viewModel = viewModel;

        subscription = new CompositeSubscription();
    }
    public void getListChannel(ListChannelRequestModel listChannelRequestModel) {
        viewModel.setLoading(true);
        Subscription subscribe = salesAppService.getListChannel(listChannelRequestModel, new SalesAppService.ServiceCallback<Response<ListChannelResponseModel>>() {
            @Override
            public void onSuccess(Response<ListChannelResponseModel> response) {
                viewModel.setLoading(false);

                if (response.isSuccess()) {
                    view.loadSpinnerData(response.data.channel_list);
                    userService.saveChannelList(response.data.channel_list);
                }
            }

            @Override
            public void onError(NetworkError error) {
                getListChannelFromLocal();
                viewModel.setLoading(false);
            }
        });

        subscription.add(subscribe);
    }

    public void getListChannelFromLocal() {
        view.loadSpinnerData(userService.getListChannel());
    }

    public void getReceived(ReceivedRequest receivedRequest) {
        viewModel.setLoading(true);
        Subscription subscribe = salesAppService.getReceived(receivedRequest, new SalesAppService.ServiceCallback<Response<ReceivedResponse>>() {
            @Override
            public void onSuccess(Response<ReceivedResponse> response) {
                viewModel.setLoading(false);

                if (response.isSuccess()) {
                    view.loadSpinnerMaterialData(response.data.receivedLists);
                    userService.saveReceiveList(response.data.receivedLists);
                }
            }

            @Override
            public void onError(NetworkError error) {
                getReceivedFromLocal();
                viewModel.setLoading(false);
            }
        });

        subscription.add(subscribe);
    }

    public void getReceivedFromLocal() {
        view.loadSpinnerMaterialData(userService.getReceiveList());
    }

    public void postDistribution(String token, String programId, String usersOrganizationId,
                                 ArrayList<MaterialRequestModel> item, String pictureBefore, String pictureAfter) {

        final DistributionPostRequest distributionPostRequest = new DistributionPostRequest();
        distributionPostRequest.token = token;
        distributionPostRequest.programId = programId;
        distributionPostRequest.usersOrganizationId = usersOrganizationId;
        distributionPostRequest.pictureBefore = pictureBefore;
        distributionPostRequest.pictureAfter = pictureAfter;
        distributionPostRequest.item = item;
        distributionPostRequest.datetime = Util.getCurrentDateTime();
        viewModel.setLoading(true);
        Subscription subscribe = salesAppService.postDistribution(distributionPostRequest,
                new SalesAppService.ServiceCallback<Response<DistributionPostResponse>>() {
                    @Override
                    public void onSuccess(Response<DistributionPostResponse> response) {
                        viewModel.setLoading(false);

                        if (response.isSuccess()) {
                            view.showMessage("success");
                            userService.setMaterialSelected(null);
                            view.finishActivity();
                        } else {
                            view.showMessage(response.message);
                        }
                    }

                    @Override
                    public void onError(NetworkError error) {
                        viewModel.setLoading(false);
                        view.onErrorPost(error.getAppErrorMessage(), distributionPostRequest);
                    }
                });

        subscription.add(subscribe);
    }

    public void onDestroy() {
        subscription.clear();
    }
}
