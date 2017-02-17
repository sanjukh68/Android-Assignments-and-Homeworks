package com.example.himanshu.technologynews;

import android.icu.util.Calendar;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Himanshu on 9/26/2016.
 */
public class TechnologyUtil  {

    static public class TechnologyPullParser
    {
        static public ArrayList<Technology> parseTechnologyItems(InputStream inputStream) throws XmlPullParserException, IOException, ParseException {
            XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            Technology technology=null;
            ArrayList<Technology> newsItemsArrayList = new ArrayList<Technology>();
            xmlPullParser.setInput(inputStream,"UTF-8");
            int event = xmlPullParser.getEventType();
            while (event!=XmlPullParser.END_DOCUMENT)
            {
                switch (event) {
                    case XmlPullParser.START_TAG:

                        if (xmlPullParser.getName().equals("item")) {
                            technology = new Technology();
                        }
                        else if(technology!=null)
                        {
                        if (xmlPullParser.getName().equals("title")) {

                                technology.setTitle(xmlPullParser.nextText().trim());

                        }
                        else if (xmlPullParser.getName().equals("link")) {
                            technology.setLink(xmlPullParser.nextText().trim());
                        }
                        else if (xmlPullParser.getName().equals("pubDate")) {
                            //String myFormat = "yyyy-MM-dd";
                            //SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                            //Date dateObj = sdf.parse(xmlPullParser.nextText().trim());
                            //Calendar myCal = Calendar.getInstance();
                            //myCal.setTime(dateObj);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
                            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                            Date date = dateFormat.parse(xmlPullParser.nextText().trim());
                            technology.setPub_date(date);
                        }
                        else if (xmlPullParser.getName().equals("description")) {
                            technology.setNews_description(xmlPullParser.nextText().trim());
                        }
                        else if (xmlPullParser.getName().equals("media:content") && xmlPullParser.getAttributeValue(null,"height").equals("144")) {
                            technology.setThumbnail_url(xmlPullParser.getAttributeValue(null,"url"));
                        }
                        else if (xmlPullParser.getName().equals("media:content") && xmlPullParser.getAttributeValue(null,"height").equals("300")) {
                            technology.setLarge_image_url(xmlPullParser.getAttributeValue(null,"url"));
                        }}
break;

                    case XmlPullParser.END_TAG:
                        if (xmlPullParser.getName().equals("item")) {
                            newsItemsArrayList.add(technology);
                            technology=null;
                        }
                    break;
                    default: break;

                }

            event=xmlPullParser.next();
            }
            return newsItemsArrayList;
        }
    }
}