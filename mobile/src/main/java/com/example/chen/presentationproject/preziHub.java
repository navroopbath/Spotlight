package com.example.chen.presentationproject;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class preziHub extends AppCompatActivity {

    private GestureDetectorCompat detectSwipe;
    private ImageButton hubImage;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_prezi_hub);
        Intent hubIntent = getIntent();
        hubImage = (ImageButton) findViewById(R.id.preziHub);
        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        hubImage.setOnTouchListener(gestureListener);
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent ev) {
            String movementDetected =ev.toString();
            Toast.makeText(getApplicationContext(), "Going to Edit Now", Toast.LENGTH_LONG).show();
            Intent goToEdit = new Intent(preziHub.this, EditPrezi.class);
            preziHub.this.startActivity(goToEdit);
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(preziHub.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                    Intent goToStats = new Intent(preziHub.this, statsPrezi.class);
                    preziHub.this.startActivity(goToStats);
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(preziHub.this, "Right Swipe", Toast.LENGTH_SHORT).show();
                    Intent goToStats = new Intent(preziHub.this, statsPrezi.class);
                    preziHub.this.startActivity(goToStats);
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
    }

}
