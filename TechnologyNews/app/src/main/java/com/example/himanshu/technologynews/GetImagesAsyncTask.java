package com.example.himanshu.technologynews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Himanshu on 9/26/2016.
 */
public class GetImagesAsyncTask extends AsyncTask<String, Void, Bitmap> {
    INewsImages news_images;
    String index;

    //TriviaActivity triviaActivity;
    public GetImagesAsyncTask(INewsImages news_images) {
        this.news_images = news_images;
    }


    @Override
    protected Bitmap doInBackground(String... params) {
Log.d("demo","IN ASYNC IMAGES");
        try {

            URL url = new URL(params[0]);
            index=params[1];
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setRequestMethod("GET");
            con.connect();
            int status_code = con.getResponseCode();
            if (status_code == HttpURLConnection.HTTP_OK) {

                int response = con.getResponseCode();
                InputStream in = con.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                return bitmap;
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {


        try {
            news_images.setUpImages(bitmap,index);
        } catch (Exception e) {
            Log.d("demo", e.toString());
        }
        super.onPostExecute(bitmap);


    }

    public static interface INewsImages {
        public void setUpImages(Bitmap bitmap, String index);
    }

}