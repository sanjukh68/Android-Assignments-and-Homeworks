package com.example.himanshu.technologynews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetNewsItemsAsyncTask.INewsData,GetImagesAsyncTask.INewsImages{
static final String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
    public static final String SINGLE_LIST="single_list";
    static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    ProgressDialog progressDialog;
    LinearLayout linearLayout;
    ImageView imageView;
    TextView textView;
    LinearLayout linearLayout_items;
    ArrayList<Technology> OriginialNewsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout_items=(LinearLayout) findViewById(R.id.linearLayout_items);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading News");
        progressDialog.show();
        new GetNewsItemsAsyncTask(this).execute("http://rss.cnn.com/rss/cnn_tech.rss");


    }

    @Override
    public void setUpData(ArrayList<Technology> questions) {
        progressDialog.dismiss();
        OriginialNewsArrayList = questions;

        for(int i=0;i<OriginialNewsArrayList.size();i++)
        {
            new GetImagesAsyncTask(this).execute(OriginialNewsArrayList.get(i).getThumbnail_url(),Integer.toString(i));
            linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150));
            linearLayout.setId(i);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            textView = new TextView(this);
            textView.setText(OriginialNewsArrayList.get(i).getTitle());
            linearLayout.addView(textView);
            linearLayout_items.addView(linearLayout);
            //customOnClickListner temp = new customOnClickListner(i);
            //temp.onClick(linearLayout);

            linearLayout.setOnClickListener(new customOnClickListner(i));

           /* linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, NewsDetails.class);
                    intent.putExtra(SINGLE_LIST, (Serializable) OriginialNewsArrayList.get(0));
                    startActivity(intent);
                }
            });
            linearLayout.*/

        }


    }

    @Override
    public void setUpImages(Bitmap bitmap, String index) {
        LinearLayout linear_temp=(LinearLayout)findViewById(Integer.parseInt(index));
        imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        //RelativeLayout.LayoutParams x = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //x.addRule(Integer.parseInt(index), LinearLayout.TEXT_ALIGNMENT_VIEW_START);
        //imageView.setLayoutParams(x);
        linear_temp.addView(imageView, 0);
    }

    public class customOnClickListner implements View.OnClickListener {
        int index = 0;

        public customOnClickListner(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, NewsDetails.class);
            intent.putExtra(SINGLE_LIST, OriginialNewsArrayList.get(index));
            startActivity(intent);
        }
    }
}
