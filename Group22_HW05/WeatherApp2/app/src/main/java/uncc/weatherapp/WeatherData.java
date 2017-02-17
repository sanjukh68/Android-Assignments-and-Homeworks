/*
Assignment: 07
Name: Sanju Kurubara Budi Hall Hriyanna Gowda
 */

package uncc.weatherapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sanju on 9/26/2016.
 */

public class WeatherData extends AsyncTask<String,Void,String> {

    MainActivity activity;
    BufferedReader reader;
    //NewsDetails newsActivity;
    public WeatherData(MainActivity activity) {
       this.activity = activity;
    }

    /*
    public WeatherData(NewsDetails activity) {
        this.newsActivity = activity;
    }*/
    @Override
    protected String doInBackground(String... strings) {


    try {
        URL url = new URL(strings[0]);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();

        con.setRequestMethod("GET");
        con.connect();

        int statusCode = con.getResponseCode();
        if (statusCode == HttpURLConnection.HTTP_OK)
        {
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while((line = reader.readLine()) != null) sb.append(line + "\n");
            return sb.toString();
        }
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}

    @Override
    protected void onPostExecute(String strings) {
        super.onPostExecute(strings);

        Log.d("demo", "in postexecute" + strings);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Weather weather = gson.fromJson(strings, Weather.class);

        if (weather == null)
        {
            Log.d("demo", "weather null");
        }
        else
        {
            Log.d("demo", String.valueOf(weather.getHourly_forecast().size()));
        }

        Intent intent = new Intent(activity,HourlyData.class);
        intent.putExtra("WEATHER", weather);
        Log.d("demo", "sending intent");
        activity.startActivity(intent);

    }
}
