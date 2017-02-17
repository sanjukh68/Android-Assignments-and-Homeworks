/*
Assignment: 07
Name: Sanju Kurubara Budi Hall Hriyanna Gowda
 */
package uncc.inclass07;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewsDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_details);
        Log.d("demo", "NewsDetails 0");
        if(getIntent().getExtras()!=null)
        {
            Log.d("demo", "NewsDetails 1");
            NewsData setNews = (NewsData) getIntent().getExtras().getSerializable("NEWS");
            ((TextView)findViewById(R.id.textViewTitle)).setText(setNews.getTitle());
            //((TextView)findViewById(R.id.textViewContent)).setText(setNews.getDescription());
            String line = setNews.getDescription().replaceAll("\\<.*?>","");
            if(line.trim().equals("")|| line==null)
            {
                ((TextView)findViewById(R.id.textViewContent)).setTextColor(Color.parseColor("#FF0000"));
                ((TextView)findViewById(R.id.textViewContent)).setText("Nothing to display!");
            }
            else
            ((TextView)findViewById(R.id.textViewContent)).setText(line);
           // ((TextView)findViewById(R.id.textViewContent)).setText(Html.fromHtml(setNews.getDescription()));
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            //  Date startDate = df.parse(setPodcast.getUpdatedDate());
            Date startDate;
            try {
                startDate = df.parse(setNews.getPubDate());
                line = df.format(startDate);
                DateFormat newDf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                line = newDf.format(startDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            //((TextView)findViewById(R.id.textViewContent)).setText(android.text.Html.fromHtml(setNews.getDescription()).toString());
            ((TextView)findViewById(R.id.textViewDate)).setText(line);
            //((TextView)findViewById(R.id.textViewDate)).setText(setNews.getPubDate());

            ImageView image = (ImageView)findViewById(R.id.imageViewNews);
            Picasso.with(this).load(setNews.getThumbnailUrl()).into(image);
            Log.d("demo", "NewsDetails 2");
            //new GetImage(NewsDetails.this).execute(setNews.getUrl());
        }
    }
    public void setNewsImage(Bitmap bitmap) {
        Log.d("set image",""+bitmap.toString());
        ((ImageView)findViewById(R.id.imageViewNews)).setImageBitmap(bitmap);
    }
}
