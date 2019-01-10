package com.app.salesapp.Gps.serviceLocation;

import com.app.salesapp.okHttp3LoggingInterceptor.OkHttp3LoggingInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zcky on 2/22/18.
 */

public class ServiceApi {
    public static final String BASE_URL="http://www.surfgold.co.id/loyalty/" ;
    private static Retrofit retrofit = null;

    public static OkHttpClient getOKHttp(){
        OkHttp3LoggingInterceptor logging = new OkHttp3LoggingInterceptor();
        logging.setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(logging).build();
        return client;
    }

    public static Retrofit getClient(){
        OkHttpClient asd = getOKHttp();
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                      .baseUrl(BASE_URL)
                      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                      .addConverterFactory(GsonConverterFactory.create())
                      .client(asd)
                      .build();
            }
        return retrofit;

    }
}
