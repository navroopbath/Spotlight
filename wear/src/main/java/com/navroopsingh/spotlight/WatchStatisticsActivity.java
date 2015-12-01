package com.navroopsingh.spotlight;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

public class WatchStatisticsActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_statistics);
    }

    // This is the onClick listener for this Activity's ImageButton
    public void goToStatsOnPhone(View view) {
        ;
    }
}
