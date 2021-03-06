package com.example.alarmme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private static final String ACTION_ID = "STANDUP_ACTION";
    private static final int NOTI_ID = 1;

    private ToggleButton button;
    private NotificationManager manager;
    private NotificationChannel channel;

    private AlarmReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);



        Intent intent = new Intent(this, AlarmReceiver.class);
        Boolean alarmup = (PendingIntent.getBroadcast(this, NOTI_ID, intent,PendingIntent.FLAG_NO_CREATE)!=null);
//        button.setChecked(alarmup);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, NOTI_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String ok = isChecked? getString(R.string.on) : getString(R.string.off);

                if (isChecked) {
//                    createChannel();
//                    deliverNotification(MainActivity.this);
                    if (alarmManager != null){
                        long ft = 10;
                        long realTime = SystemClock.elapsedRealtime() +ft;

                        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                realTime,
                                10,
                                pendingIntent);
                    }

                }else{
                    alarmManager.cancel(pendingIntent);
                    manager.cancelAll();
                }
                Toast.makeText(getApplicationContext(), ok, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createChannel(){
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(ACTION_ID, "Standup Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setDescription("Get up every 15 minute for your health.");
            channel.setLightColor(Color.RED);
            manager.createNotificationChannel(channel);
        }
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
}