package com.example.chen.cs160finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import java.util.ArrayList;

public class StartPresentation extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_presentation);
        Intent intent = getIntent();
        String message = intent.getStringExtra("Message");
    }
    /* Assume the message in form  time keyword1 keyword2 keyword3 \n time ... ... ...*/
    private ArrayList<String[]> parseMessage(String Message){
        ArrayList<String[]> rtn = new ArrayList<String[]>();
        return rtn;
    }

}
