package com.example.chen.cs160finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class EndPresentation extends Activity {

    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    ImageButton bgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_presentation);
        Intent intent = getIntent();
        String message = intent.getStringExtra("toMobile");
        bgButton = (ImageButton)findViewById(R.id.bgbutton);
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        bgButton.setOnTouchListener(gestureListener);

        Intent sendStatsBackService = new Intent(this, SendStatsBackService.class);
        sendStatsBackService.putExtra("data", message);
        startService(sendStatsBackService);

        Log.e("EndPresentation", message);
    }

//    private GoogleApiClient getGoogleApiClient(Context context) {
//        return new GoogleApiClient.Builder(context).addApi(Wearable.API).build();
//    }

//    private void retrieveDeviceNode() {
//        final GoogleApiClient client = getGoogleApiClient(this);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS);
//                NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes(client).await();
//                List<Node> nodes = result.getNodes();
//                if(nodes.size() > 0) {
//                    nodeId = nodes.get(0).getId();
//                }
//                client.disconnect();
//            }
//        }).start();
//    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent ev) {
            setContentView(R.layout.activity_main);
            return true;
        }
    }
}
