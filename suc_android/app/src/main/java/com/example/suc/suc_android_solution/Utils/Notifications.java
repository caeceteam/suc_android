package com.example.suc.suc_android_solution.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.suc.suc_android_solution.R;

/**
 * Created by efridman on 15/10/17.
 */

public class Notifications {
    private static final int FORGOT_PASSWORD_NOTIFICATION_ID = 11111;
    public static void sendForgotPasswordNotification(Context context) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_menu_send)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.forgot_password_notification_title))
                .setContentText(context.getString(R.string.forgot_password_mail_notification))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.forgot_password_mail_notification)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(FORGOT_PASSWORD_NOTIFICATION_ID, notificationBuilder.build());

    }

    private static Bitmap largeIcon(Context context){
        Resources resources = context.getResources();
        return BitmapFactory.decodeResource(resources, R.drawable.ic_menu_send);
    }
}
