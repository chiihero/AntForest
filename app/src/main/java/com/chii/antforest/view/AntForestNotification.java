package com.chii.antforest.view;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

public class AntForestNotification {
    private static final int ANTFOREST_NOTIFICATION_ID = 46;
    private static NotificationManager mNotifyManager;
    private static final String CHANNEL_ID = "com.chii.antforest.ANTFOREST_NOTIFY_CHANNEL";
    private static Notification mNotification;
    private static Notification.Builder builder;
    private static boolean started = false;

    public static void start(Context context) {
        initNotification(context);
        if (!started) {
            if (context instanceof Service) {
                ((Service) context).startForeground(ANTFOREST_NOTIFICATION_ID, mNotification);
            } else {
                getNotificationManager(context).notify(ANTFOREST_NOTIFICATION_ID, mNotification);
            }
            started = true;
        }
    }

    public static void setContentText(CharSequence cs) {
        if (started && mNotifyManager != null) {
            mNotification = builder.setContentText(cs).build();
            mNotifyManager.notify(ANTFOREST_NOTIFICATION_ID, mNotification);

        }
    }

    public static void stop(Context context, boolean remove) {
        if (started) {
            if (context instanceof Service) {
                ((Service) context).stopForeground(remove);
            } else {
                getNotificationManager(context).cancel(ANTFOREST_NOTIFICATION_ID);
            }
            started = false;
        }
    }

    private static void initNotification(Context context) {
        if (mNotification == null) {
            Intent it = new Intent(Intent.ACTION_VIEW);
            it.setData(Uri.parse("alipays://platformapi/startapp?appId=60000002"));
            PendingIntent pi = PendingIntent.getActivity(context, 0, it,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                        "AntForest能量提醒", NotificationManager.IMPORTANCE_LOW);
                notificationChannel.enableLights(false);
                notificationChannel.enableVibration(false);
                notificationChannel.setShowBadge(false);
                getNotificationManager(context).createNotificationChannel(notificationChannel);
                builder = new Notification.Builder(context, CHANNEL_ID);
            } else {
                getNotificationManager(context);
                builder = new Notification.Builder(context)
                        .setPriority(Notification.PRIORITY_LOW);
            }

            mNotification = builder
                    //设置通知左侧的小图标
                    .setSmallIcon(android.R.drawable.sym_def_app_icon)
                    //设置通知内容
                    .setContentTitle("AntForest")
                    //设置通知内容
                    .setContentText("开始检测能量")
                    //设置点击通知后不删除通知
                    .setAutoCancel(false)
                    //设置点击通知时的响应事件
                    .setContentIntent(pi)
                    .build();
        }
    }

    private static NotificationManager getNotificationManager(Context context) {
        if (mNotifyManager == null) {
            mNotifyManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mNotifyManager;
    }

}
