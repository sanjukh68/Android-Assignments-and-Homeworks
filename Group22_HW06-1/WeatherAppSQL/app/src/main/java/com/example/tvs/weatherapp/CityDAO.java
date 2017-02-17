package com.example.tvs.weatherapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CityDAO {

    private SQLiteDatabase db;

    public CityDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public long save(City city) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CityTable.COLUMN_CITY, city.getCity());
        contentValues.put(CityTable.COLUMN_COUNTRY, city.getCountry());
        contentValues.put(CityTable.COLUMN_TEMP, city.getTemp());
        contentValues.put(CityTable.COLUMN_UPDATED_DATE, city.getUpdatedOn());
        contentValues.put(CityTable.COLUMN_FAVOURITE, city.getFavourite());
        return db.insert(CityTable.TABLE_NAME, null, contentValues);
    }

    public boolean selectedUpdate(City city) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CityTable.COLUMN_TEMP, city.getTemp());
        contentValues.put(CityTable.COLUMN_UPDATED_DATE, city.getUpdatedOn());
        return db.update(CityTable.TABLE_NAME, contentValues, CityTable.COLUMN_ID + "=?", new String[] {city.get_id() + ""}) > 0;
    }

    public boolean update(City city) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CityTable.COLUMN_CITY, city.getCity());
        contentValues.put(CityTable.COLUMN_COUNTRY, city.getCountry());
        contentValues.put(CityTable.COLUMN_TEMP, city.getTemp());
        contentValues.put(CityTable.COLUMN_UPDATED_DATE, city.getUpdatedOn());
        contentValues.put(CityTable.COLUMN_FAVOURITE, city.getFavourite());
        boolean x =  db.update(CityTable.TABLE_NAME, contentValues, CityTable.COLUMN_ID + "=?", new String[] {city.get_id() + ""}) > 0;
        Log.d("DDD", x + "");
        return x;
    }

    public boolean delete(City city) {
        return db.delete(CityTable.TABLE_NAME, CityTable.COLUMN_ID + "=?", new String[] {city.get_id() + ""}) > 0;
    }

    public City get(long id) {
        City city = null;
        Cursor c = db.query(CityTable.TABLE_NAME,
                new String[] {CityTable.COLUMN_ID, CityTable.COLUMN_CITY, CityTable.COLUMN_COUNTRY, CityTable.COLUMN_TEMP, CityTable.COLUMN_UPDATED_DATE, CityTable.COLUMN_FAVOURITE},
                CityTable.COLUMN_ID + "=?", new String[] {id + ""}, null, null, null, null);
        if(c!=null && c.moveToFirst()) {
            city = buildCityFromCursor(c);
            if(!c.isClosed()) c.close();
        }
        return city;
    }

    public long get(City city) {
        City found = null;
        Cursor c = db.query(CityTable.TABLE_NAME,
                new String[] {CityTable.COLUMN_ID, CityTable.COLUMN_CITY, CityTable.COLUMN_COUNTRY, CityTable.COLUMN_TEMP, CityTable.COLUMN_UPDATED_DATE, CityTable.COLUMN_FAVOURITE},
                CityTable.COLUMN_CITY + "=? AND " + CityTable.COLUMN_COUNTRY + "=?", new String[] {city.getCity() + "", city.getCountry() + ""}, null, null, null, null);
        if(c!=null && c.moveToFirst()) {
            if ((found=buildCityFromCursor(c)) != null) {
                if (!c.isClosed()) c.close();
                return found.get_id();
            }
        }
        if(!c.isClosed()) c.close();
        return -1;
    }

    public ArrayList<City> getAll() {
        ArrayList<City> cities = new ArrayList<City>();
        Cursor c = db.query(CityTable.TABLE_NAME,
                new String[] {CityTable.COLUMN_ID, CityTable.COLUMN_CITY, CityTable.COLUMN_COUNTRY, CityTable.COLUMN_TEMP, CityTable.COLUMN_UPDATED_DATE, CityTable.COLUMN_FAVOURITE},
                null, null, null, null, null);
        if(c!=null && c.moveToFirst()) {
            do {
                City city = buildCityFromCursor(c);
                if(city != null)
                    cities.add(city);
            } while(c.moveToNext());
            if(!c.isClosed()) c.close();
        }
        return cities;
    }

    private City buildCityFromCursor(Cursor c) {
        City city = null;
        if(c!=null) {
            city = new City();
            city.set_id(c.getLong(0));
            city.setCity(c.getString(1));
            city.setCountry(c.getString(2));
            city.setTemp(c.getDouble(3));
            city.setUpdatedOn(c.getString(4));
            city.setFavourite(c.getInt(5));
        }
        return city;
    }
}
