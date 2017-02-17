package com.example.tvs.weatherapp;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class GetWeatherDetails extends AsyncTask<String, Void, String> {

    PostExecHandler caller;
    public GetWeatherDetails(PostExecHandler caller) {
        this.caller = caller;
    }

    @Override
    protected String doInBackground(String... params) {
        BufferedReader reader = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
            reader = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while((line = reader.readLine()) != null) sb.append(line + "\n");
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(reader!=null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        Gson gson = new GsonBuilder().create();
        WeatherResponse favouriteCity = gson.fromJson(s, WeatherResponse.class);
        DateFormat dfDateOnly = new SimpleDateFormat(MainActivity.DATE_ONLY_FORMAT);
        DateFormat displayFormat = new SimpleDateFormat(MainActivity.REQUIRED_DATE_FORMAT);

        if(null == favouriteCity || !favouriteCity.cod.equals("200")) caller.onError();
        else {
            for (int i = 0; i < favouriteCity.list.size(); i++) {
                try {
                    String date = dfDateOnly.format(dfDateOnly.parse(favouriteCity.list.get(i).dt_txt));
                    if (!favouriteCity.groupedData.containsKey(date)) {
                        ArrayList<WeatherResponse.WeatherList> newWeatherList = new ArrayList<>();
                        newWeatherList.add(favouriteCity.list.get(i));
                        favouriteCity.groupedData.put(date, newWeatherList);
                    } else
                        favouriteCity.groupedData.get(date).add(favouriteCity.list.get(i));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Collections.sort(favouriteCity.customDetails);

            favouriteCity.groupedData = new LinkedHashMap<>(favouriteCity.groupedData);
            Iterator it = favouriteCity.groupedData.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry<String, ArrayList<WeatherResponse.WeatherList>> pair = (Map.Entry) it.next();
                String icon = pair.getValue().get((int) Math.floor((pair.getValue().size())/2)).weather.get(0).icon;
                String date = "";
                Double avgTemp = 0.0;
                for(WeatherResponse.WeatherList weatherList : pair.getValue())
                    avgTemp += weatherList.main.temp;
                avgTemp = MainActivity.round(avgTemp / pair.getValue().size(),2);
                try {
                    date = displayFormat.format(dfDateOnly.parse(pair.getKey()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                favouriteCity.customDetails.add(new WeatherResponse.CustomDetails(date, Double.toString(avgTemp), icon));
            }

            try {
                caller.onPostExecute(favouriteCity);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    interface PostExecHandler {
        void onPostExecute(WeatherResponse weatherResponse) throws ParseException;
        void onError();
    }
}