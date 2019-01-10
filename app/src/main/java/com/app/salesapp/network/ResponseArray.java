package com.app.salesapp.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;

public class ResponseArray<T> {

    @SerializedName("response")
    public int response;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<T> data;

    @SuppressWarnings({"unused", "used by Retrofit"})
    public ResponseArray() {
    }

    public boolean isSuccess() {
        return response == HTTP_OK;
    }
}
