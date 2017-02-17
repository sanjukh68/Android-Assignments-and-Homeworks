package com.example.tvs.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SavedCitiesRecyclerAdapter.ViewHolder.OnClickEvents {

    protected final static String WEATHER_API_KEY = "10a1deea9807b2de453a44d8892da0d7", RESPONSE_MODE="JSON";
    public final static String ENP_POINT_WITH_PARAMS = "endpointWithParams", CITY_KEY = "cityKey", STATE_KEY = "stateKey";
    public final static String DATE_ONLY_FORMAT = "yyyy-MM-dd", REQUIRED_TIME_FORMAT = "hh:mm a";
    public final static String INPUT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss", REQUIRED_DATE_FORMAT = "MMM dd, yyyy";
    public final static DateFormat dtOnlyFrmt = new SimpleDateFormat(DATE_ONLY_FORMAT);
    public final static DateFormat rqTmFrmt = new SimpleDateFormat(REQUIRED_TIME_FORMAT);
    public final static DateFormat inptDtFrmt = new SimpleDateFormat(INPUT_DATE_FORMAT);
    public final static DateFormat rqDtFrmt = new SimpleDateFormat(REQUIRED_DATE_FORMAT);
    public final static long DELAY_TO_FINISH = 5000;
    public final int ACTIVITY_RESULT = 0x0001;
    public static int TEMP_UNIT = 0;
    public static SharedPreferences sharedPreferences;

    public static DatabaseDataManager dm;

    String WEATHER_API_ENDPOINT = "http://api.openweathermap.org/data/2.5/forecast?q=%cityName%,%countryName%&mode="+RESPONSE_MODE+"&units=metric&appid="+WEATHER_API_KEY;
    static ArrayList<City> savedCitiesList = new ArrayList<>();
    EditText city, state;
    Button submit;
    static TextView favouritesLabel, noFavouritesLabel;
    static RecyclerView savedCities;
    static SavedCitiesRecyclerAdapter savedCitiesRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dm = new DatabaseDataManager(this);

        submit = (Button) findViewById(R.id.submit);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        favouritesLabel = (TextView) findViewById(R.id.favouritesLabel);
        noFavouritesLabel = (TextView) findViewById(R.id.no_favourites_label);

        savedCities = (RecyclerView) findViewById(R.id.favouritesList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        savedCities.setLayoutManager(layoutManager);

        savedCities.addOnItemTouchListener(new RecyclerTouchListener(this, savedCities, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) throws ParseException {
            }

            @Override
            public void onLongClick(View view, int position) {
                dm.deleteCity(savedCitiesList.get(position));
                updateSavedCityList();
                Toast.makeText(MainActivity.this, getResources().getString(R.string.city_deleted), Toast.LENGTH_SHORT).show();
            }
        }));

        if(dm.getAllCities().size()==0) listFavourites(false);
        else listFavourites(true);

        submit.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.settings: Intent gotoSettings = new Intent(this, PreferenceActivity.class);
                                gotoSettings.putExtra( PreferenceActivity.EXTRA_SHOW_FRAGMENT, PreferenceActivity.GeneralPreferenceFragment.class.getName() );
                                gotoSettings.putExtra( PreferenceActivity.EXTRA_NO_HEADERS, true );
                                startActivityForResult(gotoSettings, 200);
                                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dm.close();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.submit:   String cityText = city.getText().toString().replace(" ", "");
                String stateText = state.getText().toString().replace(" ", "");
                if (cityText.length()==0 || cityText.matches(".*\\d+.*")) {
                    Toast.makeText(this, getResources().getString(R.string.city_name_error), Toast.LENGTH_SHORT).show();
                    return;
                } else if (stateText.length()!=2 || stateText.matches(".*\\d+.*")) {
                    Toast.makeText(this, getResources().getString(R.string.state_name_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                cityText = city.getText().toString().trim().replace(" ", "_");
                stateText = state.getText().toString().trim().replace(" ", "_");
                String apiWithParams = WEATHER_API_ENDPOINT.replace("%countryName%", stateText).replace("%cityName%", cityText);
                Intent getWeatherDetails = new Intent(this, CityWeather.class);
                getWeatherDetails.putExtra(ENP_POINT_WITH_PARAMS, apiWithParams);
                startActivityForResult(getWeatherDetails, ACTIVITY_RESULT);
                break;
        }
    }

    public void listFavourites(boolean flag) {
        int visibility = (flag)?View.VISIBLE:View.INVISIBLE;
        int visibilityReverse = (!flag)?View.VISIBLE:View.INVISIBLE;
        favouritesLabel.setVisibility(visibility);
        noFavouritesLabel.setVisibility(visibilityReverse);
        savedCities.setVisibility(visibility);
        if(flag) updateSavedCityList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 200:               if(TEMP_UNIT!=Integer.parseInt(sharedPreferences.getString("selected_temp_unit", "-1"))) {
                                        TEMP_UNIT = Integer.parseInt(sharedPreferences.getString("selected_temp_unit", "-1"));
                                        if(TEMP_UNIT==0)
                                            Toast.makeText(this, getResources().getString(R.string.preference_changed_to_C), Toast.LENGTH_SHORT).show();
                                        else Toast.makeText(this, getResources().getString(R.string.preference_changed_to_F), Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(getIntent());
                                    }
                                    break;
            case ACTIVITY_RESULT:   if(dm.getAllCities().size()!=0) listFavourites(true);
        }
    }


    private void updateSavedCityList() {
        savedCitiesList = dm.getAllCities();
        savedCitiesRecyclerAdapter = new SavedCitiesRecyclerAdapter(MainActivity.this, R.layout.saved_cities_recycler_adapter, dm.getAllCities());
        savedCities.setAdapter(savedCitiesRecyclerAdapter);
        if(savedCitiesList.size()==0) listFavourites(false);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    @Override
    public void onClicked(int position) {
        String apiWithParams = WEATHER_API_ENDPOINT.replace("%countryName%", savedCitiesList.get(position).getCountry()).replace("%cityName%", savedCitiesList.get(position).getCity());
        Intent getWeatherDetails = new Intent(MainActivity.this, CityWeather.class);
        getWeatherDetails.putExtra(ENP_POINT_WITH_PARAMS, apiWithParams);
        startActivityForResult(getWeatherDetails, ACTIVITY_RESULT);
    }

    @Override
    public void onRateClicked(int position, View v) {
        City city = savedCitiesList.get(position);
        if(savedCitiesList.get(position).getFavourite()==0) {
            city.setFavourite(1);
            dm.updateCity(city);
            ((ImageView) v.findViewById(R.id.star)).setImageResource(R.drawable.star_gold);
        }
        else {
            city.setFavourite(0);
            dm.updateCity(city);
            ((ImageView) v.findViewById(R.id.star)).setImageResource(R.drawable.star_gray);
        }
    }


}