package com.example.chen.cs160finalproject;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;


public class PhoneListenserService extends WearableListenerService {
    private static final String START_ACTIVITY = "/end_activity";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if( messageEvent.getPath().equalsIgnoreCase( START_ACTIVITY ) ) {
            String value = new String(messageEvent.getData());
            //Intent intent = new Intent(this, ClockActivity.class );
            //intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            //you need to add this flag since you're starting a new activity from a service
            //intent.putExtra("Hour", value); //propagate over the hour
            //startActivity(intent);
        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}
