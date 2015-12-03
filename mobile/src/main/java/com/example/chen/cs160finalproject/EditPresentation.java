package com.example.chen.cs160finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditPresentation extends AppCompatActivity {

    EditText titleEditText;
    Button startButton;
    EditText keyword1Input;
    EditText keyword2Input;
    EditText keyword3Input;
    Button addSlideButton;
    Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_presentation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        titleEditText = (EditText) findViewById(R.id.editText);
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToProgress = new Intent(EditPresentation.this, PresentationInProgress.class);
                startActivity(goToProgress);
            }
        });
        keyword1Input = (EditText) findViewById((R.id.keyword1Input));
        keyword2Input = (EditText) findViewById(R.id.keyword2Input);
        keyword3Input = (EditText) findViewById(R.id.keyword3Input);
        addSlideButton = (Button) findViewById(R.id.addSlideButton);
        deleteButton = (Button) findViewById(R.id.deleteSlideButton);
    }

}
