package com.example.zhuzhehai.demobtc;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.zhuzhehai.demobtc.DataBase.DB;
import com.example.zhuzhehai.demobtc.bitcoinaverage.Baverage;
import com.example.zhuzhehai.demobtc.bitcoinaverage.BaverageException;
import com.example.zhuzhehai.demobtc.model.BitconInfo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_RANKING = 102;
    private static final int REQ_CODE_GRAPH = 103;
    private static final int REQ_CODE_NEWS = 104;

    BitconInfo Bcon;
    SQLiteDatabase db;
    @BindView(R.id.day) TextView day;
    @BindView(R.id.todayOpen) TextView todayOpen;
    @BindView(R.id.todayRate) TextView todayRate;
    @BindView(R.id.todayHigh) TextView todayHigh;
    @BindView(R.id.todayLow) TextView todayLow;
    @BindView(R.id.todayAverage) TextView todayAverage;
    @BindView(R.id.currentPrice) TextView currentPrice;
    @BindView(R.id.currentTime) TextView currentTime;
    @BindView(R.id.fiveMints) TextView fiveMints;
    @BindView(R.id.thirtyMints) TextView thirtyMints;
    @BindView(R.id.sixtyMints) TextView sixtyMints;
    @BindView(R.id.currentAsk) TextView currentAsk;
    @BindView(R.id.currentBid) TextView currentBid;


    @BindView(R.id.alertRanking) Button rankingButton;
    @BindView(R.id.graph) TextView graphButton;
    @BindView(R.id.news) TextView newsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        fakeRankingData();

        rankingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AlertRanking.class);
                startActivityForResult(intent, REQ_CODE_RANKING);
            }
        });
//
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            Bcon = new LoadDateTask().execute().get();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 10000);//毫秒
    }

    private void fakeRankingData() {
        db = new DB(MainActivity.this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("timestamp", "2017-08-09 19:33");
        values.put("last", 5555);
        values.put("change", -0.022 + "");
        db.insert("AlertRanking", "", values);
        values = new ContentValues();
        values.put("timestamp", "2017-08-08 13:22");
        values.put("last", 7463);
        values.put("change", -0.022 + "");
        db.insert("AlertRanking", "", values);
         values = new ContentValues();
        values.put("timestamp", "2017-10-09 15:22");
        values.put("last", 4238);
        values.put("change", -0.333 + "");
        db.insert("AlertRanking", "", values);
        values = new ContentValues();
        values.put("timestamp", "2017-07-09 11:22");
        values.put("last", 2233);
        values.put("change", -0.123 + "");
        db.insert("AlertRanking", "", values);
    }

    private class LoadDateTask extends AsyncTask<Void, Void, BitconInfo> {
        @Override
        protected BitconInfo doInBackground(Void... voids) {
            try {
                return Baverage.getBitCon();
            } catch (BaverageException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(BitconInfo result) {
            db = new DB(MainActivity.this).getWritableDatabase();
            Double currentTime = Double.parseDouble(Bcon.timestamp);
            Double currentPrice = Double.parseDouble(result.last);
            Log.v("Current price  = ", currentPrice + "");
            int[] timeRange = new int[]{50, 100, 200};
            double[] alertPecent = new double[]{0.0001, 0.0005, 0.001};
            String[] describe = new String[]{"5 mints", "30 mints", "60 mints"};
            double[] percent = new double[3];
            for (int i = 0; i < timeRange.length; i++) {
                Double decreasing = calculateDecPercent(currentTime, timeRange[i], currentPrice);
                percent[i] = decreasing;
                if (decreasing >= 0 && decreasing > alertPecent[i]) {
                    saveData("AlertRanking", result, -1 * percent[i]);
                    showAlert(decreasing, describe[i]);
                }
//                Log.v(i + "  Test decreaing ", decreasing + "");
            }
            showData(result, percent);
            saveData("Tickets", result, 0.0);
        }
        public void showData(BitconInfo bit, double[] percent) {
            String today = bit.display_timestamp;
            Log.v("Today", today);
            day.setText (today.substring(0,10));
            String open = bit.open.day;
            todayOpen.setText(open);
            String current = bit.last;
            Double rate = (Double.parseDouble(current) - Double.parseDouble(open)) / Double.parseDouble(current);
            DecimalFormat df = new DecimalFormat("#.####");
            todayRate.setText(df.format(rate*100) + "%");
            checkColor(todayRate, rate);
            todayHigh.setText(bit.high);
            todayLow.setText(bit.low);
            todayAverage.setText(bit.averages.day);
            currentPrice.setText(bit.last);
            currentTime.setText(today.substring(10));
            fiveMints.setText(df.format(-1 * percent[0] * 100) + "%");
            checkColor(fiveMints, -1 * percent[0]);
            thirtyMints.setText(df.format(-1 * percent[1] * 100) + "%");
            checkColor(thirtyMints, -1 * percent[1]);
            sixtyMints.setText(df.format(-1 * percent[2] * 100) + "%");
            checkColor(sixtyMints, -1 * percent[2]);
            currentAsk.setText(bit.ask);
            currentBid.setText(bit.bid);
        }
        public void checkColor(TextView view, Double num) {
            if (num > 0) {
                view.setTextColor(getColor(R.color.colorIncrease));
            } else {
                view.setTextColor(getColor(R.color.colorDecrease));
            }
        }
        public void saveData(String tableName, BitconInfo result, Double d) {
            ContentValues values = new ContentValues();
            values.put("last", result.last);
            if (tableName.equals("AlertRanking")) {
                values.put("change", d);
                values.put("timestamp", result.display_timestamp);
            } else {
                values.put("timestamp", result.timestamp);
            }
            db.insert(tableName, "", values);
        }
        public double calculateDecPercent(Double currentTime, int timeRange, Double currentPrice) {
            Double start = currentTime - timeRange;
            BigDecimal bdecimal = new BigDecimal(start);
            String[] args = new String[]{bdecimal.toPlainString()};
            Log.v("start", args[0]);
            Cursor cursor = db.rawQuery("SELECT MAX(last) as max FROM Tickets WHERE timestamp >= ?", args);
            cursor.moveToFirst();
            double maxPrice = cursor.getDouble(cursor.getColumnIndex("max"));
            Log.v("max price  = ", maxPrice + "");
            if (maxPrice == 0) {
                return 0;
            }
            double percent = (maxPrice - currentPrice) / maxPrice;
            Log.v("percent  = ", percent + "");
            return percent;
        }
        public void showAlert(Double decreasing, String describe){
            decreasing *= 100;
            DecimalFormat df = new DecimalFormat("#.####");
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("DECREADING ALERT")
                    .setMessage("Bitcon price decreased " + (df.format(decreasing)) + "% in " + describe + "")
                    .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton("FINE", null).show();
        }
    }
}

