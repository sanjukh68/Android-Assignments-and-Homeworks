package uncc.weatherapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class HourlyData extends AppCompatActivity {

    ListView hourlyDataList;
    WeatherAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_weather);
        Log.d("Demo", "Hourly data");

        if(getIntent().getExtras()!=null) {
            Log.d("demo", "Hourly data 1");
            hourlyDataList = (ListView) findViewById(R.id.listView_hourly_data);
            Weather weather = (Weather) getIntent().getExtras().getSerializable("WEATHER");

            adapter = new WeatherAdapter(this, R.layout.row_layout, weather.getHourly_forecast());
            hourlyDataList.setAdapter(adapter);
            adapter.setNotifyOnChange(true);

        }
    }

}
