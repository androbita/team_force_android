package com.app.salesapp.log;

import android.util.Log;

public class SalesAppLog {
    public static void e(String tag, Throwable tr) {
        Log.e(tag, tr != null ? tr.getMessage() : "Exception is null", tr);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
    }
}
