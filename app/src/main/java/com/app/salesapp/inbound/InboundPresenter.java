package com.app.salesapp.inbound;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.notification.DetailTimelineModel;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class InboundPresenter {

    private final receiveView inboundView;
    private final SalesAppService salesAppService;
    CompositeSubscription subscriptions;
    private int maxPage = 0;

    public InboundPresenter(receiveView inboundView, SalesAppService salesAppService) {
        this.inboundView = inboundView;
        this.salesAppService = salesAppService;
        subscriptions = new CompositeSubscription();
    }

    public void getListInbound(String token, String currentProgram, int currentPage) {
        if (currentPage > maxPage) return;

        inboundView.showLoading(false);
        ListOutboundRequest outboundRequest = new ListOutboundRequest(token,currentProgram,String.valueOf(currentPage));
        Subscription subscription = salesAppService.getOutboundList(outboundRequest, new SalesAppService.ServiceCallback<Response<InboundModel>>() {
            @Override
            public void onSuccess(Response<InboundModel> response) {
                inboundView.showLoading(false);
                if(response.isSuccess()) {
                    inboundView.showListInbound(response.data);
                    maxPage = response.data.total_page;
                }
            }

            @Override
            public void onError(NetworkError error) {
                inboundView.showLoading(false);
                error.printStackTrace();
            }
        });
        subscriptions.add(subscription);
    }

    public void onDestroy() {

    }
}
