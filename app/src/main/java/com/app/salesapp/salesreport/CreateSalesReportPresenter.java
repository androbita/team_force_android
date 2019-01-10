package com.app.salesapp.salesreport;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.attendance.createattendance.ListChannelRequestModel;
import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.user.UserService;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class CreateSalesReportPresenter {

    private final CompositeSubscription compositeSubscription;
    private final UserService userService;

    CreateSalesReportView mView;
    SalesAppService salesAppService;

    public CreateSalesReportPresenter(CreateSalesReportView mView, SalesAppService salesAppService, UserService userService) {
        this.mView = mView;
        this.salesAppService = salesAppService;
        compositeSubscription = new CompositeSubscription();
        this.userService = userService;
    }

    public void getListChannel(ListChannelRequestModel listChannelRequestModel) {
        mView.showLoading(true);
        Subscription subscribe = salesAppService.getListChannel(listChannelRequestModel, new SalesAppService.ServiceCallback<Response<ListChannelResponseModel>>() {
            @Override
            public void onSuccess(Response<ListChannelResponseModel> response) {
                mView.showLoading(false);

                if (response.isSuccess()) {
                    mView.loadChannelSpinner(response.data.channel_list);
                    userService.saveChannelList(response.data.channel_list);
                }
            }

            @Override
            public void onError(NetworkError error) {
                getListChannelFromLocal();
                mView.showLoading(false);
            }
        });

        compositeSubscription.add(subscribe);

    }

    public void getListProduct(ListChannelRequestModel listChannelRequestModel) {

        mView.showLoading(true);
        Subscription subscribe = salesAppService.getProduct(listChannelRequestModel, new SalesAppService.ServiceCallback<Response<List<ProductModel>>>() {
            @Override
            public void onSuccess(Response<List<ProductModel>> response) {
                mView.showLoading(false);

                if (response.isSuccess()) {
                    mView.loadProductSpinner(response.data);
                    userService.saveProductList(response.data);
                }
            }

            @Override
            public void onError(NetworkError error) {
                getListProductFromLocal();
                mView.showLoading(false);
            }
        });

        compositeSubscription.add(subscribe);
        mView.loadProductSpinner(userService.getListProduct());

    }

    public void getListSellingType(ListChannelRequestModel listChannelRequestModel) {
        mView.showLoading(true);
        Subscription subscribe = salesAppService.getSellingType(listChannelRequestModel, new SalesAppService.ServiceCallback<Response<List<SellingTypeModel>>>() {
            @Override
            public void onSuccess(Response<List<SellingTypeModel>> response) {
                mView.showLoading(false);

                if (response.isSuccess()) {
                    mView.loadSellingTypeSpinner(response.data);
                    userService.saveListSellingType(response.data);
                }
            }

            @Override
            public void onError(NetworkError error) {
                getListSellingTypeFromLocal();
                mView.showLoading(false);
            }
        });

        compositeSubscription.add(subscribe);

    }

    public void postSellingReport(final SellingReportRequestModel sellingReportRequestModel) {
        mView.showLoading(true);
        Subscription subscribe = salesAppService.postSelling(sellingReportRequestModel, new SalesAppService.ServiceCallback<Response<String>>() {

            @Override
            public void onSuccess(Response<String> response) {
                mView.showLoading(false);

                if (response.isSuccess())
                    mView.onSuccessPostSelling(response);
                else
                    mView.onErrorPostSelling(response.message, sellingReportRequestModel);
            }

            @Override
            public void onError(NetworkError error) {
                mView.showLoading(false);
                mView.onErrorPostSelling(error.getAppErrorMessage(), sellingReportRequestModel);
            }
        });
        compositeSubscription.add(subscribe);
    }

    public void getListChannelFromLocal() {
        mView.loadChannelSpinner(userService.getListChannel());
    }

    public void getListProductFromLocal() {
        mView.loadProductSpinner(userService.getListProduct());

    }

    public void getListSellingTypeFromLocal() {
        mView.loadSellingTypeSpinner(userService.getSellingTypeList());
    }

}
