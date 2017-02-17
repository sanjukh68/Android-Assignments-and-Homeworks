package com.example.himanshu.technologynews;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Himanshu on 9/26/2016.
 */
public class Technology implements Serializable {
String title, news_description, link, thumbnail_url, large_image_url;
    Date pub_date;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNews_description() {
        return news_description;
    }

    public void setNews_description(String news_description) {
        this.news_description = news_description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getLarge_image_url() {
        return large_image_url;
    }

    public void setLarge_image_url(String large_image_url) {
        this.large_image_url = large_image_url;
    }



    public Date getPub_date() {
        return pub_date;
    }

    public void setPub_date(Date pub_date) {
        this.pub_date = pub_date;
    }

    @Override
    public String toString() {
        return "Technology{" +
                "title='" + title + '\'' +
                ", news_description='" + news_description + '\'' +
                ", link='" + link + '\'' +
                ", thumbnail_url='" + thumbnail_url + '\'' +
                ", large_image_url='" + large_image_url + '\'' +
                ", pub_date=" + pub_date +
                '}';
    }

}
