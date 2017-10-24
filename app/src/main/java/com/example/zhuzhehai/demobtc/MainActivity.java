package com.example.zhuzhehai.demobtc;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.zhuzhehai.demobtc.DataBase.DB;
import com.example.zhuzhehai.demobtc.bitcoinaverage.Baverage;
import com.example.zhuzhehai.demobtc.bitcoinaverage.BaverageException;
import com.example.zhuzhehai.demobtc.model.BitconInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    BitconInfo Bcon;
    TextView txV;
    TextView txMax;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txV = (TextView) findViewById(R.id.price);
        txMax = (TextView) findViewById(R.id.Max);
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
            txV.setText(Bcon.last);
            db = new DB(MainActivity.this).getWritableDatabase();
            Double end = Double.parseDouble(Bcon.timestamp);
            Double start = Double.parseDouble(Bcon.timestamp) - 300;
            String[] args = new String[1];
            args[0] = (start+"");
            Log.v("start", start + "");
            Cursor cursor = db.rawQuery("SELECT MAX(last) as last FROM Tickets WHERE timestamp >= ?", args);
            cursor.moveToFirst();
            int maxid = cursor.getInt(cursor.getColumnIndex("last"));
            Log.v("123456", maxid + "");
            txMax.setText(maxid + "");
            ContentValues values = new ContentValues();
            values.put("timestamp", result.timestamp);
            values.put("last", result.last);
            db.insert("Tickets", "", values);

        }
    }
}

