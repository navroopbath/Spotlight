package com.example.chen.cs160finalproject;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/*
    1. Services need to be defined in the manifest.
     <service
            android:name=".TimeWatcherService"
            android:label="@string/app_name"/>
*/

public class SendStatsBackService extends Service {
    private GoogleApiClient mApiClient;
    private static final String START_ACTIVITY = "/end_activity";

    @Override
    public void onCreate() {
        super.onCreate();

        /* Initialize the googleAPIClient for message passing */
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        /* Successfully connected */
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        /* Connection was interrupted */
                    }
                })
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Kick off new work to do
        sendDataToMobile(intent);
        return START_STICKY;
    }


    private void sendDataToMobile(Intent intent) {
        String message = intent.getStringExtra("data");
        Log.d("SendStatsBackService", message);
        mApiClient.connect(); //connect to the API client to send a message!
        sendMessage(START_ACTIVITY, message); //actually send the message to the watch
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    //How to send a message to the WatchListenerService
    private void sendMessage( final String path, final String text ) {
        Log.d("SendStatsBackService2", text);
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    Log.d("SendStatsBackService3", text);
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes() ).await();
                }
            }
        }).start();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }
}