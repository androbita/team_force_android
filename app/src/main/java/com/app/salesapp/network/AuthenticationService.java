package com.app.salesapp.network;

import com.app.salesapp.user.OauthTokenResponse;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface AuthenticationService {
    @POST("oauth/token?client_id=consumer-trusted-client&grant_type=refresh_token")
    Observable<OauthTokenResponse> refreshToken(@Query("refresh_token") String refreshToken);

    @GET("oauth/token?client_id=consumer-trusted-client&grant_type=password&username={username}&password={password}")
    Observable<OauthTokenResponse> requestToken(@Path("username") String username, @Path("password") String password);
}
