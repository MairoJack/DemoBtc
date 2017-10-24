package com.example.zhuzhehai.demobtc.DataBase;

/**
 * Created by zhuzhehai on 10/23/17.
 */

public class AlterRanking {

    //table name
    public static final String TABLE_NAME = "Tickets";

    //column info
    public static final String T_ID = "id";
    public static final String TIMESTAMP = "timestamp";
    public static final String T_last= "last";
    public static final String T_Change= "change";

    //create table
    public static final String CREATE_TABLE_ALERTRANKING = "CREATE TABLE " + TABLE_NAME
            + " ("
            + T_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TIMESTAMP + " VARCHAR(255) NOT NULL,"
            + T_last + " VARCHAR(320) NOT NULL"
            + T_Change + " VARCHAR(320) NOT NULL"
            + ");";
}
