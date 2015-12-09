package com.example.chen.cs160finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Presentation extends Activity {
    ArrayList<Long> timePerSlide = new ArrayList<Long>();
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private static final int SECOND = 1000;
    ArrayList<String[]> slides;
    double letterWidth = 7.5;
    int halfWidthOfScreen = 115 - 8;
    int idx = 0;
    int tapTime = 0;
    long startTime;
    long currTime;
    long slideStartTime;
    TextView kw1;
    TextView kw2;
    TextView kw3;
    TextView slideNum;
    TextView time;
    BoxInsetLayout myCurrBag;
    ImageButton bgButton;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    int bgGreen = 1;
    CountDownTimer timer;

    ProgressBar barTimer;
    int barMax = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentation);
        myCurrBag = (BoxInsetLayout)findViewById(R.id.Prezcontainer);
        time = (TextView)findViewById(R.id.time);
        kw1 = (TextView)findViewById(R.id.keyword1);
        kw2 = (TextView)findViewById(R.id.keyword2);
        kw3 = (TextView)findViewById(R.id.keyword3);
        slideNum = (TextView)findViewById(R.id.slideNum);
        bgButton = (ImageButton)findViewById(R.id.PresentationButton);
        Intent intent = getIntent();
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        bgButton.setOnTouchListener(gestureListener);
        //barTimer = (ProgressBar)findViewById(R.id.barTimer);
        //startTimer(1);
        String message = intent.getStringExtra("Message");
        startTime = intent.getLongExtra("StartTime", 0);
        slides = parseMessage(message);
        loadSlide(idx);

    }
    private void loadSlide(int i) {
        int second = Integer.parseInt(slides.get(i)[0]);
        Log.d("loadSlide ", String.valueOf(i));
        String keyword1 = slides.get(i)[1];
        String keyword2 = slides.get(i)[2];
        String keyword3 = slides.get(i)[3];
        kw1.setText(paddingString(keyword1));
        kw2.setText(paddingString(keyword2));
        kw3.setText(paddingString(keyword3));
        slideNum.setText(String.valueOf(i + 1) + "/" + String.valueOf(slides.size()));
        createAndStartTimer(second * 1000);

    }
    /* Assume the message in the form "second keyword1 keyword2 keyword3 \n second ... ... ..." */
    private ArrayList<String[]> parseMessage(String message){
        ArrayList<String[]> rtn = new ArrayList<String[]>();
        String[] addon = new String[4];
        String[] addon2 = new String[4];
        //String[] addon3 = new String[4];
        addon[0] = "20";
        //addon[1] = "Introduction";
        addon[1] = "B";
        //addon[2] = "Company History";
        addon[2] = "a";
        //addon[3] = "Product intro";
        addon[3] = "c";
        addon2[0] = "30";
        //addon2[1] = "Sale up 200%";
        addon2[1] = "Goals";

        //addon2[2] = " Active user 40M";
        addon2[2] = "Excel";
        //addon2[3] = " User up 100%";
          addon2[3] = "Flawless";
        //addon3[0] = "20";
        //addon3[1] = " Target Users";
        //addon3[2] = " Marketing Plan";
        //addon3[3] = "  Questions?";
        rtn.add(addon);
        rtn.add(addon2);
        //rtn.add(addon3);

        /*String lines[] = message.split("\\r?\\n");
        for (int i = 1; i < lines.length; i += 1){
            String slide = lines[0];
            // Content[0] = time in seconds; Content[1] = keyword 1; Content[2] = keyword 2; Content[3] = keyword 3
            String content[] = slide.split("\\s+");
            rtn.add(content);
        }*/
        return rtn;
    }
    private void createAndStartTimer(final int miliseconds) {
        slideStartTime = System.currentTimeMillis();
        Log.d("miliseconds", String.valueOf(miliseconds));
        timer = new CountDownTimer(miliseconds, SECOND) {
            long milisec_remain = miliseconds;

            public void onTick(long millisUntilFinished) {
                milisec_remain -= SECOND;
                /*currTime = System.currentTimeMillis();
                long diffSec = (currTime - startTime)/1000;
                if ((miliseconds/1000 - diffSec) == 5){
                    myCurrBag.setBackgroundResource(R.drawable.redbackground);
                    bgGreen = 0;
                }
                Long sec = diffSec % 60;
                Long min = diffSec / 60;*/
                Long sec = (milisec_remain/1000) %60;
                Long min = milisec_remain/60000;
                if (milisec_remain/1000 < 5){
                    myCurrBag.setBackgroundResource(R.drawable.redbackground);
                    bgGreen = 0;
                }
                String secd = "";
                String minu = "";
                String timeNow;
                if (sec < 10) {
                    secd = "0" + sec.toString();
                } else {
                    secd = sec.toString();
                }
                if (min < 10) {
                    minu = "0" + min.toString();
                }else{
                    minu = min.toString();
                }
                timeNow = minu + " : " + secd;
                time.setText(timeNow);
            }
            public void onFinish() {
                time.setText("00 : 00");
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                v.vibrate(500);
            }
        };

        timer.start();
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent ev) {
            timer.cancel();
            idx += 1;
            if (idx < slides.size()) {
                if (bgGreen == 0) {
                    myCurrBag.setBackgroundResource(R.drawable.greenbackground);
                    bgGreen = 1;
                }
                currTime = System.currentTimeMillis();
                timePerSlide.add((currTime - slideStartTime)/1000);
                Toast.makeText(Presentation.this, String.valueOf((currTime - slideStartTime)/1000), Toast.LENGTH_SHORT).show();
                loadSlide(idx);
            } else {
                Intent intent = new Intent(Presentation.this, EndPresentation.class);
                startActivity(intent);
                finish();
            }

            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    timer.cancel();
                    //Toast.makeText(Presentation.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                    idx += 1;
                    if (idx < slides.size()) {
                        if (bgGreen == 0) {
                            myCurrBag.setBackgroundResource(R.drawable.greenbackground);
                            bgGreen = 1;
                        }
                        currTime = System.currentTimeMillis();
                        timePerSlide.add((currTime - slideStartTime)/1000);
                        Toast.makeText(Presentation.this, String.valueOf((currTime - slideStartTime)/1000), Toast.LENGTH_SHORT).show();
                        loadSlide(idx);
                    } else {
                        Intent intent = new Intent(Presentation.this, EndPresentation.class);
                        String timeOfSlide = convertTimetoString(timePerSlide);
                        intent.putExtra("toMobile", timeOfSlide);
                        startActivity(intent);
                        finish();
                    }
                }
            } catch (Exception e) {
                //Toast.makeText(Presentation.this, "Error", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }
    private String convertTimetoString(ArrayList<Long> time) {
        return null;
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

