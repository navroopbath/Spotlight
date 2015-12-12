package com.example.chen.cs160finalproject;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;


public class PhoneListenerService extends WearableListenerService {
    private static final String START_ACTIVITY = "/end_activity";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("PhoneListenerService", "Received");
        if( messageEvent.getPath().equalsIgnoreCase( START_ACTIVITY ) ) {
            Log.d("PhoneListenerService", "Received2");
            String value = new String(messageEvent.getData());
            Log.d("PhoneListenerService", value);

            //The value has a format of "timeForFirstSlide timeForSecondSlide timeForThirdSlide..."
            if(value.equals("NoData")) {
                Intent goToMain = new Intent(this, MainActivity.class);
                goToMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(goToMain);
            } else {
                Intent goToStats = new Intent(this, StatsActivity.class);
                goToStats.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                goToStats.putExtra("data", value);
                startActivity(goToStats);
            }
        } else {
            super.onMessageReceived( messageEvent );
        }
    }
}
