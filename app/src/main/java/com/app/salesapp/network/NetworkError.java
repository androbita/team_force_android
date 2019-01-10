package com.app.salesapp.network;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import retrofit2.adapter.rxjava.HttpException;

import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

public class NetworkError extends Throwable {
    public static final String DEFAULT_ERROR_MESSAGE = "Something went wrong! Please try again.";
    public static final String NETWORK_ERROR_MESSAGE = "Terjadi Kesalahan !";
    public static final String ERROR_MESSAGE_HEADER = "Error-Message";
    private final Throwable error;
    private String errorBody;

    public NetworkError(Throwable e) {
        super(e);
        this.error = e;
    }

    public String getMessage() {
        return error.getMessage();
    }

    public boolean isAuthFailure() {
        return getErrorCodeFromBodyResponse() == HTTP_UNAUTHORIZED;
    }

    public String getAppErrorMessage() {
        if (isDueToFlakyNetworkConnection()) return NETWORK_ERROR_MESSAGE;
        if (isNotHttpError()) return DEFAULT_ERROR_MESSAGE;
        retrofit2.Response<?> response = ((HttpException) this.error).response();
        if (response != null) {
            String status = getErrorMessageFromBodyResponse();
            if (!TextUtils.isEmpty(status)) return status;

            Map<String, List<String>> headers = response.headers().toMultimap();
            if (headers.containsKey(ERROR_MESSAGE_HEADER))
                return headers.get(ERROR_MESSAGE_HEADER).get(0);
        }

        return DEFAULT_ERROR_MESSAGE;
    }

    protected String getErrorMessageFromBodyResponse() {
        try {
            return getResponse(Response.class).message;
        } catch (Exception e) {
            return null;
        }
    }

    protected int getErrorCodeFromBodyResponse() {
        try {
            return getResponse(Response.class).response;
        } catch (Exception e) {
            return HTTP_NOT_FOUND;
        }
    }

    public <T> T getResponse(Class<T> tClass) throws IOException {
        retrofit2.Response<?> response = ((HttpException) this.error).response();
        String responseBodyString = errorBody(response);
        T tResponse = new Gson().fromJson(responseBodyString, tClass);
        return tResponse;
    }

    @NonNull
    private String errorBody(retrofit2.Response<?> response) throws IOException {
        return this.errorBody == null ? this.errorBody = response.errorBody().string() : this.errorBody;
    }

    public Throwable getError() {
        return error;
    }

    public boolean isDueToFlakyNetworkConnection() {
        return error instanceof IOException;
    }

    private boolean isNotHttpError() {
        return !(this.error instanceof HttpException);
    }
}
