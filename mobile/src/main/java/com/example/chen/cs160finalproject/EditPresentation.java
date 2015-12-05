package com.example.chen.cs160finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditPresentation extends AppCompatActivity {

    int presentationNum;
    int slideNum;
    String[]presentationData;
    List<String>presentationDataList;
    TextView presentationTitle;
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
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_white, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMenu = new Intent(EditPresentation.this, MainActivity.class);
                startActivity(goToMenu);
            }
        });
        slideNum = 1;
        presentationTitle = (TextView) findViewById(R.id.presentationTitle);
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
        addSlideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeKeywords(presentationData, presentationNum, slideNum);
            }
        });

        deleteButton = (Button) findViewById(R.id.deleteSlideButton);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            presentationNum = extras.getInt("presentation");
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
            presentationData = buffer.toString().split("\n");
            presentationDataList = Arrays.asList(presentationData);
//            for(int i = 0; i < presentation.length; i++) {
//                total = total + presentation[i] + "\n";
//            }
//            Toast.makeText(getApplicationContext(), total, Toast.LENGTH_LONG).show();
            if(presentationData.length > 1) {
                loadKeywords(presentationData, 1);
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
            lineCounter += numKeywords + MainActivity.EXTRA_LINES + 1;
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

    /* presentation array will look like:
     0 [name of presentation]
     1 [integer representing number of slides, say x]
     --x blocks of stuff--, each block with the format:
        [integer representing lines of keywords, say k]
        --k lines of keywords--
        one line representing the time assigned to the slide

     */
    private void storeKeywords(String[] presentation, int presentationNum, int slideNumber) {
        try {
            String FILE_NAME = MainActivity.FILE_NAME_BASE + presentationNum;

            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            List<String> presentationDataList = new ArrayList<>();
            for(int i = 0; i < presentation.length; i++) {
                presentationDataList.add(presentation[i]);
            }
//            for(int i = 0; i < presentationDataList.size(); i++) {
//                Log.e("keke1", presentationDataList.get(i));
//            }
            int lineCounter = 2;
            int numKeywords;
            for (int i = 0; i < slideNumber - 1; i++) {
                numKeywords = Integer.parseInt(presentation[lineCounter]);
                lineCounter += numKeywords + MainActivity.EXTRA_LINES + 1;
            }
            numKeywords = Integer.parseInt(presentation[lineCounter]);
            //Log.e("keke2", numKeywords+"");
            for (int i = 0; i < numKeywords + MainActivity.EXTRA_LINES + 1; i++) {
                String str = presentationDataList.remove(lineCounter);
                //Log.e("keke2.5", str);
            }
            int count = 0;
            if (keyword1Input.getText() != null) {
                count++;
            }
            if (keyword2Input.getText() != null) {
                count++;
            }
            if (keyword3Input.getText() != null) {
                count++;
            }
            presentationDataList.add(lineCounter++, count + "");
            if (keyword1Input.getText() != null) {
                presentationDataList.add(lineCounter++, keyword1Input.getText().toString());
            }
            if (keyword2Input.getText() != null) {
                presentationDataList.add(lineCounter++, keyword2Input.getText().toString());
            }
            if (keyword3Input.getText() != null) {
                presentationDataList.add(lineCounter++, keyword3Input.getText().toString());
            }
            presentationDataList.add(lineCounter, 0 + "");

            for (int i = 0; i < presentationDataList.size(); i++) {
                //Log.e("keke4", presentationDataList.get(i));
                fos.write((presentationDataList.get(i) + "\n").getBytes());
            }
            fos.close();
        }catch(NullPointerException e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }catch(Exception e) {
            Toast.makeText(getApplicationContext(), "Error reading: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
