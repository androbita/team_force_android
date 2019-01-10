package com.app.salesapp.user;

import com.app.salesapp.util.SalesAppPreference;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {
    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public UserService providesSalesAppUserService(SalesAppPreference salesAppPreference, Gson gson) {
        return new UserService(salesAppPreference,gson);
    }
}
