package com.app.salesapp.network;

import com.app.salesapp.log.SalesAppLog;

import static com.app.salesapp.common.SalesAppConstant.LOG_TAG;

public class NetworkConfig {
    public final String baseUrl;

    public NetworkConfig(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        SalesAppLog.d(LOG_TAG, baseUrl);
        return baseUrl;
    }
}
