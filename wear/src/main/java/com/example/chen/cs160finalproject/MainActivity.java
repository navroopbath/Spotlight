package com.example.chen.cs160finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            TimeUnit.SECONDS.sleep(5);
        }catch (InterruptedException e) {

        }
        setContentView(R.layout.activity_start_presentation);
    }
}
