package com.weye.secondquizandroid;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.app.ActivityManager.RunningServiceInfo;

public class NotificationService  extends Service {
    NotificationCompat.Builder mNotifyBuilder;
    NotificationManager notificationManager;

    Boolean checkNoti;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //check foreground running or not running
         checkNoti = isServiceRunningInForeground(this, NotificationService.class);
        //Log.d("GIA TRI CHAY NGAM", String.valueOf(checkNoti));

        //if foreground not running, then start foreground service notification
        if (checkNoti == false) {
            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 100,
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            //config the notification
            mNotifyBuilder = new NotificationCompat.Builder(
                    this).setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("OPERR Driver")
                    .setContentText("You are on the break")
                    .setOngoing(true)
                    .setAutoCancel(true).setWhen(System.currentTimeMillis() + 10 * 1000)
                    .setContentIntent(pendingIntent);

            //create countdown timer
            new CountDownTimer(300000, 1000) {

                public void onTick(long millisUntilFinished) {

                    String text = String.format(Locale.getDefault(), "Time left: %02d : %02d ",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                    //update content of notification
                    mNotifyBuilder.setContentText(text);
                    notificationManager.notify(12345, mNotifyBuilder.build());
                }

                public void onFinish() {
                    //  textView.setText("done!");
                    //if countdown to zero, remove notification
                    notificationManager.cancelAll();
                    stopForeground(true);
                }

            }.start();

            //start foreground notification
            startForeground(1, mNotifyBuilder.build());


        }

        return START_NOT_STICKY;
    }

    //check foreground running or not running
    public static boolean isServiceRunningInForeground(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }

            }
        }
        return false;
    }





}
