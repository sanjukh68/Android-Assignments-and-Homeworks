package com.example.tvs.weatherapp;

import java.io.Serializable;
import java.util.ArrayList;

public class CityWeather {
    String city, state;
    Response response;
    ArrayList<HourlyForecast> hourly_forecast;
}

class Response {
    String version, termsofService;
    Feature features;
    ErrorObject error = null;

    class Feature {
        int hourly;
    }

    class ErrorObject {
        String type, description;
    }
}

class HourlyForecast implements Serializable {
    FCTTime FCTTIME;
    WindDirection wdir;
    String condition, icon, icon_url, fctcode, sky, wx, uvi, humidity, pop;
    EngMetric temp, dewpoint, wspd, windchill, heatindex, feelslike, qpf, snow, mslp;

    class FCTTime  implements Serializable {
        String hour, hour_padded, min, min_unpadded, sec;
        String year, mon, mon_padded, mon_abbrev, mday, mday_padded, yday;
        String isdst, epoch, pretty, civil, month_name, month_name_abbrev;
        String weekday_name, weekday_name_night, weekday_name_abbrev, weekday_name_unlang;
        String weekday_name_night_unlang, ampm, tz, age, UTCDATE;
    }

    class EngMetric implements Serializable {
        String english, metric;
    }

    class WindDirection implements Serializable {
        String dir, degrees;
    }
}
