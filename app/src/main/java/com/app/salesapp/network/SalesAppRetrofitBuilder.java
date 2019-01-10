package com.app.salesapp.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SalesAppRetrofitBuilder {
    private OkHttpClient okHttpClient;
    private GsonConverterFactory gsonConverterFactory;

    public SalesAppRetrofitBuilder(OkHttpClient okHttpClient, GsonConverterFactory gsonConverterFactory) {
        this.okHttpClient = okHttpClient;
        this.gsonConverterFactory = gsonConverterFactory;
    }

    public Retrofit buildForUrl(String url) {
        return new Retrofit.Builder()
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(url)
                .build();
    }
}
