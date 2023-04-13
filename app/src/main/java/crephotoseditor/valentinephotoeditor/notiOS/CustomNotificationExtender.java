package crephotoseditor.valentinephotoeditor.notiOS;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;
import com.u.securekeys.SecureEnvironment;
import com.u.securekeys.annotation.SecureKey;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import crephotoseditor.valentinephotoeditor.R;


public class CustomNotificationExtender extends NotificationExtenderService {

    public static int notificationCount = 1;
    private NotificationHelper notificationHelper;
    @SecureKey(key = "api_main_x", value = "http://securetechnoweb.com/rr/images/")
    String bannerUrl = SecureEnvironment.getString("api_main_x");

    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult notification) {
        JSONObject data = notification.payload.additionalData;

        if (data != null) {
            try {
                GCMModel objGcmModel = new GCMModel();
                objGcmModel.setMessage(data.getString("message"));
                if (data.getString("image_url") != null && !data.getString("image_url").equals("")) {
                    objGcmModel.setBanner(bannerUrl + data.getString("image_url"));
                }
                objGcmModel.setIcon(data.getString("icon"));
                objGcmModel.setPlaylink(data.getString("play_link"));
                objGcmModel.setAppname(data.getString("app_name"));
                objGcmModel.setPriority(notificationCount++);
                sendNotificationLow(objGcmModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void sendNotificationLow(GCMModel objGcmModel) {
        new MyAsyncTask(objGcmModel).execute();
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }

    class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
        GCMModel objGcmModel;
        NotificationManager notificationManager;
        NotificationCompat.Builder notificationBuilder;
        Notification.Builder notificationBuilder1;

        public MyAsyncTask(GCMModel objGcmModel) {
            this.objGcmModel = objGcmModel;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
                Bitmap bitmap = getBitmapFromURL(objGcmModel.getIcon());
                if (bitmap != null) {
                    remoteViews.setImageViewBitmap(R.id.apkicon, bitmap);
                    remoteViews.setViewVisibility(R.id.apkicon, View.VISIBLE);
                } else {
                    remoteViews.setViewVisibility(R.id.apkicon, View.GONE);
                }

                remoteViews.setTextViewText(R.id.apkname, objGcmModel.getAppname());
                remoteViews.setTextViewText(R.id.message, objGcmModel.getMessage());
                RemoteViews remoteViews1 = null;
                if (objGcmModel.getBanner() != null && !objGcmModel.getBanner().equals("")) {
                    Bitmap banner = getBitmapFromURL(objGcmModel.getBanner());
                    if (banner != null) {
                        remoteViews1 = new RemoteViews(getPackageName(), R.layout.notification_banner_layout);

                        remoteViews1.setImageViewBitmap(R.id.apkicon, bitmap);
                        remoteViews1.setImageViewBitmap(R.id.ivBanner, banner);

                        remoteViews1.setTextViewText(R.id.apkname, objGcmModel.getAppname());
                        remoteViews1.setTextViewText(R.id.message, objGcmModel.getMessage());
                    }
                }
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(objGcmModel.getPlaylink()));

                PendingIntent pendingIntent = PendingIntent.getActivity(CustomNotificationExtender.this, objGcmModel.getPriority(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationHelper = new NotificationHelper(getApplicationContext());
                    notificationBuilder1 = notificationHelper.getNotification1(remoteViews, remoteViews1, pendingIntent, defaultSoundUri);
                } else {
                    notificationBuilder = new NotificationCompat.Builder(CustomNotificationExtender.this)
                            .setSmallIcon(R.mipmap.download)
                            .setTicker(getString(R.string.app_name))
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setContent(remoteViews);
                    if (remoteViews1 != null) {
                        notificationBuilder.setCustomBigContentView(remoteViews1);
                    }
                }
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("oneSignal", " " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bmp) {
            super.onPostExecute(bmp);
            if (notificationManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationManager.notify(objGcmModel.getPriority(), notificationBuilder1.build());
                } else {
                    notificationManager.notify(objGcmModel.getPriority(), notificationBuilder.build());
                }
            }
        }
    }
}
