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

public class SignalWatch extends Service {

    private static final int INTERVAL = 300000;
    private static final int SECOND = 1000;

    private GoogleApiClient mApiClient;


    /* TIME API DETAILS */
    private String mUrlString = "http://www.timeapi.org/pdt/now";
    private String mTimeResponse = "";
    private static final String START_ACTIVITY = "/start_activity";



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
        createAndStartTimer();
        return START_STICKY;
    }


    private void createAndStartTimer() {
        /* Ask the Time API "What time is it?" at the end of INTERVAL.
         * ----------------------
         * Time API : GET http://www.timeapi.org/pdt/now
         * Returns text e.g. 2015-10-08T19:33:33-07:00
         *
         */
        String message = "CS160";
        message += '\n';
        message += "20";
        message += "B ";
        message += "A ";
        message += "c";
        message += '\n';
        message += "30";
        message += "haha ";
        message += "hehe ";
        message += "heihei";
        mApiClient.connect(); //connect to the API client to send a message!
        sendMessage(START_ACTIVITY, message); //actually send the message to the watch
                            //pass extra information of the hour (as a string)

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    //How to send a message to the WatchListenerService
    private void sendMessage( final String path, final String text ) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                for(Node node : nodes.getNodes()) {
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