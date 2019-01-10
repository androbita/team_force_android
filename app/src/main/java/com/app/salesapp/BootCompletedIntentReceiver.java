package com.app.salesapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.salesapp.Gps.LocationBackgroundService;

/**
 * Created by Nugraha ROG on 2/22/2018.
 */

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent pushIntent = new Intent(context, LocationBackgroundService.class);
            context.startService(pushIntent);
        }
    }
}
