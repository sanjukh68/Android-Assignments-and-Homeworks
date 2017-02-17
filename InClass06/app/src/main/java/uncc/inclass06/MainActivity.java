package uncc.inclass06;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {
    LinearLayout layout;
    ProgressDialog dialog;
    static ImageView thumbNail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thumbNail = null;
        layout= (LinearLayout) findViewById(R.id.layout_vertical);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Images...");
        dialog.show();
        new ReadNews(this).execute("http://rss.cnn.com/rss/cnn_tech.rss");

    }

    public void setData(NewsData news) {

        LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        //ll.setId(news.getId());
        thumbNail=new ImageView(this);
        thumbNail.setId(news.getId());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);//ViewGroup.LayoutParams.WRAP_CONTENT);
        ll.setLayoutParams(params);
        //thumbNail.setPadding(50,50,50,50);

//        thumbNail.setMaxHeight(120);
//        thumbNail.setLayoutParams(params);
        Log.d("url",""+news.getThumbnailUrl());
        Log.d("url2",""+news.getUrl());
        //.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);

        new GetImage(this,news.getId()).execute(news.getThumbnailUrl());
        TextView txt= new TextView(this);
        txt.setText(news.getTitle());
        txt.setClickable(true);
        //txt.setId(news.getId());
        ll.addView(thumbNail);
        ll.addView(txt);
        ll.setClickable(true);
        layout.addView(ll);
        addClickable(ll,txt,news);
        //thumbNail = null;
    }

    public void setImage(Bitmap bitmap) {
        Log.d("set image",""+bitmap.toString());
        thumbNail.setImageBitmap(bitmap);
    }


    private void addClickable(LinearLayout ll, TextView tv, final NewsData news) {
        Log.d("demo","adding listener");
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(getBaseContext(),NewsDetails.class);
                intent.putExtra("NEWS",news);
                startActivity(intent);
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(getBaseContext(),NewsDetails.class);
                intent.putExtra("NEWS",news);
                startActivity(intent);
            }
        });
    }
}
