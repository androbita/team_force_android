package com.app.salesapp.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.app.salesapp.log.SalesAppLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.app.salesapp.common.SalesAppConstant.LOG_TAG;

public class Util {

    public static final String DEFAULT_TIME_PATTERN = "MMM d, HH:mm";
    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String TAG = Util.class.getName();

    public static String formatDateFromAPI(String datetime, String outputPattern) {
        if (TextUtils.isEmpty(datetime))
            return "Date not available";

        if (outputPattern == null)
            outputPattern = DEFAULT_TIME_PATTERN;

        try {
            Date date = formatDateFromAPI(datetime);
            return new SimpleDateFormat(outputPattern).format(date);
        } catch (Exception e) {
            SalesAppLog.e(LOG_TAG, e);
        }
        return datetime;
    }

    public static Date formatDateFromAPI(String datetime) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_PATTERN);
        Date date = simpleDateFormat.parse(datetime);
        return date;
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static String getVersionName(Context context) throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        String version = pInfo.versionName;
        return version;
    }

   public static void movePhotoToGallery(Context context, String photofilePath, String name){
       File from = new File(photofilePath);
       File dir = new File(Environment.getExternalStoragePublicDirectory(
               Environment.DIRECTORY_PICTURES), "Teamforce");
       if (!dir.exists())
       {
           if(dir.mkdirs()){
               Log.e(TAG,"DIR CREATED");
           }else {
               Log.e(TAG,"DIR NOT CREATED");
           }
       }
        File newFile = new File(dir.getPath()+"/"+name);
       try {
           copyFile(context,from,newFile);
           refreshMedia(context,dir);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

    private static void refreshMedia(Context context,File destination) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f1 = new File("file://" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
            Uri contentUri = Uri.fromFile(f1);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        } else {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(destination)));
    }

    public static void copyFile(Context context,File src, File dst) throws IOException
    {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try
        {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        }
        finally
        {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }


        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, dst.getName());
        values.put(MediaStore.Images.Media.DESCRIPTION, "");
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis ());
        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, dst.toString().toLowerCase(Locale.US).hashCode());
        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, dst.getName().toLowerCase(Locale.US));
        values.put("_data", dst.getAbsolutePath());

        ContentResolver cr = context.getContentResolver();
        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }


    public static String getCurrentDateTime() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_PATTERN);
        return sdf.format(now);
    }
}
