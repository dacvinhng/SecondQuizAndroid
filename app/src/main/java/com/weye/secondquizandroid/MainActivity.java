package com.weye.secondquizandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class MainActivity extends Activity {

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        //after open app, it will call service starting notification if notification not running
        Intent serviceIntent = new Intent(this, NotificationService.class);
        //serviceIntent.setAction(NotificationService.ACTION_START_FOREGROUND_SERVICE);
        //startService(serviceIntent);
        ContextCompat.startForegroundService(this, serviceIntent);
    }



}
