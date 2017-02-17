package com.example.tvs.weatherapp;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class WeatherResponse implements Serializable{
    CityDetails city;
    String cod, message;
    Double cnt;
    ArrayList<WeatherList> list;

    ArrayList<CustomDetails> customDetails = new ArrayList<>();
    LinkedHashMap<String, ArrayList<WeatherList>> groupedData = new LinkedHashMap<>();

    static class CustomDetails implements Comparable<CustomDetails>{
        String date, temp, icon;

        public CustomDetails(String date, String temp, String icon) {
            this.date = date;
            this.temp = temp;
            this.icon = icon;
        }

        @Override
        public int compareTo(CustomDetails o) {
            try {
                if(MainActivity.rqDtFrmt.parse(this.date).after(MainActivity.rqDtFrmt.parse(o.date)))
                    return 1;
                if(MainActivity.rqDtFrmt.parse(this.date).before(MainActivity.rqDtFrmt.parse(o.date)))
                    return -1;
                else return 0;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    static class CityDetails {
        Integer id, population;
        String name, country;
        Coordinate coord;
        CitySys sys;

        static class Coordinate {
            Double lon, lat;
        }

        static class CitySys {
            Integer population;
        }
    }

    static class WeatherList {
        String dt, dt_txt;
        Main main;
        Wind wind;
        Cloud clouds;
        Rain rain;
        ArrayList<WeatherParams> weather;
        WeatherListSys sys;

        static class Main {
            Double temp, temp_min, temp_max, pressure, sea_level, grnd_level, humidity, temp_kf;
        }

        static class WeatherParams {
            Integer id;
            String main, description, icon;
        }

        static class Wind {
            Double speed, deg;
        }

        static class Rain {
            @SerializedName("3h") Double _3h;
        }

        static class Cloud {
            Integer all;
        }

        static class WeatherListSys {
            String pod;
        }
    }
}