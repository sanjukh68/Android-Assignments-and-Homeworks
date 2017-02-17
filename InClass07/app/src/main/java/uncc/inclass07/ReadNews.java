/*
Assignment: 07
Name: Sanju Kurubara Budi Hall Hriyanna Gowda
 */

package uncc.inclass07;

import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sanju on 9/26/2016.
 */

public class ReadNews extends AsyncTask<String,Void,ArrayList<NewsData>> {

    MainActivity activity;
    NewsDetails newsActivity;
    public ReadNews(MainActivity activity) {
        this.activity = activity;
    }
    public ReadNews(NewsDetails activity) {
        this.newsActivity = activity;
    }
    @Override
    protected ArrayList<NewsData> doInBackground(String... strings) {


    try {
        URL url = new URL(strings[0]);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();

        con.setRequestMethod("GET");
        con.connect();

        int statusCode = con.getResponseCode();
        if (statusCode == HttpURLConnection.HTTP_OK)
        {
            InputStream in = con.getInputStream();
            //return PersonsUtil.PersonSAXParser.parsePerson(in);
            return NewsUtil.NewsPullParser.parseNews(in);
        }
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (SAXException e) {
        e.printStackTrace();
    } catch (XmlPullParserException e) {
        e.printStackTrace();
    }
    return null;
}

    @Override
    protected void onPostExecute(ArrayList<NewsData> strings) {
        super.onPostExecute(strings);

        for(int i=0;i<strings.size();i++) {
            activity.setData(strings.get(i));
        }

        activity.FillListView();
        activity.dialog.dismiss();
    }
}
