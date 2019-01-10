package com.app.salesapp.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.app.salesapp.common.SalesAppConstant;
import com.app.salesapp.log.SalesAppLog;

public class NetworkUtils {

    public static final int TYPE_NOT_CONNECTED = 0;
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE_DATA = 2;
    private Context context;

    public NetworkUtils(Context context) {
        this.context = context;
    }

    private int getConnectivityStatus() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE_DATA;
        }
        return TYPE_NOT_CONNECTED;
    }

    public boolean isConnected() {
        boolean isConnected = false;
        try {
            isConnected = getConnectivityStatus() != TYPE_NOT_CONNECTED;
        } catch (Exception e) {
            SalesAppLog.e(SalesAppConstant.LOG_TAG, e);
        }
        return isConnected;
    }
}
