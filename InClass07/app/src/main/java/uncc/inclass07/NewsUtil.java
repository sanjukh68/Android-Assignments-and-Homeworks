/*
Assignment: 07
Name: Sanju Kurubara Budi Hall Hriyanna Gowda
 */

package uncc.inclass07;

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
                        if (parser.getName().equals("entry")) {
                            news = new NewsData();
                            news.setId(newsId++);
                        } else if (parser.getName().equals("title")) {
                            if(news != null)
                            news.setTitle(parser.nextText());
                        } else if (parser.getName().equals("summary")) {
                            if(news != null)
                            news.setDescription(parser.nextText().toString());
                        } else if (parser.getName().equals("im:releaseDate")) {
                            String line="";
                            if(news != null)
                            {
                                line = parser.nextText();
                                news.setPubDate(line);
                            }
                            Log.d("demo", "release date" + news.getPubDate()+"---"+line);
                        } else if (parser.getName().equals("im:image")) {

                             String height = parser.getAttributeValue(null, "height");
                                //news.setThumbnailUrl(parser.getAttributeValue(null, "url"));
                                 news.setThumbnailUrl(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("entry"))
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
