package com.example.tvs.weatherapp;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CityTable {

    static final String TABLE_NAME = "cities";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_CITY = "city";
    static final String COLUMN_COUNTRY = "country";
    static final String COLUMN_TEMP = "temperature";
    static final String COLUMN_UPDATED_DATE = "updated_on";
    static final String COLUMN_FAVOURITE = "favourite";

    static public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLE_NAME + " (");
        sb.append(COLUMN_ID + " integer primary key autoincrement, ");
        sb.append(COLUMN_CITY + " text not null, ");
        sb.append(COLUMN_COUNTRY + " text not null, ");
        sb.append(COLUMN_TEMP + " real not null, ");
        sb.append(COLUMN_UPDATED_DATE + " text not null, ");
        sb.append(COLUMN_FAVOURITE + " integer not null);");

        try {
            db.execSQL(sb.toString());

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        CityTable.onCreate(db);
    }
}
