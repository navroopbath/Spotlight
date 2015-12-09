package com.example.chen.cs160finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class EditPresentation extends AppCompatActivity {

    int presentationNum;
    int currentSlide;
    List<String>presentationDataList;
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
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_white, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeKeywords(currentSlide);
                Intent goToMenu = new Intent(EditPresentation.this, MainActivity.class);
                startActivity(goToMenu);
            }
        });
        android.support.v7.app.ActionBar tempbar = getSupportActionBar();
        currentSlide = 1;
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
                storeKeywords(currentSlide);
                newSlide();
            }
        });

        deleteButton = (Button) findViewById(R.id.deleteSlideButton);
        deleteButton.setBackgroundColor(Color.parseColor("#295055"));
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSlide(currentSlide);
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            presentationNum = extras.getInt("presentation");
            loadPresentation();
        }
        tempbar.setTitle(presentationDataList.get(0).toUpperCase());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_presentation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.start:
                storeKeywords(currentSlide);
                Intent goToPresenting = new Intent(EditPresentation.this, PresentationInProgress.class);
                startActivity(goToPresenting);
                return true;

            default: return true;
        }
    }

    private void deleteSlide(int slideNumber) {
        int numSlides = Integer.parseInt(presentationDataList.get(1));
        if(numSlides == 1) {
            return;
        }
        numSlides--;
        presentationDataList.set(1, numSlides + "");
        int lineCounter = 2;
        int numKeywords;
        for(int i = 0; i < slideNumber - 1; i++) {
            numKeywords = Integer.parseInt(presentationDataList.get(lineCounter));
            lineCounter += numKeywords + MainActivity.EXTRA_LINES + 1;
        }
        numKeywords = Integer.parseInt(presentationDataList.get(lineCounter));
        for(int i = 0; i < numKeywords + MainActivity.EXTRA_LINES + 1; i++) {
            presentationDataList.remove(lineCounter);
        }
        write();
        if(currentSlide == numSlides + 1) {
            currentSlide--;
        }
        resetSlide();
        loadKeywords(currentSlide);
        loadSlidesIntoView();
    }

    /*
    First modifies presentationDataList to be up to date, then writes presentationDataList.
    Finally, takes the new total slides and loads slides up to that number.
     */
    private void newSlide() {
        currentSlide = Integer.parseInt(presentationDataList.get(1)) + 1;
        for(int i = 0; i < presentationDataList.size(); i++) {
            Log.e("newSlide 1", presentationDataList.get(i));
        }
        presentationDataList.set(1, currentSlide + "");
        presentationDataList.add("0");
        presentationDataList.add("0");
        for (int i = 0; i < presentationDataList.size(); i++) {
            Log.e("newslide 2", presentationDataList.get(i));
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
            final int slideNumber = i;
            TextView nextSlide = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            nextSlide.setText(" " + slideNumber + " ");
            nextSlide.setTextSize(50);
            nextSlide.setTextColor(Color.parseColor("#ffffff"));
            if(i != currentSlide) {
//                nextSlide.setBackgroundColor(Color.parseColor("#6699ff"));
                Log.e("loadSlidesIntoView", "not the current slide: " + i);
            } else {
//                nextSlide.setBackgroundColor(Color.parseColor("#3366cc"));
                nextSlide.setTextColor(Color.parseColor("#000000"));
                nextSlide.setBackgroundResource(R.drawable.back);
                Log.e("loadSlidesIntoView", "current slide: " + i);
            }
            nextSlide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storeKeywords(currentSlide);
                    resetSlide();
                    currentSlide = slideNumber;
                    loadKeywords(currentSlide);
                    loadSlidesIntoView();
                }
            });
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
     */
    private void loadPresentation() {
        load();
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
        int time = 0;
        switch(numKeywords) {
            case 1: keyword1Input.setText(presentationDataList.get(lineCounter + 1));
                time = Integer.parseInt(presentationDataList.get(lineCounter + 2));
                break;
            case 2: keyword1Input.setText(presentationDataList.get(lineCounter + 1));
                keyword2Input.setText(presentationDataList.get(lineCounter + 2));
                time = Integer.parseInt(presentationDataList.get(lineCounter + 3));
                break;
            case 3: keyword1Input.setText(presentationDataList.get(lineCounter + 1));
                keyword2Input.setText(presentationDataList.get(lineCounter + 2));
                keyword3Input.setText(presentationDataList.get(lineCounter + 3));
                time = Integer.parseInt(presentationDataList.get(lineCounter + 4));
                break;
            default:
                time = Integer.parseInt(presentationDataList.get(lineCounter + 1));
                break;
        }
        int minutes = time / 60;
        int seconds = time % 60;
        if(minutes != 0) {
            minuteInput.setText(minutes + "");
        }
        if(seconds != 0) {
            secondInput.setText(seconds + "");
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
        presentationDataList.add(lineCounter, getTotalTime() + "");
        for (int i = 0; i < presentationDataList.size(); i++) {
            Log.e("keke4", presentationDataList.get(i));
        }
        write();
    }

    private int getTotalTime() {
        int minutes = 0;
        int seconds = 0;
        if(minuteInput.getText() != null && minuteInput.getText().length() != 0) {
            minutes = Integer.parseInt(minuteInput.getText().toString());
        }
        if(secondInput.getText() != null && secondInput.getText().length() != 0) {
            seconds = Integer.parseInt(secondInput.getText().toString());
        }
        return minutes * 60 + seconds;
    }
}

