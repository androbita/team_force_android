package com.app.salesapp.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.main.MainActivity;
import com.app.salesapp.user.UserService;

import java.util.Date;
import java.util.Map;

import javax.inject.Inject;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.app.salesapp.service.Constants.IS_NOTIFICATION;

public class CommentCommand extends FCMCommand {

    @Inject
    public UserService userService;

    @Override
    void executeMessage(Context context, Map<String, String> data) {
        ((SalesApp) context.getApplicationContext()).getSalesAppDeps().inject(this);
        userService.addNotification(new NotificationModel(new Date(), data.get("message"), true, data.get("type"), data.get("timeline_id")));

        int id = 0;
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(IS_NOTIFICATION, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notify = new NotificationCompat.Builder(context)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Sales Club")
                .setContentText(data.get("message"))
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_notification_screen)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_notification_screen))
                .setTicker(data.get("title"))
                .setLights(Color.RED, 3000, 3000)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setVibrate(new long[] { 1000, 1000 })
                .setContentIntent(pIntent);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT < 16) {
            /*build notification for HoneyComb to ICS*/
            notificationManager.notify(id, notify.getNotification());
        }
        if (Build.VERSION.SDK_INT > 15) {
            /*Notification for Jellybean and above*/
            notificationManager.notify(id, notify.build());
        }

    }
}
