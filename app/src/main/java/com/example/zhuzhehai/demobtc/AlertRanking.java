package com.example.zhuzhehai.demobtc;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.zhuzhehai.demobtc.DataBase.DB;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class AlertRanking extends AppCompatActivity {



    private static final String[] TABLE_HEADERS = { "#", "Prices", "Change", "Timestamp"};
    private List<String[]> dataSource = new ArrayList<>();
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_ranking);
        db = new DB(this).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM AlertRanking ORDER BY change DESC", null);
        int count = 1;
        while (cursor.moveToNext()) {
            dataSource.add(new String[4]);
            int index = dataSource.size() - 1;
            dataSource.get(index)[0] = count + "";
            dataSource.get(index)[1] = cursor.getString(cursor.getColumnIndex("last"));
            DecimalFormat df = new DecimalFormat("#.####");
            Double num = cursor.getDouble(cursor.getColumnIndex("change"));
            String s = df.format(num * 100 )+ "%";
            dataSource.get(index)[2] = s;
            dataSource.get(index)[3] = cursor.getString(cursor.getColumnIndex("timestamp"));
            count++;
        }
        String[][] DATA_TO_SHOW = new String[dataSource.size()][4];
        for (int i = 0; i < dataSource.size(); i++) {
            DATA_TO_SHOW[i] = dataSource.get(i);
        }
        TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.tableView);
        TableColumnWeightModel columnModel = new TableColumnWeightModel(4);
        columnModel.setColumnWeight(0, 1);
        columnModel.setColumnWeight(1, 2);
        columnModel.setColumnWeight(2, 2);
        columnModel.setColumnWeight(3, 4);
        tableView.setColumnModel(columnModel);
        tableView.setHeaderElevation(10);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, DATA_TO_SHOW));
    }
}
