package com.example.himanshu.technologynews;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Himanshu on 9/26/2016.
 */
public class GetNewsItemsAsyncTask extends AsyncTask<String, Void, ArrayList<Technology>> {
    INewsData newsData;

    public GetNewsItemsAsyncTask(INewsData newsData) {
        this.newsData = newsData;
    }

    @Override
    protected ArrayList<Technology> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int statusCode = connection.getResponseCode();
            if(statusCode==HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();

                return TechnologyUtil.TechnologyPullParser.parseTechnologyItems(inputStream);
            }}
        catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Technology> technologies) {
        super.onPostExecute(technologies);
/*
        Collections.sort(technologies, new Comparator<Technology>() {
            @Override
            public int compare(Technology o1, Technology o2) {
                return o1.getPub_date().compareTo(o2.getPub_date());
            }
        });*/
        newsData.setUpData(technologies);
    }

    public static interface INewsData
    {
        public void setUpData(ArrayList<Technology> questions);
    }


}