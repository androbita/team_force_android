package com.app.salesapp.inbound;

import com.app.salesapp.network.Response;

public interface InboundUpdateView {
    void showLoading(boolean show);

    void onSuccessPostInboundUpdate(Response<String> response);

    void onErrorPostInboundUpdate(String message);
}
