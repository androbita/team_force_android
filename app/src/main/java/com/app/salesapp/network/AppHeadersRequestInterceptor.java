package com.app.salesapp.network;

import android.content.Context;
import android.text.TextUtils;

import com.app.salesapp.common.DeviceUtil;
import com.app.salesapp.location.LocationService;
import com.app.salesapp.user.UserService;
import com.app.salesapp.util.Location;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class AppHeadersRequestInterceptor implements Interceptor, okhttp3.Interceptor {
    public static final String LOCATION_UNKNOWN = "UNKNOWN";
    public static final String X_LOCATION = "X-Location";
    public static final String X_APP_VERSION = "X-AppVersion";
    public static final String X_UNIQUE_ID = "X-UniqueId";
    public static final String AUTHORIZATION = "Authorization";
    public static final String X_LANGUAGE = "Accept-Language";

    private final LocationService locationService;
    private final UserService userService;
    private final String appVersionName;
    private final String uniqueId;
    private final String deviceLocale;

    public AppHeadersRequestInterceptor(Context context, LocationService locationService, UserService userService) {
        this.locationService = locationService;
        this.userService = userService;
        this.appVersionName = DeviceUtil.getAppVersionName(context);
        uniqueId = DeviceUtil.getUniqueId(context);
        deviceLocale = DeviceUtil.getDeviceLocal();
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();

        Location lastLocation = locationService.getLastKnownLocation();
        String lastKnownLocation = lastLocation == null ? LOCATION_UNKNOWN : toLatLng(lastLocation);
        Request.Builder builder = request.newBuilder()
                .addHeader(X_LOCATION, lastKnownLocation)
                .addHeader(X_APP_VERSION, appVersionName)
                .addHeader(X_UNIQUE_ID, uniqueId)
                .addHeader(X_LANGUAGE, deviceLocale);

        if (TextUtils.isEmpty(request.header(AUTHORIZATION)) &&
                userService.isLoggedIn() &&
                !TextUtils.isEmpty(userService.getAccessToken()))
            builder.addHeader(AUTHORIZATION, userService.getAuthHeader());
        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }

    private String toLatLng(Location lastLocation) {
        return String.format("%s,%s", lastLocation.latitude, lastLocation.longitude);
    }

    @Override
    public okhttp3.Response intercept(okhttp3.Interceptor.Chain chain) throws IOException {
        okhttp3.Request request = chain.request();
        Location lastLocation = locationService.getLastKnownLocation();
        String lastKnownLocation = lastLocation == null ? LOCATION_UNKNOWN : toLatLng(lastLocation);
        okhttp3.Request.Builder builder = request.newBuilder()
                .addHeader(X_LOCATION, lastKnownLocation)
                .addHeader(X_APP_VERSION, appVersionName)
                .addHeader(X_UNIQUE_ID, uniqueId)
                .addHeader(X_LANGUAGE, deviceLocale);

        if (TextUtils.isEmpty(request.header(AUTHORIZATION)) &&
                userService.isLoggedIn() &&
                !TextUtils.isEmpty(userService.getAccessToken()))
            builder.addHeader(AUTHORIZATION, userService.getAuthHeader());
        okhttp3.Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}
