/*
Assignment: 07
Name: Sanju Kurubara Budi Hall Hriyanna Gowda
 */
package uncc.inclass07;

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

    public void setPubDate(String pubDate) {
        Date date = null;
        SimpleDateFormat format2 = null;
        String line = " ";
        Log.d("DATE",pubDate);
        this.pubDate = pubDate;

    }

    @Override
    public String toString() {
        return "NewsData{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", id=" + id +
                '}';
    }
}
