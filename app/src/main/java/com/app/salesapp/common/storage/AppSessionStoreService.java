package com.app.salesapp.common.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.app.salesapp.log.SalesAppLog;
import com.app.salesapp.util.LruMemoryCache;
import com.google.gson.Gson;

import static com.app.salesapp.common.SalesAppConstant.LOG_TAG;

public class AppSessionStoreService {
    private static final String APP_SESSION_PREFERENCE_KEY = "com.app.salesapp.common.storage.AppSessionStoreService";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public AppSessionStoreService(Context context, Gson gson) {
        this.gson = gson;
        sharedPreferences = context.getSharedPreferences(APP_SESSION_PREFERENCE_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    public void putObject(String key, Object object) {
        if (object == null || TextUtils.isEmpty(key)) {
            Log.e(LOG_TAG, "Object or Key is empty, Ignoring store");
            return;
        }
        LruMemoryCache.put(key, object);
        sharedPreferences.edit().putString(key, gson.toJson(object)).apply();
    }

    public <T> T getObject(String key, Class<T> tClass) {
        Object cachedValue = LruMemoryCache.get(key);
        if (cachedValue != null)
            return (T) cachedValue;

        String value = sharedPreferences.getString(key, null);
        if (value == null) {
            return null;
        } else {
            try {
                return gson.fromJson(value, tClass);
            } catch (Exception e) {
                SalesAppLog.e(LOG_TAG, e);
                return null;
            }
        }
    }
}
