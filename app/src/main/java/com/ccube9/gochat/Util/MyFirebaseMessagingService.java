package com.ccube9.gochat.Util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ccube9.gochat.Home.Fragment.NotificationActivity;
import com.ccube9.gochat.News.Activity.NewsFunctions;
import com.ccube9.gochat.R;
import com.ccube9.gochat.Splash.SplashActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FireBaseMessagingService";
    public static String NOTIFICATION_CHANNEL_ID = "com.itw.firebasepushnotificationdemo";
    public static final int NOTIFICATION_ID = 1;


    @Override
    public void onNewToken(String s) {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                String token =  task.getResult().getToken();
                Log.e("gettoken",token);

                PrefManager.setFCMToken(MyFirebaseMessagingService.this,token);
            }
        });
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("notificationlog", String.valueOf(remoteMessage.getData()));
        Map data = remoteMessage.getData();
//        Log.e(TAG, "Message Notification title: " + data.get("title"));
//       / Log.e(TAG, "Message Notification message: " + data.get("message"));
        String title  = String.valueOf(data.get("notification")),
                message  = "";//String.valueOf(data.get("msg"));
      //  showNotification(getBaseContext(),title, message);
shownot(getBaseContext(),title,message);
    }

    private void shownot(Context context, String title, String message) {

            Intent ii;
            ii = new Intent(context, NewsFunctions.class);
            ii.setData((Uri.parse("custom://" + System.currentTimeMillis())));
            ii.setAction("actionstring" + System.currentTimeMillis());
            ii.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pi = PendingIntent.getActivity(context, 0, ii,PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                        .setOngoing(true)
                        .setSmallIcon(getNotificationIcon())
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setContentIntent(pi)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(Notification.CATEGORY_SERVICE)
                        .setWhen(System.currentTimeMillis())
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentTitle(title)
                        .addAction(R.mipmap.ic_launcher, "More", pi)
                        .addAction(R.mipmap.ic_launcher, "And more", pi).build();

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                        Context.NOTIFICATION_SERVICE);
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,title, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(notificationChannel);
                notificationManager.notify(NOTIFICATION_ID, notification);
            }
            else{
                notification = new NotificationCompat.Builder(this)
                        .setSmallIcon(getNotificationIcon())
                        .setAutoCancel(true)
                        .setContentText(message)
                        .setContentIntent(pi)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentTitle(title).build();
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                        Context.NOTIFICATION_SERVICE);
                notificationManager.notify(NOTIFICATION_ID, notification);
            }

    }


    private void showNotification(Context context, String title, String message) {
        Intent ii;
        ii = new Intent(context, NewsFunctions.class);
        ii.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        ii.setAction("actionstring" + System.currentTimeMillis());
        ii.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pi = PendingIntent.getActivity(context, 0, ii,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
                    .setOngoing(true)
                    .setSmallIcon(getNotificationIcon())
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pi)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setWhen(System.currentTimeMillis())
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentTitle(title).build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                    Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,title, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
        else{
            notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(getNotificationIcon())
                    .setAutoCancel(true)
                    .setContentText(message)
                    .setContentIntent(pi)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentTitle(title).build();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                    Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }
}
