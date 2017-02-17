package com.example.tvs.weatherapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WeatherForecast extends AppCompatActivity  implements GetWeatherDetails.PostExecHandler, View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    public static final String WEATHER_AT_TIME="weatherAtTime";

    ProgressDialog progressDialog;
    TextView cityDetails;
    ListView weatherDetails;
    CityWeather cityWeatherInDisplay;
    static ArrayList<HourlyForecast> listOfHourlyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        cityDetails = (TextView) findViewById(R.id.cityDetails);
        weatherDetails = (ListView) findViewById(R.id.weatherDetails);

        (findViewById(R.id.addToFav)).setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle(R.string.loding_data_msg);
        progressDialog.show();

        new GetWeatherDetails(this).execute(getIntent().getStringExtra(MainActivity.ENP_POINT_WITH_PARAMS));
    }

    @Override
    public void onPostExecute(final CityWeather favouriteCity) {
        progressDialog.dismiss();
        if(favouriteCity.response.error != null) {
            Handler delayHandler = new Handler();
            Toast.makeText(this, favouriteCity.response.error.description, Toast.LENGTH_LONG).show();
            findViewById(R.id.addToFav).setEnabled(false);
            delayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, MainActivity.DELAY_TO_FINISH);
        }
        else {
            cityWeatherInDisplay = favouriteCity;
            listOfHourlyData = favouriteCity.hourly_forecast;
            favouriteCity.city = getIntent().getStringExtra(MainActivity.CITY_KEY);
            favouriteCity.state = getIntent().getStringExtra(MainActivity.STATE_KEY);
            cityDetails.setText(favouriteCity.city + ", " + favouriteCity.state);

            WeatherListAdapter weatherAdapter = new WeatherListAdapter(this, R.layout.weather_list_adapter, listOfHourlyData);
            weatherAdapter.setNotifyOnChange(true);
            try {
                weatherDetails.setAdapter(weatherAdapter);
            } catch(Exception e) {
                findViewById(R.id.addToFav).setEnabled(false);
                Toast.makeText(WeatherForecast.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
            }
            try {
                weatherDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent showDetails = new Intent(WeatherForecast.this, WeatherInDetail.class);
                        showDetails.putExtra(WEATHER_AT_TIME, listOfHourlyData.get(position));
                        showDetails.putExtra(MainActivity.CITY_KEY, favouriteCity.city);
                        showDetails.putExtra(MainActivity.STATE_KEY, favouriteCity.state);
                        startActivity(showDetails);
                    }
                });
            } catch(Exception e) {
                findViewById(R.id.addToFav).setEnabled(false);
                Toast.makeText(WeatherForecast.this, R.string.error_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id) {
            case R.id.addToFav: PopupMenu popup = new PopupMenu(this, v);
                                MenuInflater inflater = popup.getMenuInflater();
                                inflater.inflate(R.menu.items, popup.getMenu());
                                popup.setOnMenuItemClickListener(this);
                                popup.show();
                                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.addCityToFav: int index = 0;
                                    for(CityWeather cityWeather : MainActivity.favouriteCities) {
                                        if(cityWeather.city.equals(cityWeatherInDisplay.city)) {
                                            MainActivity.favouriteCities.remove(index);
                                            MainActivity.favouriteCities.add(cityWeatherInDisplay);
                                            Toast.makeText(WeatherForecast.this, R.string.favourite_updated, Toast.LENGTH_SHORT).show();
                                            return true;
                                        }
                                        index++;
                                    }
                                    MainActivity.favouriteCities.add(cityWeatherInDisplay);
                                    Toast.makeText(WeatherForecast.this, R.string.favourite_added, Toast.LENGTH_SHORT).show();
                                    MainActivity.listFavourites(true);
                                    MainActivity.favouritesListAdapter.notifyDataSetChanged();
                                    return true;
            default:                return false;
        }
    }
}