/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.app.salesapp.okio;

import android.util.Log;

import com.squareup.okhttp.Connection;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.internal.http.HttpEngine;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okio.Buffer;
import okio.BufferedSource;

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * {@linkplain OkHttpClient#interceptors() application interceptor} or as a {@linkplain
 * OkHttpClient#networkInterceptors() network interceptor}. <p> The format of the logs created by
 * this class should not be considered stable and may change slightly between releases. If you need
 * a stable logging format, use your own interceptor.
 */
public final class HttpLoggingInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    public static final String TAG = "OKHTTP_NETWORK";

    public enum Level {
        /**
         * No logs.
         */
        NONE,
        /**
         * Logs request and response lines.
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
         * }</pre>
         */
        BASIC,
        /**
         * Logs request and response lines and their respective headers.
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * }</pre>
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         * <p>
         * <p>Example:
         * <pre>{@code
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END GET
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * }</pre>
         */
        BODY;

        public static Level getLogLevel(String level) {
            if (level == null) return NONE;
            switch (level) {
                case "NONE":
                    return NONE;
                case "BASIC":
                    return BASIC;
                case "HEADERS":
                    return HEADERS;
                case "BODY":
                    return BODY;
                default:
                    return NONE;
            }
        }
    }

    public interface Logger {
        void log(String message);

        /**
         * A {@link Logger} defaults output appropriate for the current platform.
         */
        Logger DEFAULT = new Logger() {
            @Override
            public void log(String message) {
                Log.d(TAG, message);
            }
        };
    }

    public HttpLoggingInterceptor() {
        this(Logger.DEFAULT);
    }

    public HttpLoggingInterceptor(Logger logger) {
        this.logger = logger;
    }

    private final Logger logger;

    private volatile Level level = Level.NONE;

    /**
     * Change the level at which this interceptor logs.
     */
    public HttpLoggingInterceptor setLevel(Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
        return this;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Level level = this.level;

        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = level == Level.BODY;
        boolean logHeaders = logBody || level == Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.getProtocol() : Protocol.HTTP_1_1;
        String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        logger.log(requestStartMessage);

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    logger.log("Content-Type: " + requestBody.contentType());
                }
                if (requestBody.contentLength() != -1) {
                    logger.log("Content-Length: " + requestBody.contentLength());
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    logger.log(name + ": " + headers.value(i));
                }
            }
            logger.log("--> End Headers");
            if (!logBody || !hasRequestBody) {
                logger.log("--> END " + request.method());
            } else if (bodyEncoded(request.headers())) {
                logger.log("--> END " + request.method() + " (encoded body omitted)");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }

                logger.log("");
                if (isPlaintext(buffer)) {
                    logger.log(buffer.readString(charset));
                    logger.log("--> END " + request.method()
                            + " (" + requestBody.contentLength() + "-byte body)");
                } else {
                    logger.log("--> END " + request.method() + " (binary "
                            + requestBody.contentLength() + "-byte body omitted)");
                }
            }
        }

        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        logger.log("<-- " + response.code() + ' ' + response.message() + ' '
                + response.request().url() + " (" + tookMs + "ms" + (!logHeaders ? ", "
                + bodySize + " body" : "") + ')');

        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                logger.log(headers.name(i) + ": " + headers.value(i));
            }
            logger.log("--> End Headers");

            if (!logBody || !HttpEngine.hasBody(response)) {
                logger.log("<-- END HTTP");
            } else if (bodyEncoded(response.headers())) {
                logger.log("<-- END HTTP (encoded body omitted)");
            } else {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();

                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8);
                    } catch (UnsupportedCharsetException e) {
                        logger.log("");
                        logger.log("Couldn't decode the response body; charset is likely malformed.");
                        logger.log("<-- END HTTP");

                        return response;
                    }
                }

                if (!isPlaintext(buffer)) {
                    logger.log("");
                    logger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                    return response;
                }

                if (contentLength != 0) {
                    logger.log("");
                    logger.log(buffer.clone().readString(charset));
                }

                logger.log("<-- END HTTP (" + buffer.size() + "-byte body)");
            }
        }

        return response;
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                if (Character.isISOControl(readUtf8CodePoint(prefix))) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
//        String contentEncoding = headers.get("Content-Encoding");
//        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
        return false;
    }

    static final int REPLACEMENT_CHARACTER = '\ufffd';

    public static int readUtf8CodePoint(Buffer buffer) throws EOFException {
        if (buffer.size() == 0) throw new EOFException();

        byte b0 = buffer.getByte(0);
        int codePoint;
        int byteCount;
        int min;

        if ((b0 & 0x80) == 0) {
            // 0xxxxxxx.
            codePoint = b0 & 0x7f;
            byteCount = 1; // 7 bits (ASCII).
            min = 0x0;

        } else if ((b0 & 0xe0) == 0xc0) {
            // 0x110xxxxx
            codePoint = b0 & 0x1f;
            byteCount = 2; // 11 bits (5 + 6).
            min = 0x80;

        } else if ((b0 & 0xf0) == 0xe0) {
            // 0x1110xxxx
            codePoint = b0 & 0x0f;
            byteCount = 3; // 16 bits (4 + 6 + 6).
            min = 0x800;

        } else if ((b0 & 0xf8) == 0xf0) {
            // 0x11110xxx
            codePoint = b0 & 0x07;
            byteCount = 4; // 21 bits (3 + 6 + 6 + 6).
            min = 0x10000;

        } else {
            // We expected the first byte of a code point but got something else.
            buffer.skip(1);
            return REPLACEMENT_CHARACTER;
        }

        if (buffer.size() < byteCount) {
            throw new EOFException("size < " + byteCount + ": " + buffer.size()
                    + " (to read code point prefixed 0x" + Integer.toHexString(b0) + ")");
        }

        // Read the continuation bytes. If we encounter a non-continuation byte, the sequence consumed
        // thus far is truncated and is decoded as the replacement character. That non-continuation byte
        // is left in the stream for processing by the next call to readUtf8CodePoint().
        for (int i = 1; i < byteCount; i++) {
            byte b = buffer.getByte(i);
            if ((b & 0xc0) == 0x80) {
                // 0x10xxxxxx
                codePoint <<= 6;
                codePoint |= b & 0x3f;
            } else {
                buffer.skip(i);
                return REPLACEMENT_CHARACTER;
            }
        }

        buffer.skip(byteCount);

        if (codePoint > 0x10ffff) {
            return REPLACEMENT_CHARACTER; // Reject code points larger than the Unicode maximum.
        }

        if (codePoint >= 0xd800 && codePoint <= 0xdfff) {
            return REPLACEMENT_CHARACTER; // Reject partial surrogates.
        }

        if (codePoint < min) {
            return REPLACEMENT_CHARACTER; // Reject overlong code points.
        }

        return codePoint;
    }
}