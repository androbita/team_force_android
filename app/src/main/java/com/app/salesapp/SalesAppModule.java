package com.app.salesapp;

import com.app.salesapp.network.AuthenticationService;
import com.app.salesapp.network.ServiceProxyBuilder;
import com.app.salesapp.user.UserService;
import com.app.salesapp.util.RxBus;
import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class SalesAppModule {
    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public SalesAppService providesSalesAppService(SalesAppNetworkService salesAppNetworkService) {
        return new SalesAppService(salesAppNetworkService);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public SalesAppNetworkService providesSalesAppNetworkService(
            @Named("cachedRetrofit") Retrofit retrofit,
            AuthenticationService authenticationService,
            UserService userService,
            Gson gson) {
        return new ServiceProxyBuilder<>(
                SalesAppNetworkService.class,
                retrofit.create(SalesAppNetworkService.class),
                authenticationService,
                userService,
                gson).build();
    }
}
