package com.app.salesapp.user;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import static com.app.salesapp.common.SalesAppConstant.ACCESS_TOKEN;
import static com.app.salesapp.common.SalesAppConstant.REFRESH_TOKEN;

public class OauthTokenResponse {

    @SerializedName(ACCESS_TOKEN)
    private String accessToken;
    @SerializedName(REFRESH_TOKEN)
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public OauthTokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean isSuccessful() {
        return !TextUtils.isEmpty(accessToken) && !TextUtils.isEmpty(refreshToken);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OauthTokenResponse that = (OauthTokenResponse) o;

        if (accessToken != null ? !accessToken.equals(that.accessToken) : that.accessToken != null)
            return false;
        return refreshToken != null ? refreshToken.equals(that.refreshToken) : that.refreshToken == null;

    }

    @Override
    public int hashCode() {
        int result = accessToken != null ? accessToken.hashCode() : 0;
        result = 31 * result + (refreshToken != null ? refreshToken.hashCode() : 0);
        return result;
    }
}
