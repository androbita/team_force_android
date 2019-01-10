package com.app.salesapp.common;

import android.content.Context;

import com.app.salesapp.util.RxBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Context context;
    private final RxBus rxBus;
    public AppModule(Context context, RxBus rxBus) {
        this.context = context;
        this.rxBus = rxBus;
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public Context providesContext() {
        return context;
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public RxBus providesRxBus() {
        return rxBus;
    }
}
