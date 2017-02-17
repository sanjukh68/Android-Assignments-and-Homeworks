/*
Assignment: 07
Name: Sanju Kurubara Budi Hall Hriyanna Gowda
 */
package uncc.inclass07;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sanju on 10/3/2016.
 */

public class NewsAdapter extends ArrayAdapter<NewsData> {
    List<NewsData> mData;
    Context mContext;
    int mResource;

    public NewsAdapter(Context context, int resource, List<NewsData> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        NewsData newsData = mData.get(position);

        TextView newsText = (TextView) convertView.findViewById(R.id.textViewNews);
        newsText.setText(newsData.getTitle());

        ImageView image = (ImageView) convertView.findViewById(R.id.imageViewNews);
        Picasso.with(mContext).load(newsData.getThumbnailUrl()).into(image);
        if (MainActivity.searchMode == true && MainActivity.searchCount > position)
            convertView.setBackgroundColor(Color.BLUE);
        else
            convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }
}