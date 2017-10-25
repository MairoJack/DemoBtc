package com.example.zhuzhehai.demobtc.DataBase;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mark93 on 2/21/2017.
 */

public class DB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 10;
    private static final String DATABASE_NAME = "bitconDatae.db";
    private Context context;

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("info", " Create db");
        try {
            db.execSQL(Tickets.CREATE_TABLE_TICKETS);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(AlertRanking.CREATE_TABLE_ALERTRANKING);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old_version, int new_version) {
        db.execSQL("DROP TABLE IF EXISTS " + Tickets.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AlertRanking.TABLE_NAME);
        onCreate(db);
        Log.i("onUpgrade", " onUpgrade is called");
    }
}