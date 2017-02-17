package com.example.tvs.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class WeatherInDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_in_detail);

        HourlyForecast weatherInDetail = (HourlyForecast) getIntent().getSerializableExtra(WeatherForecast.WEATHER_AT_TIME);

        ((TextView) findViewById(R.id.cityStateNames))
                .setText(getIntent().getStringExtra(MainActivity.CITY_KEY)+ ", " + getIntent().getStringExtra(MainActivity.STATE_KEY));
        Picasso.with(this).load(weatherInDetail.icon_url).into((ImageView) findViewById(R.id.weatherImage));
        ((TextView) findViewById(R.id.weatherCondition)).setText(weatherInDetail.condition);

        ((TextView) findViewById(R.id.temp)).setText(weatherInDetail.temp.english + " Farenheit");
        ((TextView) findViewById(R.id.maxTemp)).setText(weatherInDetail.temp.english + " Farenheit");
        ((TextView) findViewById(R.id.minTemp)).setText(weatherInDetail.temp.english + " Farenheit");
        ((TextView) findViewById(R.id.feelsLike)).setText(weatherInDetail.feelslike.english + " Farenheit");
        ((TextView) findViewById(R.id.humidity)).setText(weatherInDetail.humidity + "%");
        ((TextView) findViewById(R.id.dewPoint)).setText(weatherInDetail.dewpoint.english + " Farenheit");
        ((TextView) findViewById(R.id.pressure)).setText(weatherInDetail.mslp.english);
        ((TextView) findViewById(R.id.clouds)).setText(weatherInDetail.condition);
        ((TextView) findViewById(R.id.winds)).setText(weatherInDetail.wspd.english + "mph, " + weatherInDetail.wdir.degrees + " " + weatherInDetail.wdir.dir);
    }
}
