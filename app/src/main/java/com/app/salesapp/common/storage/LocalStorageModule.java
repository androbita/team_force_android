package com.app.salesapp.common.storage;

import android.content.Context;

import com.app.salesapp.location.LocationService;
import com.app.salesapp.util.SalesAppPreference;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalStorageModule {
    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public SalesAppPreference providesSalesAppPreferences(Context context) {
        return SalesAppPreference.getInstance(context);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public AppSessionStoreService providesAppSessionStoreService(Context context, Gson gson) {
        return new AppSessionStoreService(context, gson);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public LocationService providesLocationService(AppSessionStoreService appSessionStoreService) {
        return new LocationService(appSessionStoreService);
    }
}
