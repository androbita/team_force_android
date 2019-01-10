package com.app.salesapp;

import android.support.multidex.MultiDexApplication;

import com.app.salesapp.common.AppModule;
import com.app.salesapp.deps.DaggerSalesAppDeps;
import com.app.salesapp.deps.SalesAppDeps;
import com.app.salesapp.util.RxBus;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import javax.inject.Inject;

public class SalesApp extends MultiDexApplication {

    @Inject
    SalesAppService salesAppService;

    private SalesAppDeps salesAppDeps;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        salesAppDeps = DaggerSalesAppDeps.builder().appModule(new AppModule(this, new RxBus())).build();
    }

    public SalesAppService getSalesAppService() {
        return salesAppService;
    }

    public SalesAppDeps getSalesAppDeps() {
        return salesAppDeps;
    }
}
