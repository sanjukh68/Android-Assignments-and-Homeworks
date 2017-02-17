package uncc.inclass06;

import android.util.Log;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by sanju on 9/26/2016.
 */

public class NewsUtil {

    static public class NewsPullParser
    {
        static int newsId = 0;
        static ArrayList<NewsData> newsList;

        static public ArrayList<NewsData> parseNews(InputStream in) throws IOException, SAXException, XmlPullParserException {

            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in,"UTF-8");
            NewsData news = null;
            newsList = new ArrayList<NewsData>();
            int event = parser.getEventType();
            while(event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("item")) {
                            news = new NewsData();
                            news.setId(newsId++);
                            //Log.d("")
                            //news.setTitle(parser.getName().equals("age"));
                        } else if (parser.getName().equals("title")) {
                            if(news != null)
                            news.setTitle(parser.nextText());
                        } else if (parser.getName().equals("description")) {
                            if(news != null)
                            news.setDescription(parser.nextText().toString());
                        } /*else if (parser.getName().equals("link")) {
                            news.setUrl(parser.nextText());
                        }*/
                        else if (parser.getName().equals("pubDate")) {
                            if(news != null)
                            news.setPubDate(parser.nextText());
                        }
                        else if (parser.getName().equals("media:group")) {
                            //news.setPubDate(parser.nextText());
                        } else if (parser.getName().equals("media:content")) {

                                String height = parser.getAttributeValue(null, "height");
                                String width = parser.getAttributeValue(null, "width");
                                Log.d("demo", "width and Heihjt" + width + height);
                                if (height.equals(width)) {
                                        Log.d("Set","Setting the url same dimen");
                                        news.setThumbnailUrl(parser.getAttributeValue(null, "url"));
                                        Log.d("urlUtil",""+news.getThumbnailUrl());

                                }
                                else{
                                    Log.d("Set","Setting the url diff dimen");

                                    news.setUrl(parser.getAttributeValue(null, "url"));
                                    Log.d("urlUtil2",""+news.getUrl());
                                    }
                            Log.d("Set","url set");
                                }



                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item"))
                        {
                            if(news != null)
                                newsList.add(news);
                            news = null;

                        }

                        break;
                    default:
                        break;
                }

                event = parser.next();
            }
            return newsList;

        }
    }
}
