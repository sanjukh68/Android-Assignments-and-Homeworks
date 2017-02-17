/**
 * Assignment: Homework 04
 * File: GetQuestions.java
 * Sujal T V, Sanju K H
 */

package com.example.tvs.triviaquiz;

import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by TVS on 23/9/16.
 */

public class GetQuestions extends AsyncTask<String, Void, String>{
    QuestionApiResponse context;

    GetQuestions(QuestionApiResponse context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        BufferedReader data = null;
        try {
            URL baseUrl = new URL(params[0]);
            HttpURLConnection connexion = (HttpURLConnection) baseUrl.openConnection();
            connexion.setRequestMethod("GET");
            connexion.connect();
            int connexionStatus = connexion.getResponseCode();
            if(connexionStatus != HttpURLConnection.HTTP_OK) return null;
            data = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line = "";
            while((line=data.readLine())!=null)
                response.append(line + "\n");
            return response.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        try {
            context.handleResponse(Question.parseResponse(response));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    interface QuestionApiResponse {
        void handleResponse(ArrayList<Question> questions);
    }
}
