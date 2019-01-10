package com.app.salesapp.common;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.app.salesapp.log.SalesAppLog;

import java.util.Locale;

public class DeviceUtil {
    public static String getAppVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            SalesAppLog.e(SalesAppConstant.LOG_TAG, e);
            return "";
        }
    }

    public static String getPackageName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            SalesAppLog.e(SalesAppConstant.LOG_TAG, e);
            return "";
        }
    }

//    public static String getDeviceIMEI(Context context) {
//        try {
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//                return tm.getDeviceId();
//            }
//        } catch (Exception e) {
//            SalesAppLog.e(SalesAppConstant.LOG_TAG, e);
//            return "";
//        }
//        return "";
//    }

    public static String getUniqueId(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            SalesAppLog.e(SalesAppConstant.LOG_TAG, e);
            return "NA";
        }
    }

    public static String getDeviceLocal() {
        return Locale.getDefault().getLanguage();
    }

    public static String getPlayServicesVersion(Context context) {
        try {
            int versionCode = context.getPackageManager().getPackageInfo("com.google.android.gms", 0).versionCode;
            Log.d(SalesAppConstant.LOG_TAG, "Google Play Services version " + versionCode + " installed");
            return Integer.toString(versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            SalesAppLog.e(SalesAppConstant.LOG_TAG, e);
            return "-1";
        }
    }
}
