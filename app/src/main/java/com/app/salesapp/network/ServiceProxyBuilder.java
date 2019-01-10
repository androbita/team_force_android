package com.app.salesapp.network;

import com.app.salesapp.user.UserService;
import com.google.gson.Gson;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ServiceProxyBuilder<T> {
    private final Class<T> type;
    private final T service;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final Gson gson;

    public ServiceProxyBuilder(Class<T> type, T service,
                               AuthenticationService authenticationService,
                               UserService userService,
                               Gson gson) {
        this.type = type;
        this.service = service;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.gson = gson;
    }

    public T build() {
        InvocationHandler authenticationServiceHandler = new AuthenticationServiceHandler<T>(service, authenticationService, userService, gson);
        return ((T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, authenticationServiceHandler));
    }
}
