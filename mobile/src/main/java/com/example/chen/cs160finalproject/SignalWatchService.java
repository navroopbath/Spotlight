package com.example.chen.cs160finalproject;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

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

public class SignalWatchService extends Service {
    private GoogleApiClient mApiClient;
    private String mUrlString = "http://www.timeapi.org/pdt/now";
    private String mTimeResponse = "";
    private static final String START_ACTIVITY = "/start_activity";
    private static final int INTERVAL = 300000;
    private static final int SECOND = 1000;

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
        CountDownTimer timer = new CountDownTimer(INTERVAL, SECOND) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        URL url;
                        try {
                            url = new URL(mUrlString);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            return;
                        }

                        HttpURLConnection urlConnection = null;
                        try {
                            urlConnection = (HttpURLConnection) url.openConnection();
                            urlConnection.connect();
                            InputStream in = urlConnection.getInputStream();
                            Scanner scanner = new Scanner(in);
                            //Scanners scan line by line: if your response is longer than 1 line, you need a loop
                            mTimeResponse = scanner.nextLine(); //parses the GET request into a string
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (urlConnection != null) {
                                urlConnection.disconnect();
                            }
                        }
                        if (!mTimeResponse.equals("")) {
                            String[] date_time = mTimeResponse.split("T"); //an example string: 2015-10-08T15:17:23-07:00

                            String hour = date_time[1].substring(0,2); //extract the hour (xx)
                            Log.d("String", "The hour is: " + hour);
                            mApiClient.connect(); //connect to the API client to send a message!
                            sendMessage(START_ACTIVITY, hour); //actually send the message to the watch
                            //pass extra information of the hour (as a string)
                        }

                    }
                }).start();

                // Start the timer again
                createAndStartTimer();
            }
        };

        timer.start();
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
