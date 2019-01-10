package com.app.salesapp.network;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.app.salesapp.log.SalesAppLog;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import okio.Buffer;
import okio.BufferedSource;

import static com.app.salesapp.common.SalesAppConstant.ACCESS_TOKEN;
import static com.app.salesapp.common.SalesAppConstant.CONFIRMATION_NEW_PASSWORD;
import static com.app.salesapp.common.SalesAppConstant.CONFIRMATION_PASSWORD;
import static com.app.salesapp.common.SalesAppConstant.CURRENT_PASSWORD;
import static com.app.salesapp.common.SalesAppConstant.LOG_TAG;
import static com.app.salesapp.common.SalesAppConstant.NEW_PASSWORD;
import static com.app.salesapp.common.SalesAppConstant.PASSWORD;
import static com.app.salesapp.common.SalesAppConstant.REFRESH_TOKEN;

/**
 * utility class to intercept the http request.
 * It will add error details into event tracker in case of failure api call.
 */
public class HttpInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static final String AUTHORIZATION = "Authorization";

    private static final String TAG = HttpInterceptor.class.getName();
    private static final String PLACE_HOLDER = "**********";
    private static final String PASSWORD_URL_FIELD = "password=";

    private final Gson gson;

    public HttpInterceptor(Gson gson) {
        this.gson = gson;
    }

    /**
     * Framework  method which will be get called whenever new http request gets called.
     * Here in this method we intercept api call and if api call fails, we send that details to
     * event tracker.
     *
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();
        final String url = getUrl(request);
        final String httpMethod = request.method();
        final String requestHeaders = getHeaders(request.headers());
        final String requestData = readRequestBody(request);
        final Response response = chain.proceed(request);
        final int responseCode = response.code();
        final String responseHeaders = getHeaders(response.headers());
        final String responseBody = readResponseBody(response.body());
        if (responseCode >= 400 && responseCode <= 599) {
            try {
                NetworkResponse errorResponse = gson.fromJson(responseBody, NetworkResponse.class);
                String error = null;
                String email = null;
                if (errorResponse != null) {
                    if (!TextUtils.isEmpty(errorResponse.status)) {
                        error = errorResponse.status;
                    } else if (!TextUtils.isEmpty(errorResponse.message)) {
                        error = errorResponse.message;
                    }
                    if (!TextUtils.isEmpty(errorResponse.email)) {
                        email = errorResponse.email;
                    } else if (!TextUtils.isEmpty(errorResponse.userName)) {
                        email = errorResponse.userName;
                    }
                }
            } catch (Exception ex) {
                SalesAppLog.e(LOG_TAG, ex);
            }
        }
        return response;
    }

    private String getHeaders(Headers headers) {
        Set<String> names = headers.names();
        Iterator<String> iterator = names.iterator();
        StringBuilder headerString = new StringBuilder();
        while (iterator.hasNext()) {
            String header = iterator.next();
            if (!AUTHORIZATION.equals(header)) {
                headerString
                        .append(header)
                        .append(": ")
                        .append(headers.get(header))
                        .append("\n");
            }
        }
        return headerString.toString();
    }

    private String getUrl(Request request) {
        try {
            return filterUrl(request.uri().toString());
        } catch (Throwable e) {
            return "UNKNOWN URL";
        }
    }

    @NonNull
    private String readRequestBody(Request request) {
        String requestData = "-";
        try {
            RequestBody requestBody = request.body();
            if (requestBody == null)
                return requestData;
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            requestData = buffer.readString(charset);
            return getFilteredData(requestData);
        } catch (Throwable e) {
            SalesAppLog.e(LOG_TAG, e);
            return requestData;
        }
    }

    private String readResponseBody(ResponseBody responseBody) {
        String responseData = "-";
        if (responseBody == null)
            return responseData;
        try {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE);
            Buffer responseBuffer = source.buffer();
            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            String responseString = responseBuffer.clone().readString(charset);
            return getFilteredData(responseString);
        } catch (Throwable e) {
            SalesAppLog.e(LOG_TAG, e);
            return responseData;
        }
    }

    private static String getFilteredData(String request) {
        if (request == null || request.length() <= 0)
            return request;
        try {
            Object json = new JSONTokener(request).nextValue();

            if (!(json instanceof JSONObject)) {
                return request;
            }

            JSONObject jsonObject = ((JSONObject) json);
            if (jsonObject.has(ACCESS_TOKEN)) {
                jsonObject.put(ACCESS_TOKEN, PLACE_HOLDER);
            }

            if (jsonObject.has(REFRESH_TOKEN)) {
                jsonObject.put(REFRESH_TOKEN, PLACE_HOLDER);
            }

            if (jsonObject.has(CURRENT_PASSWORD)) {
                jsonObject.put(CURRENT_PASSWORD, PLACE_HOLDER);
            }

            if (jsonObject.has(PASSWORD)) {
                jsonObject.put(PASSWORD, PLACE_HOLDER);
            }

            if (jsonObject.has(CONFIRMATION_PASSWORD)) {
                jsonObject.put(CONFIRMATION_PASSWORD, PLACE_HOLDER);
            }

            if (jsonObject.has(NEW_PASSWORD)) {
                jsonObject.put(NEW_PASSWORD, PLACE_HOLDER);
            }

            if (jsonObject.has(CONFIRMATION_NEW_PASSWORD)) {
                jsonObject.put(CONFIRMATION_NEW_PASSWORD, PLACE_HOLDER);
            }

            return jsonObject.toString();
        } catch (Throwable e) {
            SalesAppLog.e(TAG, e);
            return request;
        }
    }

    String filterUrl(String url) {
        if (!url.contains(PASSWORD_URL_FIELD)) return url;
        return url.replaceAll("([\\?&]{1})password=[^&$]*", "$1password=" + PLACE_HOLDER);
    }

    public static class NetworkResponse {
        @SerializedName("status")
        public String status;

        @SerializedName("message")
        public String message;

        @SerializedName("email")
        public String email;

        @SerializedName("userName")
        public String userName;
    }
}
