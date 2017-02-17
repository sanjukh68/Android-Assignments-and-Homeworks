package com.example.tvs.weatherapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class DatabaseDataManager {

    private Context mContext;
    private DatabaseOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private CityDAO cityDAO;


    public DatabaseDataManager(Context mContext) {
        this.mContext = mContext;
        dbOpenHelper = new DatabaseOpenHelper(this.mContext);
        db = dbOpenHelper.getWritableDatabase();
        cityDAO = new CityDAO(db);
    }

    public void close() {
        if(db!=null) db.close();
    }

    public CityDAO getCityDAO() {
        return this.cityDAO;
    }

    public long saveCity(City city) {
        return this.cityDAO.save(city);
    }

    public boolean selectedUpdateCity(City city) {
        return this.cityDAO.selectedUpdate(city);
    }

    public boolean updateCity(City city) {
        return this.cityDAO.update(city);
    }

    public boolean deleteCity(City city) {
        return this.cityDAO.delete(city);
    }

    public City getCity(long id) {
        return this.cityDAO.get(id);
    }

    public long getCity(City city) {
        return this.cityDAO.get(city);
    }

    public ArrayList<City> getAllCities() {
        return this.cityDAO.getAll();
    }
}
