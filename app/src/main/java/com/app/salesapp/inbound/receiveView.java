package com.app.salesapp.inbound;

public interface receiveView {
    void showListInbound(InboundModel data);

    void showLoading(boolean b);

    void showUpdateReceive();
}
