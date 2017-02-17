package com.example.tvs.weatherapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class CityWeather extends AppCompatActivity  implements GetWeatherDetails.PostExecHandler {

    public static final String IMAGE_ICON_URL = "http://openweathermap.org/img/w/%iconNum%.png";

    ProgressDialog progressDialog;
    WeatherResponse cityWeatherInDisplay;
    RecyclerView recyclerView, details;
    ArrayList<WeatherResponse.WeatherList> adapterList;
    DetailWeatherRecyclerAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        details = (RecyclerView) findViewById(R.id.weatherDetailRecycler);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle(R.string.loding_data_msg);
        progressDialog.show();

        new GetWeatherDetails(this).execute(getIntent().getStringExtra(MainActivity.ENP_POINT_WITH_PARAMS));
    }

    @Override
    public void onPostExecute(WeatherResponse cityWeather) throws ParseException {
        progressDialog.dismiss();
        cityWeatherInDisplay = cityWeather;
        ((TextView)findViewById(R.id.forecastTitle))
                .setText(getResources().getString(R.string.forecastTitle) + " " + cityWeather.city.name + ", " + cityWeather.city.country);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        final DailyWeatherRecyclerAdapter adapter = new DailyWeatherRecyclerAdapter(this, R.layout.daily_weather_card, cityWeather.customDetails);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        details.setLayoutManager(layoutManager1);

        Iterator it = cityWeather.groupedData.entrySet().iterator();
        if(it.hasNext()) {
            LinkedHashMap.Entry<String, ArrayList<WeatherResponse.WeatherList>> linkedHashMap = (LinkedHashMap.Entry) it.next();
            changeDetails(linkedHashMap.getKey(), linkedHashMap.getValue());
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) throws ParseException {
                String date = MainActivity.dtOnlyFrmt.format(MainActivity.rqDtFrmt.parse(cityWeatherInDisplay.customDetails.get(position).date));
                changeDetails(date, cityWeatherInDisplay.groupedData.get(date));
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    public void changeDetails(String date, ArrayList<WeatherResponse.WeatherList> arrayList) throws ParseException {
        ((TextView)findViewById(R.id.threeHourlyForecast))
                .setText(getResources().getString(R.string.threeHourlyTitle) + " " + MainActivity.rqDtFrmt.format(MainActivity.dtOnlyFrmt.parse(date)));
        adapterList = arrayList;
        adapter1 = new  DetailWeatherRecyclerAdapter(CityWeather.this, R.layout.weather_detail_recycler_adapter, adapterList);
        details.setAdapter(adapter1);
    }

    @Override
    public void onError() {
        if(progressDialog.isShowing()) progressDialog.dismiss();
        Toast.makeText(this, getResources().getString(R.string.error_msg), Toast.LENGTH_LONG).show();
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, MainActivity.DELAY_TO_FINISH);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.city_weather_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.save_city:if(null == cityWeatherInDisplay || cityWeatherInDisplay.customDetails == null) {
                                    Toast.makeText(this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                                Date date = new Date();
                                Double avgAvgTemp = 0.0;
                                for(WeatherResponse.CustomDetails customDetails : cityWeatherInDisplay.customDetails)
                                    avgAvgTemp += Double.parseDouble(customDetails.temp);
                                avgAvgTemp /= cityWeatherInDisplay.customDetails.size();
                                City city = new City(0, cityWeatherInDisplay.city.name, cityWeatherInDisplay.city.country,
                                        avgAvgTemp, MainActivity.rqDtFrmt.format(date));
                                long cityId = MainActivity.dm.getCity(city);
                                //If city isn't in the database
                                if(cityId == -1) {
                                    MainActivity.dm.saveCity(city);
                                    Toast.makeText(this, getResources().getString(R.string.favourite_added), Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    city.set_id(cityId);
                                    MainActivity.dm.selectedUpdateCity(city);
                                    Toast.makeText(this, getResources().getString(R.string.favourite_updated), Toast.LENGTH_SHORT).show();
                                }
                                break;
            case R.id.settings: Intent gotoSettings = new Intent(this, PreferenceActivity.class);
                                gotoSettings.putExtra( PreferenceActivity.EXTRA_SHOW_FRAGMENT, PreferenceActivity.GeneralPreferenceFragment.class.getName() );
                                gotoSettings.putExtra( PreferenceActivity.EXTRA_NO_HEADERS, true );
                                startActivityForResult(gotoSettings, 400);
                                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 400:   if(MainActivity.TEMP_UNIT!=Integer.parseInt(MainActivity.sharedPreferences.getString("selected_temp_unit", "-1"))) {
                            MainActivity.TEMP_UNIT = Integer.parseInt(MainActivity.sharedPreferences.getString("selected_temp_unit", "-1"));
                            if(MainActivity.TEMP_UNIT==0)
                                Toast.makeText(this, getResources().getString(R.string.preference_changed_to_C), Toast.LENGTH_SHORT).show();
                            else Toast.makeText(this, getResources().getString(R.string.preference_changed_to_F), Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                        }
                        break;
        }
    }
}