package com.example.tvs.weatherapp;

import java.io.Serializable;
import java.util.ArrayList;

public class CityWeather implements Serializable{
    String city, state, dateUpdated;
    HourlyForecast.EngMetric avgTemp = new HourlyForecast.EngMetric();
    Response response;
    ArrayList<HourlyForecast> hourly_forecast;
}

class Response implements Serializable{
    ErrorObject error = null;

    class ErrorObject implements Serializable{
        String description;
    }
}

class HourlyForecast implements Serializable {
    FCTTime FCTTIME;
    WindDirection wdir;
    String condition, icon_url, humidity;
    EngMetric temp, dewpoint, wspd, feelslike, mslp;

    class FCTTime  implements Serializable {
        String civil;
    }

    static class EngMetric implements Serializable {
        String english, metric;
    }

    static class WindDirection implements Serializable {
        String dir, degrees;
    }
}
