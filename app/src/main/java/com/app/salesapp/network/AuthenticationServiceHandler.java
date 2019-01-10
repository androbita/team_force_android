package com.app.salesapp.network;

import com.app.salesapp.common.SalesAppConstant;
import com.app.salesapp.log.SalesAppLog;
import com.app.salesapp.user.UserService;
import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import rx.Observable;
import rx.functions.Func1;

public class AuthenticationServiceHandler<T> implements InvocationHandler {
    private final RefreshTokenManager refreshTokenManager;
    private final T service;

    public AuthenticationServiceHandler(T service,
                                        AuthenticationService authenticationService,
                                        UserService userService,
                                        Gson gson) {
        this.service = service;
        this.refreshTokenManager = new RefreshTokenManager(authenticationService, userService, gson);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Observable<Object> observable = (Observable<Object>) method.invoke(service, args);

        observable.onErrorResumeNext(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                SalesAppLog.e(SalesAppConstant.LOG_TAG, throwable);
                return Observable.error(throwable);
            }
        });

        SalesAppAuthenticated salesAppAuthenticated = method.getAnnotation(SalesAppAuthenticated.class);
        if (salesAppAuthenticated != null)
            return this.refreshTokenManager.handleRefreshToken(observable);
        else
            return observable;
    }
}
