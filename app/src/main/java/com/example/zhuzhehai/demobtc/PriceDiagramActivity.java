package com.example.zhuzhehai.demobtc;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PriceDiagramActivity extends AppCompatActivity {


    private Handler handler = new Handler();

    @BindView(R.id.line_chart)
    LineChart lineChart;

    private LineDataSet lineDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_price_diagram);
        ButterKnife.bind(this);
        Description description = new Description();
        description.setText("Price Diagram");
        lineChart.setDescription(description);

        ArrayList<Entry> vals = new ArrayList<>();

        vals.add(new Entry(2, 10));
        vals.add(new Entry(4, 15));
        vals.add(new Entry(6, 30));
        vals.add(new Entry(8, 23));
        vals.add(new Entry(10, 6));

        lineDataSet = new LineDataSet(vals, "Bitcoin");
        lineDataSet.setColor(Color.RED);
        lineDataSet.setCircleColor(Color.GREEN);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setValueTextSize(14f);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.setMaxVisibleValueCount(5);
        lineChart.invalidate();


        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(16f);
        xAxis.setTextColor(Color.BLACK);

        YAxis right = lineChart.getAxisRight();
        right.setEnabled(false);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setTextSize(16f);
        yAxis.setTextColor(Color.BLACK);

        handler.postDelayed(runnable, 2000);
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            LineData data = lineChart.getData();
            data.addEntry(new Entry(data.getXMax() + 2, (float) ((Math.random()) * 100)), 0);
            lineChart.moveViewToX(data.getDataSetCount() - 5);
            lineChart.setMaxVisibleValueCount(5);
            handler.postDelayed(this, 2000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
