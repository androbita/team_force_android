package com.app.salesapp.network;

import android.content.Context;
import android.text.TextUtils;

import com.app.salesapp.R;
import com.app.salesapp.common.SalesAppEnvironment;
import com.app.salesapp.location.LocationService;
import com.app.salesapp.okHttp3LoggingInterceptor.OkHttp3LoggingInterceptor;
import com.app.salesapp.okio.HttpLoggingInterceptor;
import com.app.salesapp.user.UserService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.salesapp.okio.HttpLoggingInterceptor.Level.getLogLevel;

@Module
public class CommonNetworkModule {

    public static final int CACHE_DIR_SIZE_30MB = 30 * 1024 * 1024;

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public AppHeadersRequestInterceptor providesAppHeadersRequestInterceptor(
            Context context,
            LocationService locationService,
            UserService userService) {
        return new AppHeadersRequestInterceptor(context, locationService, userService);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public HttpInterceptor providesHttpInterceptor(Gson gson) {
        return new HttpInterceptor(gson);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public HttpLoggingInterceptor providesHttpLoggingInterceptor(Context context) {
        return new HttpLoggingInterceptor().setLevel(getLogLevel(context.getString(R.string.okhttp_log_level)));
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public OkHttpClient providesOkHttpClient(Context context,
                                             SalesAppEnvironment salesAppEnvironment,
                                             AppHeadersRequestInterceptor appHeadersRequestInterceptor,
                                             HttpInterceptor httpInterceptor,
                                             HttpLoggingInterceptor httpLoggingInterceptor) {
        OkHttpClient okHttpClient = new OkHttpClient();
        List<Interceptor> interceptors = okHttpClient.networkInterceptors();
        interceptors.add(appHeadersRequestInterceptor);
        interceptors.add(httpInterceptor);
        interceptors.add(httpLoggingInterceptor);
        int timeout = salesAppEnvironment.isTestEnvironment() ? 5 : 30;
        okHttpClient.setConnectTimeout(timeout, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(timeout, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(timeout, TimeUnit.SECONDS);
        return okHttpClient;
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public Gson providesGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).
                registerTypeAdapter(Date.class, new DateTypeAdapter()).create();
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public RestAdapter providesRestAdapter(
            NetworkConfig config,
            OkHttpClient okHttpClient,
            Gson gson,
            final UserService userService,
            RestAdapter.LogLevel retrofitLogLevel) {
        RestAdapter.Builder restAdapterBuilder = new RestAdapter.Builder();
        restAdapterBuilder.setConverter(new GsonConverter(gson));
        restAdapterBuilder.setEndpoint(config.getBaseUrl());
        restAdapterBuilder.setClient(new OkClient(okHttpClient));
        restAdapterBuilder.setLogLevel(retrofitLogLevel);
        return restAdapterBuilder.build();
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public RestAdapter.LogLevel getRetrofitLogLevel(Context context) {
        String level = context.getString(R.string.okhttp_log_level);
        if (TextUtils.isEmpty(level)) return RestAdapter.LogLevel.NONE;
        switch (level) {
            case "NONE":
                return RestAdapter.LogLevel.NONE;
            case "BASIC":
                return RestAdapter.LogLevel.BASIC;
            case "HEADERS":
                return RestAdapter.LogLevel.HEADERS;
            case "BODY":
                return RestAdapter.LogLevel.FULL;
            default:
                return RestAdapter.LogLevel.NONE;
        }
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public OkHttp3LoggingInterceptor providesOkHttp3LoggingInterceptor(Context context) {
        OkHttp3LoggingInterceptor interceptor = new OkHttp3LoggingInterceptor();
        interceptor.setLevel(logLevel(context.getString(R.string.okhttp_log_level)));
        return interceptor;
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public okhttp3.logging.HttpLoggingInterceptor providesOkHttpLoggingInterceptor(Context context) {
        okhttp3.logging.HttpLoggingInterceptor interceptor = new okhttp3.logging.HttpLoggingInterceptor();
        interceptor.setLevel(logLevel(context.getString(R.string.okhttp_log_level)));
        return interceptor;
    }

    private okhttp3.logging.HttpLoggingInterceptor.Level logLevel(String level) {
        if (level == null) return okhttp3.logging.HttpLoggingInterceptor.Level.NONE;
        switch (level) {
            case "NONE":
                return okhttp3.logging.HttpLoggingInterceptor.Level.NONE;
            case "BASIC":
                return okhttp3.logging.HttpLoggingInterceptor.Level.BASIC;
            case "HEADERS":
                return okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;
            case "BODY":
                return okhttp3.logging.HttpLoggingInterceptor.Level.BODY;
            default:
                return okhttp3.logging.HttpLoggingInterceptor.Level.NONE;
        }
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public okhttp3.OkHttpClient providesOkHttpClient3(
            SalesAppEnvironment salesAppEnvironment,
            AppHeadersRequestInterceptor appHeadersRequestInterceptor,
            OkHttp3LoggingInterceptor httpLoggingInterceptor) {
        int timeout = salesAppEnvironment.isTestEnvironment() ? 5 : 30;
        return new okhttp3.OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(appHeadersRequestInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public Cache providesCache(Context context) {
        return new Cache(context.getExternalCacheDir(), CACHE_DIR_SIZE_30MB);
    }

    @Provides
    @Singleton
    @Named("cachedOkHttp3Client")
    @SuppressWarnings("unused")
    public okhttp3.OkHttpClient providesCachedOkHttpClient3(
            SalesAppEnvironment salesAppEnvironment,
            AppHeadersRequestInterceptor appHeadersRequestInterceptor,
            OkHttp3LoggingInterceptor httpLoggingInterceptor,
            Cache cache) {
        int timeout = salesAppEnvironment.isTestEnvironment() ? 5 : 30;
        return new okhttp3.OkHttpClient.Builder()
                .cache(cache)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(appHeadersRequestInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    @Named("nonCachedOkHttp3Client")
    @SuppressWarnings("unused")
    public okhttp3.OkHttpClient providesNonCachedOkHttpClient3(
            SalesAppEnvironment salesAppEnvironment,
            OkHttp3LoggingInterceptor httpLoggingInterceptor
    ) {
        int timeout = salesAppEnvironment.isTestEnvironment() ? 5 : 30;
        return new okhttp3.OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    @Named("nonCachedAuthenticatedOkHttp3Client")
    @SuppressWarnings("unused")
    public okhttp3.OkHttpClient providesNonCachedAuthenticatedOkHttpClient3(
            SalesAppEnvironment salesAppEnvironment,
            AppHeadersRequestInterceptor appHeadersRequestInterceptor,
            OkHttp3LoggingInterceptor httpLoggingInterceptor
    ) {
        int timeout = salesAppEnvironment.isTestEnvironment() ? 5 : 30;
        return new okhttp3.OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(appHeadersRequestInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build();
    }

    @Provides
    @Singleton
    @Named("cachedRetrofit")
    @SuppressWarnings("unused")
    public Retrofit providesCachedRetrofit(
            @Named("cachedOkHttp3Client") okhttp3.OkHttpClient okHttpClient,
            NetworkConfig networkConfig,
            Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(networkConfig.getBaseUrl())
                .build();
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public SalesAppRetrofitBuilder providesSalesAppRetrofitBuilder(okhttp3.OkHttpClient okHttpClient, Gson gson) {
        return new SalesAppRetrofitBuilder(okHttpClient, GsonConverterFactory.create(gson));
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    @Named("cached")
    public SalesAppRetrofitBuilder providesCachedSalesAppRetrofitBuilder(@Named("cachedOkHttp3Client") okhttp3.OkHttpClient okHttpClient, Gson gson) {
        return new SalesAppRetrofitBuilder(okHttpClient, GsonConverterFactory.create(gson));
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public Retrofit.Builder providesRetrofitBuilder(
            okhttp3.OkHttpClient okHttpClient,
            Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public Retrofit providesRetrofit(Retrofit.Builder builder, NetworkConfig config) {
        return builder
                .baseUrl(config.getBaseUrl())
                .build();
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public AuthenticationService providesAuthenticationService(Retrofit retrofit) {
        return retrofit.create(AuthenticationService.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public NetworkUtils providesNetworkUtils(Context context) {
        return new NetworkUtils(context);
    }
}
