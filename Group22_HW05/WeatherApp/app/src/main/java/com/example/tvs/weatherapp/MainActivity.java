package com.example.tvs.weatherapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected final static String WEATHER_API_KEY = "9bb32f4ad8859f72";
    public final static String ENP_POINT_WITH_PARAMS = "endpointWithParams", CITY_KEY = "cityKey", STATE_KEY = "stateKey";
    public final static long DELAY_TO_FINISH = 5000;
    public final int ACTIVITY_RESULT = 0x0001;

    String WEATHER_API_ENDPOINT = "http://api.wunderground.com/api/"+MainActivity.WEATHER_API_KEY+"/hourly/q/%stateName%/%cityName%.json";
    static ArrayList<CityWeather> favouriteCities = new ArrayList<>();
    EditText city, state;
    Button submit;
    static TextView favouritesLabel, noFavouritesLabel;
    static ListView favouritesList;
    static FavouritesListAdapter favouritesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit = (Button) findViewById(R.id.submit);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        favouritesLabel = (TextView) findViewById(R.id.favouritesLabel);
        noFavouritesLabel = (TextView) findViewById(R.id.no_favourites_label);

        favouritesList = (ListView) findViewById(R.id.favouritesList);
        favouritesListAdapter = new FavouritesListAdapter(this, R.layout.favourite_list_adapter, favouriteCities);
        favouritesListAdapter.setNotifyOnChange(true);
        favouritesList.setAdapter(favouritesListAdapter);
        favouritesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                favouriteCities.remove(position);
                Toast.makeText(MainActivity.this, R.string.city_deleted, Toast.LENGTH_SHORT).show();
                favouritesListAdapter.notifyDataSetChanged();
                if(favouriteCities.size()==0) listFavourites(false);
                return true;
            }
        });

        if(favouriteCities.size()==0) listFavourites(false);
        else listFavourites(true);

        submit.setOnClickListener(this);
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
                String apiWithParams = WEATHER_API_ENDPOINT.replace("%stateName%", stateText).replace("%cityName%", cityText);
                Intent getWeatherDetails = new Intent(this, WeatherForecast.class);
                getWeatherDetails.putExtra(ENP_POINT_WITH_PARAMS, apiWithParams);
                getWeatherDetails.putExtra(CITY_KEY, cityText.replace("_", " "));
                getWeatherDetails.putExtra(STATE_KEY, stateText.replace("_", " "));
                startActivityForResult(getWeatherDetails, ACTIVITY_RESULT);
                break;
        }
    }

    public static void listFavourites(boolean flag) {
        int visibility = (flag)?View.VISIBLE:View.INVISIBLE;
        int visibilityReverse = (!flag)?View.VISIBLE:View.INVISIBLE;
        favouritesLabel.setVisibility(visibility);
        noFavouritesLabel.setVisibility(visibilityReverse);
        favouritesList.setVisibility(visibility);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case ACTIVITY_RESULT:   if(favouriteCities.size()!=0) listFavourites(true);
        }
    }
}