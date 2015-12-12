package com.example.chen.cs160finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class StartPresentation extends Activity {
    double letterWidth = 12.5;
    int halfWidthOfScreen = 110 - 8;
    TextView preziTitle;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_presentation);
        Intent intent = getIntent();
        final String message = intent.getStringExtra("Message");
       // final String message = "CS160" + '\n' + "20 " + "B "+"A "+"c"+'\n'+"30 "+"haha "+"hehe "+"heihei";
        String lines[] = message.split("\\r?\\n");
        String title = lines[0];
        preziTitle = (TextView)findViewById(R.id.preziTitle);
        //preziTitle.setText("VC Pitch");
        preziTitle.setText(paddingString(title));
        Log.d("i is", title);
        ImageButton startButton = (ImageButton) findViewById(R.id.greenbutton);
        ImageButton cancelButton = (ImageButton) findViewById(R.id.redbutton);
        startButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(StartPresentation.this, Presentation.class);
                long currTime = System.currentTimeMillis();
                intent.putExtra("Message", message); //propagate over the hour
                intent.putExtra("StartTime", currTime);
                startActivity(intent);
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
            }
        });

    }
    private String paddingString(String msg) {
        int length = msg.length();
        int numOfSpace = (int)((double)halfWidthOfScreen/letterWidth) - (length + 1)/2;
        Log.d("num of space: ", String.valueOf(numOfSpace));
        String rtn = "";
        for (int i = 0; i < numOfSpace; i += 1) {
            rtn += " ";
        }
        rtn += msg;
        return rtn;
    }



}
