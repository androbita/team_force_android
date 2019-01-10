package com.app.salesapp.network;

import android.content.Context;
import android.text.TextUtils;

import com.app.salesapp.R;
import com.app.salesapp.common.SalesAppEnvironment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ConfigModule {
    private final String baseUrl;

    @SuppressWarnings({"unused", "used by dagger"})
    public ConfigModule() {
        this(null);
    }

    public ConfigModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public NetworkConfig providesAppEnvironment(Context context) {
        String baseUrl = !TextUtils.isEmpty(this.baseUrl) ? this.baseUrl : context.getString(R.string.server_url);
        return new NetworkConfig(baseUrl);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public SalesAppEnvironment providesSalesAppEnvironment(Context context) {
        String environment = !TextUtils.isEmpty(this.baseUrl) ? "test" : context.getString(R.string.sales_app_env);
        return new SalesAppEnvironment(environment);
    }
}
