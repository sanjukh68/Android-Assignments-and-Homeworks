package com.example.himanshu.technologynews;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Xml;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class NewsDetails extends AppCompatActivity implements GetImagesAsyncTask.INewsImages{
Technology technology;
    ImageView imageView_newsImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        if (getIntent().getExtras() != null) {

            technology= (Technology)getIntent().getExtras().getSerializable(MainActivity.SINGLE_LIST);
            TextView textView_title=(TextView)findViewById(R.id.textView_title);
            textView_title.setText(technology.getTitle());

            TextView textView_date=(TextView)findViewById(R.id.textView_date);
            java.text.SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            String formattedDate = sdf.format(technology.getPub_date());
            textView_date.setText(formattedDate);

            TextView textView_description=(TextView)findViewById(R.id.textView_description);
            textView_description.setText(Html.fromHtml(technology.getNews_description().toString()));

            imageView_newsImage=(ImageView)findViewById(R.id.imageView);
            new GetImagesAsyncTask(NewsDetails.this).execute(technology.getLarge_image_url(), "");
        }
    }

    @Override
    public void setUpImages(Bitmap bitmap, String index) {
        imageView_newsImage.setImageBitmap(bitmap);
    }
}
