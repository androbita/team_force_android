package com.app.salesapp.login;

import com.google.gson.annotations.SerializedName;

public class LoginRequestModel {

    @SerializedName("deviceToken")
    public String deviceToken;
    @SerializedName("apikey")
    public String apiKey;
    @SerializedName("username")
    public String username;
    @SerializedName("password")
    public String password;
    @SerializedName("imei")
    public String imei;
    @SerializedName("merk_device")
    public String merkDevice;
    @SerializedName("type_device")
    public String typeDevice;

    public LoginRequestModel(String apiKey, String username, String password, String imei, String merkDevice, String typeDevice, String deviceToken) {
        this.apiKey = apiKey;
        this.username = username;
        this.password = password;
        this.imei = imei;
        this.merkDevice = merkDevice;
        this.typeDevice = typeDevice;
        this.deviceToken = deviceToken;

    }
}
