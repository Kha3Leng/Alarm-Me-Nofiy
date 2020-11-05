package com.example.alarmme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.widget.ToggleButton;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String ACTION_ID = "STANDUP_ACTION";
    private static final int NOTI_ID = 1;

    private NotificationManager manager;
    private NotificationChannel channel;

    @Override
    public void onReceive(Context context, Intent intent) {
        createChannel(context);
        deliverNotification(context);
    }

    public void deliverNotification(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTI_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ACTION_ID)
                .setSmallIcon(R.drawable.ic_run)
                .setContentText("Get up every 15 minutes for your health")
                .setContentTitle("Stand Up!")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        manager.notify(NOTI_ID, builder.build());

    }

    public void createChannel(Context context){
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(ACTION_ID, "Standup Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setDescription("Get up every 15 minute for your health.");
            channel.setLightColor(Color.RED);
            manager.createNotificationChannel(channel);
        }
    }
}