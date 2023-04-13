package crephotoseditor.valentinephotoeditor.notiOS;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

import crephotoseditor.valentinephotoeditor.R;


public class NotificationHelper extends ContextWrapper {
    private NotificationManager notifManager;
    public static final String CHANNEL_ONE_ID = "crephotoseditor.valentinephotoeditor.ONE";
    public static final String CHANNEL_ONE_NAME = "Channel One";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME, notifManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setShowBadge(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(notificationChannel);
    }

    private NotificationManager getManager() {
        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notifManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotification1(RemoteViews remoteViews, RemoteViews remoteView1, PendingIntent pendingIntent, Uri defaultSoundUri) {
        if (remoteView1 != null) {
            return new Notification.Builder(getApplicationContext(), CHANNEL_ONE_ID)
                    .setSmallIcon(R.mipmap.download)
                    .setTicker(getString(R.string.app_name))
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setContent(remoteViews)
                    .setCustomBigContentView(remoteView1);
        } else {
            return new Notification.Builder(getApplicationContext(), CHANNEL_ONE_ID)
                    .setSmallIcon(R.mipmap.download)
                    .setTicker(getString(R.string.app_name))
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setContent(remoteViews);

        }
    }
}