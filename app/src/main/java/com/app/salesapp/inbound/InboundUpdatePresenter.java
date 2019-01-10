package com.app.salesapp.inbound;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class InboundUpdatePresenter {

    private final InboundUpdateView mView;
    private final SalesAppService salesAppService;
    CompositeSubscription subscription;

    public InboundUpdatePresenter(InboundUpdateView mView, SalesAppService salesAppService) {
        this.mView = mView;
        this.salesAppService = salesAppService;
        subscription = new CompositeSubscription();
    }

    public void postInboundUpdate(String token, String program_id, String outbound_id, String qtyReceived, String qtyReturner
            , String remark, String photos) {
        mView.showLoading(true);
        InboundUpdateRequest inboundUpdateRequest = new InboundUpdateRequest(token,
                program_id,
                outbound_id,
                qtyReceived,
                qtyReturner,
                remark,
                photos);
        Subscription subscribe = salesAppService.postInboundUpdate(inboundUpdateRequest, new SalesAppService.ServiceCallback<Response<String>>() {

            @Override
            public void onSuccess(Response<String> response) {
                mView.showLoading(false);

                if (response.isSuccess())
                    mView.onSuccessPostInboundUpdate(response);
                else
                    mView.onErrorPostInboundUpdate(response.message);
            }

            @Override
            public void onError(NetworkError error) {
                mView.showLoading(false);
                error.printStackTrace();
                mView.onErrorPostInboundUpdate(error.getAppErrorMessage());
            }
        });
        subscription.add(subscribe);
    }

}
