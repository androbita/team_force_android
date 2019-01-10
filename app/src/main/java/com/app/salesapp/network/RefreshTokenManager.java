package com.app.salesapp.network;

import com.app.salesapp.user.OauthTokenResponse;
import com.app.salesapp.user.UserService;
import com.google.gson.Gson;

import java.net.HttpURLConnection;

import retrofit.RetrofitError;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.functions.Func1;

public class RefreshTokenManager<T> {
    private static final String INVALID_GRANT_RESPONSE = "invalid_grant";
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final Gson gson;

    public RefreshTokenManager(AuthenticationService authenticationService,
                               UserService userService,
                               Gson gson) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.gson = gson;
    }

    public <T> Observable<T> handleRefreshToken(final Observable<T> observable) {
        return observable
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends T>>() {
                    @Override
                    public Observable<? extends T> call(final Throwable throwable) {
                        if (isTokenExpired(throwable)) {
                            return refreshToken(throwable).flatMap(new Func1<Boolean, Observable<? extends T>>() {
                                @Override
                                public Observable<? extends T> call(Boolean isSuccessful) {
                                    if (isSuccessful) {
                                        return observable;
                                    }
                                    return Observable.error(throwable);
                                }
                            });
                        }
                        return Observable.error(throwable);
                    }
                });
    }

    private boolean isTokenExpired(Throwable throwable) {
        try {
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                return httpException.response().code() == HttpURLConnection.HTTP_UNAUTHORIZED;
            } else if (throwable instanceof RetrofitError) {
                RetrofitError retrofitError = (RetrofitError) throwable;
                return retrofitError.getResponse().getStatus() == HttpURLConnection.HTTP_UNAUTHORIZED;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private Observable<Boolean> refreshToken(final Throwable unauthorizedAccessError) {
        return authenticationService.refreshToken(userService.getRefreshToken())
                .flatMap(new Func1<OauthTokenResponse, Observable<? extends Boolean>>() {
                    @Override
                    public Observable<? extends Boolean> call(OauthTokenResponse oauthTokenResponse) {
                        String accessToken = oauthTokenResponse.getAccessToken();
                        String refreshToken = oauthTokenResponse.getRefreshToken();
                        userService.saveToken(accessToken, refreshToken);
                        return Observable.just(oauthTokenResponse.isSuccessful());
                    }
                }).onErrorResumeNext(new Func1<Throwable, Observable<? extends Boolean>>() {
                    @Override
                    public Observable<? extends Boolean> call(Throwable throwable) {
                        return Observable.error(unauthorizedAccessError);
                    }
                });
    }
}
