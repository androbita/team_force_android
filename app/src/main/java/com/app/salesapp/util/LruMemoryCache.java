package com.app.salesapp.util;

import android.support.annotation.NonNull;
import android.util.LruCache;

public class LruMemoryCache {
    private static final int CACHE_SIZE = 1024 * 2;//2MB
    static LruCache cache = new LruCache(CACHE_SIZE);

    public static void put(@NonNull String key, @NonNull Object object) {
        cache.put(key, object);
    }

    public static Object get(@NonNull String key) {
        return cache.get(key);
    }
}
