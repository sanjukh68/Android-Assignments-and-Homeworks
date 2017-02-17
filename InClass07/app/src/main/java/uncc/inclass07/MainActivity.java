/*
Assignment: 07
Name: Sanju Kurubara Budi Hall Hriyanna Gowda
 */
package uncc.inclass07;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SearchView;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //LinearLayout layout;
    ProgressDialog dialog;
    ArrayList newsList;
    ListView listview;
    NewsAdapter adapter;
    EditText searchView;
    public static boolean searchMode;
    public static int searchCount;
    //static ImageView thumbNail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsList = new ArrayList<NewsData>();

        listview = (ListView) findViewById(R.id.listViewId);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Images...");
        dialog.show();
        new ReadNews(this).execute("https://itunes.apple.com/us/rss/toppodcasts/limit=30/xml");

        Button goButton = (Button) findViewById(R.id.buttonGo);
        goButton.setOnClickListener((View.OnClickListener) this);

        Button clearButton = (Button) findViewById(R.id.buttonClear);
        clearButton.setOnClickListener((View.OnClickListener) this);

        searchView = (EditText) findViewById(R.id.searchBarId);

    }

    public void setData(NewsData news) {

        if (news != null)
        {
            newsList.add(news);
        }
    }

    public  void FillListView()
    {
        sortNews();
        adapter = new NewsAdapter(this, R.layout.row_layout, newsList);
        listview.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("demo", "Position " + i + ", Value " + newsList.get(i).toString());
                Intent  intent = new Intent(getBaseContext(),NewsDetails.class);
                NewsData news = (NewsData) newsList.get(i);
                intent.putExtra("NEWS", news);
                Log.d("demo", "sending intent");
                startActivity(intent);
            }
        });
    }
    public void setImage(Bitmap bitmap) {
        Log.d("set image",""+bitmap.toString());
        /*
        thumbNail.setImageBitmap(bitmap);
        */

    }


    @Override
    public void onClick(View view) {

        ArrayList<NewsData> searchedData = new ArrayList<NewsData>();
        searchCount = 0;
        switch (view.getId())
        {
            case R.id.buttonGo:
                //adapter.getFilter().filter(searchView.getText());
                for  (int i = 0; i < newsList.size(); i++)
                {
                    NewsData news = (NewsData) newsList.get(i);
                    if (news.getTitle().toLowerCase().contains(searchView.getText().toString().toLowerCase()))
                    {
                        newsList.add(0, (NewsData) newsList.get(i));
                        adapter.add((NewsData) newsList.get(i));
                        adapter.sort(new Comparator<NewsData>() {
                            @Override
                            public int compare(NewsData newsData, NewsData t1) {
                                return 0;
                            }

                            @Override
                            public boolean equals(Object o) {
                                return false;
                            }
                        });
                        newsList.remove(i+1);
                        searchCount++;
                        searchMode = true;
                    }
                }

                adapter.notifyDataSetChanged();
                //newsList = searchedData;
                break;
            case R.id.buttonClear:
                searchCount = 0;
                searchMode = false;
                searchView.setText("");
                adapter.getFilter().filter("");
                sortNews();
                adapter.notifyDataSetChanged();
                break;
        }
    }
    public void sortNews()
    {
        Collections.sort(newsList, new Comparator<NewsData>() {
            @Override
            public int compare(NewsData lhs, NewsData rhs) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                try {
                    Date startDate = df.parse(lhs.getPubDate());
                    Date startDater = df.parse(rhs.getPubDate());
                    return -(startDate.compareTo(startDater));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }
}

