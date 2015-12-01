package com.navroopsingh.spotlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;

public class EndPresentationActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_presentation);
    }

    // This is the onClick listener method for this activity's ImageButton view
    public void goToStats(View view) {
        Intent intent = new Intent(this, WatchStatisticsActivity.class);
        startActivity(intent);
    }
}
