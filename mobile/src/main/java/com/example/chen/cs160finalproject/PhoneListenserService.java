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
            //The value has a format of "timeForFirstSlide timeForSecondSlide timeForThirdSlide..."

            //Do what ever you want with the value

        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}
