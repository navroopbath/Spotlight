package com.example.chen.cs160finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Statistics");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_white, null));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMenu = new Intent(StatsActivity.this, MainActivity.class);
                startActivity(goToMenu);
            }
        });
        Bundle extras = getIntent().getExtras();
        Log.d("StatsActivity", String.valueOf(extras == null));
        if (extras != null) {
            data = extras.getString("data");
            Log.d("StatsActivity", data);
            createTimingGraph(data);
        }
    }

    public void createTimingGraph(String data) {
        Log.d("StatsActivity", data);
        List<String> dataList = Arrays.asList(data.split(" "));
        List<DataPoint> dataPoints = new ArrayList<>();
        for(int i = 0; i < dataList.size(); i++) {
            Log.d("StatsActivity", dataList.get(i));
            dataPoints.add(new DataPoint(i + 1, Integer.parseInt(dataList.get(i))));
        }

        GraphView timingGraph = (GraphView) findViewById(R.id.timeGraph);
        LineGraphSeries<DataPoint> timingMockSeries = new LineGraphSeries<DataPoint>(dataPoints.toArray(new DataPoint[dataPoints.size()]));
        GridLabelRenderer timingGraphRenderer = timingGraph.getGridLabelRenderer();
        Viewport timingViewport = timingGraph.getViewport();
        //timingViewport.setXAxisBoundsManual(true);
        //timingViewport.setMaxX(dataList.size() * 2);
        timingGraphRenderer.setVerticalAxisTitle("Seconds");
        timingGraphRenderer.setHorizontalAxisTitle("Slide Number");
        timingGraphRenderer.setGridColor(33);
        //timingGraphRenderer.setNumHorizontalLabels(dataList.size() + 1);
        timingGraph.setTitle("Seconds Spent Per Slide");
        timingGraph.setTitleTextSize(35);
        timingGraph.addSeries(timingMockSeries);
    }

}
