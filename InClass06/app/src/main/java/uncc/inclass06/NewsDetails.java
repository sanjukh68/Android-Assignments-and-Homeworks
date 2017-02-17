package uncc.inclass06;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        if(getIntent().getExtras()!=null)
        {
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

            //((TextView)findViewById(R.id.textViewContent)).setText(android.text.Html.fromHtml(setNews.getDescription()).toString());
            ((TextView)findViewById(R.id.textViewDate)).setText(setNews.getPubDate());
            ((TextView)findViewById(R.id.textViewDate)).setText(setNews.getPubDate());
            new GetImage(NewsDetails.this).execute(setNews.getUrl());
        }
    }
    public void setNewsImage(Bitmap bitmap) {
        Log.d("set image",""+bitmap.toString());
        ((ImageView)findViewById(R.id.imageViewNews)).setImageBitmap(bitmap);
    }
}
