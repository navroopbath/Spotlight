package com.example.chen.cs160finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
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
    List<String>presentationDataList;
    TextView presentationTitle;
    Button startButton;
    EditText keyword1Input;
    EditText keyword2Input;
    EditText keyword3Input;
    EditText minuteInput;
    EditText secondInput;
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
        startButton.setBackgroundColor(Color.parseColor("#295055"));
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

        minuteInput = (EditText) findViewById(R.id.minuteInput);
        secondInput = (EditText) findViewById(R.id.secondInput);

        minuteInput.setHint("00");
        secondInput.setHint("00");

        addSlideButton = (Button) findViewById(R.id.addSlideButton);
        addSlideButton.setBackgroundColor(Color.parseColor("#295055"));

        addSlideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeKeywords(slideNum);
                newSlide();
            }
        });

        deleteButton = (Button) findViewById(R.id.deleteSlideButton);
        deleteButton.setBackgroundColor(Color.parseColor("#295055"));
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            presentationNum = extras.getInt("presentation");
            loadPresentation();
        }
    }

    /*
    First modifies presentationDataList to be up to date, then writes presentationDataList.
    Finally, takes the new slideNum and loads slides up to that number.
     */
    private void newSlide() {
        slideNum++;
        for(int i = 0; i < presentationDataList.size(); i++) {
            Log.e("newSlide keke1", presentationDataList.get(i));
        }
        presentationDataList.set(1, slideNum + "");
        presentationDataList.add("0");
        presentationDataList.add("0");
        for (int i = 0; i < presentationDataList.size(); i++) {
            Log.e("newslide keke2", presentationDataList.get(i));
        }
        write();
        loadSlidesIntoView();
        resetSlide();
    }

    private void resetSlide() {
        keyword1Input.setText("");
        keyword2Input.setText("");
        keyword3Input.setText("");
        minuteInput.setText("");
        secondInput.setText("");
        keyword1Input.requestFocus();
    }

    private void loadSlidesIntoView() {
        LinearLayout slideLayout = (LinearLayout) findViewById(R.id.slides);
        slideLayout.removeAllViews();
        int totalSlides = Integer.parseInt(presentationDataList.get(1));
        for(int i = 1; i <= totalSlides; i++) {
            TextView nextSlide = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            nextSlide.setText(" " + i + " ");
            nextSlide.setTextSize(50);
            nextSlide.setTextColor(Color.parseColor("#ffffff"));
            nextSlide.setBackgroundColor(Color.parseColor("#6699ff"));
            if(i == totalSlides) {
                nextSlide.setBackgroundColor(Color.parseColor("#3366cc"));
            }
            nextSlide.setBackgroundResource(R.drawable.back);
            nextSlide.setLayoutParams(params);
            slideLayout.addView(nextSlide);
        }
    }

    /*
    Writes the data in presentationDataList into the text file. Overrides the text file, so
    presentationDataList must be complete.
     */
    private void write() {
        try {
            String FILE_NAME = MainActivity.FILE_NAME_BASE + presentationNum;
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            for(int i = 0; i < presentationDataList.size(); i++) {
                fos.write((presentationDataList.get(i) + "\n").getBytes());
            }
            fos.close();
        } catch (NullPointerException e) {
//            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), "writePresentation error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /*
    Loads the presentation corresponding to presentationNum and all of its data into
    presentationDataList. Each line in the text file is a separate entry in the list.
     */
    private void load() {
        try {
            String FILE_NAME = MainActivity.FILE_NAME_BASE + presentationNum;

            FileInputStream fis = openFileInput(FILE_NAME);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((fis)));
            String line = "";
            StringBuffer buffer = new StringBuffer();
            while((line = bufferedReader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            String[]presentationData = buffer.toString().split("\n");
            presentationDataList = new ArrayList<>();
            for(int i = 0; i < presentationData.length; i++) {
                presentationDataList.add(presentationData[i]);
            }
            fis.close();
        }catch(Exception e) {
//            Toast.makeText(getApplicationContext(), "Presentation data error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /*
    Loads the presentation data and also loads the keywords for the first slide.
    TODO: load all slides visually
     */
    private void loadPresentation() {
        load();
        presentationTitle.setText(presentationDataList.get(0).toUpperCase());
        if(presentationDataList.size() > 1) {
            loadKeywords(1);
        }
        loadSlidesIntoView();
    }

    /* presentation array will look like:
     0 [name of presentation]
     1 [integer representing number of slides, say x]
     --x blocks of stuff--, each block with the format:
        [integer representing lines of keywords, say k]
        --k lines of keywords--
        one line representing the time assigned to the slide
     */
    private void loadKeywords(int slideNumber) {
        int lineCounter = 2;
        int numKeywords;
        for(int i = 0; i < slideNumber - 1; i++) {
            numKeywords = Integer.parseInt(presentationDataList.get(lineCounter));
            lineCounter += numKeywords + MainActivity.EXTRA_LINES + 1;
        }
        numKeywords = Integer.parseInt(presentationDataList.get(lineCounter));
        switch(numKeywords) {
            case 1: keyword1Input.setText(presentationDataList.get(lineCounter + 1)); break;
            case 2: keyword1Input.setText(presentationDataList.get(lineCounter + 1));
                    keyword2Input.setText(presentationDataList.get(lineCounter + 2)); break;
            case 3: keyword1Input.setText(presentationDataList.get(lineCounter + 1));
                    keyword2Input.setText(presentationDataList.get(lineCounter + 2));
                    keyword3Input.setText(presentationDataList.get(lineCounter + 3)); break;
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
    private void storeKeywords(int slideNumber) {
        for(int i = 0; i < presentationDataList.size(); i++) {
            Log.e("keke1", presentationDataList.get(i));
        }
        int lineCounter = 2;
        int numKeywords;
        for (int i = 0; i < slideNumber - 1; i++) {
            numKeywords = Integer.parseInt(presentationDataList.get(lineCounter));
            lineCounter += numKeywords + MainActivity.EXTRA_LINES + 1;
        }
        numKeywords = Integer.parseInt(presentationDataList.get(lineCounter));
        Log.e("keke2", numKeywords+"");
        for (int i = 0; i < numKeywords + MainActivity.EXTRA_LINES + 1; i++) {
            String str = presentationDataList.remove(lineCounter);
            Log.e("keke2.5", str);
        }
        int count = 0;
        if (keyword1Input.getText().toString().length() != 0) {
            count++;
        }
        if (keyword2Input.getText().toString().length() != 0) {
            count++;
        }
        if (keyword3Input.getText().toString().length() != 0) {
            count++;
        }
        presentationDataList.add(lineCounter++, count + "");
        if (keyword1Input.getText().toString().length() != 0) {
            presentationDataList.add(lineCounter++, keyword1Input.getText().toString());
        }
        if (keyword2Input.getText().toString().length() != 0) {
            presentationDataList.add(lineCounter++, keyword2Input.getText().toString());
        }
        if (keyword3Input.getText().toString().length() != 0) {
            presentationDataList.add(lineCounter++, keyword3Input.getText().toString());
        }
        presentationDataList.add(lineCounter, 0 + "");
        for (int i = 0; i < presentationDataList.size(); i++) {
            Log.e("keke4", presentationDataList.get(i));
        }
        write();
    }
}
