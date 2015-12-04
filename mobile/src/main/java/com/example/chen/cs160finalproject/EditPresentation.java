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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.List;

public class EditPresentation extends AppCompatActivity {

    String presentation;
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

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int presentationNum = extras.getInt("presentation");
            loadPresentation(presentationNum);
        }
    }

    private void loadPresentation(int presentationNum) {
        try {
            String FILE_NAME = MainActivity.FILE_NAME_BASE + presentationNum;
//            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_APPEND);
//            fos.write(("kek" + "\n").getBytes());
//            fos.close();

            FileInputStream fis = openFileInput(FILE_NAME);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((fis)));
            String line = "";
            StringBuffer buffer = new StringBuffer();
            while((line = bufferedReader.readLine()) != null) {
                buffer.append(line + "\n");
            }
//            String total = "";
            String[] presentation = buffer.toString().split("\n");
//            for(int i = 0; i < presentation.length; i++) {
//                total = total + presentation[i] + "\n";
//            }
//            Toast.makeText(getApplicationContext(), total, Toast.LENGTH_LONG).show();
            if(presentation.length > 1) {
                loadKeywords(presentation, 1);
            }
            fis.close();
        }catch(Exception e) {
            Toast.makeText(getApplicationContext(), "Error reading: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /* presentation array will look like:
     0 [name of presentation]
     1 [integer representing number of slides, say x]
     --x blocks of stuff--, each block with the format:
        [integer representing lines of keywords, say k]
        --k lines of keywords--
        one line representing the time assigned to the slide

     */
    private void loadKeywords(String[]presentation, int slideNumber) {
        int lineCounter = 2;
        int numKeywords;
        for(int i = 0; i < slideNumber - 1; i++) {
            numKeywords = Integer.parseInt(presentation[lineCounter]);
            lineCounter += numKeywords + 1;
        }
        numKeywords = Integer.parseInt(presentation[lineCounter]);
        switch(numKeywords) {
            case 1: keyword1Input.setText(presentation[lineCounter + 1]); break;
            case 2: keyword1Input.setText(presentation[lineCounter + 1]);
                    keyword2Input.setText(presentation[lineCounter + 2]); break;
            case 3: keyword1Input.setText(presentation[lineCounter + 1]);
                    keyword2Input.setText(presentation[lineCounter + 2]);
                    keyword3Input.setText(presentation[lineCounter + 3]); break;
            default: break;
        }
    }
}
