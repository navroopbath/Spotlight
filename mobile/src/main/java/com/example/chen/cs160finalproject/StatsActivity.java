package com.example.chen.cs160finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class StatsActivity extends AppCompatActivity {

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

        createTimingGraph(5.0);
    }

    public void createTimingGraph(double numSlides) {
        GraphView timingGraph = (GraphView) findViewById(R.id.timeGraph);
        BarGraphSeries<DataPoint> timingMockSeries = new BarGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(1, 5),
                new DataPoint(2, 30)
        });
        GridLabelRenderer timingGraphRenderer = timingGraph.getGridLabelRenderer();
        Viewport timingViewport = timingGraph.getViewport();
        timingViewport.setXAxisBoundsManual(true);
        timingViewport.setMaxX(numSlides + 1.0);
        timingGraphRenderer.setVerticalAxisTitle("Seconds");
        timingGraphRenderer.setHorizontalAxisTitle("Slide Number");
        timingGraphRenderer.setGridColor(33);
        timingGraphRenderer.setNumHorizontalLabels(7);
        timingGraph.setTitle("Seconds Spent Per Slide");
        timingGraph.setTitleTextSize(35);
        timingGraph.addSeries(timingMockSeries);
    }

}
