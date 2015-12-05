package com.example.chen.cs160finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class StartPresentation extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_presentation);
        Intent intent = getIntent();
        final String message = intent.getStringExtra("Message");
        String lines[] = message.split("\\r?\\n");
        String title = lines[0];
        TextView preziTitle = (TextView)findViewById(R.id.preziTitle);
        preziTitle.setText(title);
        ImageButton startButton = (ImageButton) findViewById(R.id.greenbutton);
        ImageButton cancelButton = (ImageButton) findViewById(R.id.redbutton);
        startButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(StartPresentation.this, Presentation.class);
                intent.putExtra("Message", message); //propagate over the hour
                startActivity(intent);
            }
        });


    }



}
