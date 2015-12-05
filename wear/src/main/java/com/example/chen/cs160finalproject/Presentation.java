package com.example.chen.cs160finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

import java.util.ArrayList;

public class Presentation extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        Intent intent = getIntent();
        final String message = intent.getStringExtra("Message");
        ArrayList<String[]> slides = parseMessage(message);
    }
    /* Assume the message in the form "second keyword1 keyword2 keyword3 \n second ... ... ..." */
    private ArrayList<String[]> parseMessage(String message){
        ArrayList<String[]> rtn = new ArrayList<String[]>();
        String[] addon = new String[4];
        addon[0] = "50";
        addon[1] = "Hello";
        addon[2] = " World";
        addon[3] = " haha";
        rtn.add(addon);

        /*String lines[] = message.split("\\r?\\n");
        for (int i = 1; i < lines.length; i += 1){
            String slide = lines[0];
            // Content[0] = time in seconds; Content[1] = keyword 1; Content[2] = keyword 2; Content[3] = keyword 3
            String content[] = slide.split("\\s+");
            rtn.add(content);
        }*/
        return rtn;
    }
}
