package com.app.salesapp.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SalesAppPreference {
    public static final String SALES_APP_PREF = "SalesAppPref";
    private static SalesAppPreference sinPref;
    private SharedPreferences sharedPreference;

    public SalesAppPreference(SharedPreferences sharedPreferences) {
        this.sharedPreference = sharedPreferences;
    }

    public static SalesAppPreference getInstance(Context context) {
        if (sinPref == null)
            sinPref = new SalesAppPreference(context.
                    getSharedPreferences(SALES_APP_PREF, Context.MODE_PRIVATE));
        return sinPref;
    }

    public String getString(String key, String defValue) {
        return sharedPreference.getString(key, defValue);
    }

    public int getInt(String key, int defValue) {
        return sharedPreference.getInt(key, defValue);
    }


    public String setString(String key, String value) {
        sharedPreference.edit().putString(key, value).apply();
        return key;
    }

    public void setInt(String key, int value) {
        sharedPreference.edit().putInt(key, value).apply();
    }

    public void setBoolean(String key, boolean value) {
        sharedPreference.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreference.getBoolean(key, defValue);
    }

    public void setLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key, long defValue) {
        return sharedPreference.getLong(key, defValue);
    }

    public void remove(String key) {
        sharedPreference.edit().remove(key).apply();
    }

    public void clear() {
        sharedPreference.edit().clear().commit();
    }
}
