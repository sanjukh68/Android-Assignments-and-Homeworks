package com.example.tvs.weatherapp;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
        caller.onPostExecute(gson.fromJson(s, CityWeather.class));
    }

    interface PostExecHandler {
        void onPostExecute(CityWeather favouriteCity);
    }
}