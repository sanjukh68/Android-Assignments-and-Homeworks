package uncc.inclass06;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sanju on 9/26/2016.
 */

public class NewsData implements Serializable{

    String title, url, description, pubDate, thumbnailUrl;
    int id;

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setPubDate(String pubDate) {
        Date date = null;
        SimpleDateFormat format2 = null;
        String line = " ";
        Log.d("DATE",pubDate);
            try {
                format2 = new SimpleDateFormat("MM/dd/yyyy HH:mm a", Locale.ENGLISH);
                date = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH).parse(pubDate);
                Log.d("DATE","hiii"+date.toString());
                line = format2.format(date);
            } catch (ParseException e) {
                e.printStackTrace();

        }
        Log.d("DATE","hwllo"+line);
        //  SimpleDateFormat format = new SimpleDateFormat();
        this.pubDate = line;
    }

    @Override
    public String toString() {
        return "NewsData{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +

                ", pubDate='" + pubDate + '\'' +
                ", id=" + id +
                '}';
    }
}
