package com.example.tvs.weatherapp;

import java.util.Date;

public class City {
    private long _id;
    private int favourite;
    private String city, country, updatedOn;
    private double temp;

    public City() {}

    public City(int favourite, String city, String country, double temp, String updatedOn) {
        this.favourite = favourite;
        this.city = city;
        this.country = country;
        this.temp = temp;
        this.updatedOn = updatedOn;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Override
    public String toString() {
        return "City{" +
                "_id=" + _id +
                ", favourite=" + favourite +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", temp=" + temp +
                ", updatedOn=" + updatedOn +
                '}';
    }
}
