package com.app.salesapp.network;

import com.google.gson.annotations.SerializedName;

import static java.net.HttpURLConnection.HTTP_OK;

public class Response<T> {

    @SerializedName("response")
    public int response;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public T data;

    @SuppressWarnings({"unused", "used by Retrofit"})
    public Response() {
    }

    public boolean isSuccess() {
        return response == HTTP_OK;
    }
}
