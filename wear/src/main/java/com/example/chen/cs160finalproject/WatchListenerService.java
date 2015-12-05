package com.example.chen.cs160finalproject;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;


public class WatchListenerService extends WearableListenerService {
    private static final String START_ACTIVITY = "/start_activity";

    @Override
    /* Assume the message received in the form "time keyword1 keyword2 keyword3 \n time ... ... ..."*/
    public void onMessageReceived(MessageEvent messageEvent) {
        if( messageEvent.getPath().equalsIgnoreCase( START_ACTIVITY ) ) {
            String value = new String(messageEvent.getData());
            Intent intent = new Intent(this, StartPresentation.class );
            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra("Message", value); //propagate over the hour
            startActivity(intent);
        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}
