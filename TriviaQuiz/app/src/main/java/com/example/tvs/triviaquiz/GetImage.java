/**
 * Assignment: Homework 04
 * File: GetImage.java
 * Sujal T V, Sanju K H
 */

package com.example.tvs.triviaquiz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by TVS on 23/9/16.
 */

public class GetImage extends AsyncTask<String, Void, Bitmap> {
    ImageApiResponse context;

    GetImage(ImageApiResponse context) {
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap image;
        try {
            URL imageUrl = new URL(params[0]);
            HttpURLConnection connexion = (HttpURLConnection) imageUrl.openConnection();
            connexion.setRequestMethod("GET");
            connexion.connect();
            int responseCode = connexion.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) return null;
            image = BitmapFactory.decodeStream(imageUrl.openStream());
            return image;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap response) {
        context.handleResponse(response);
    }

    interface ImageApiResponse {
        void handleResponse(Bitmap image);
    }
}
