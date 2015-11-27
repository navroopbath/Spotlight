package com.example.chen.presentationproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

public class statsPrezi extends AppCompatActivity {

    private ImageButton statsImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_stats_prezi);
        statsImage = (ImageButton) findViewById(R.id.statspageone);
        Intent statIntent = getIntent();
    }

}
