package com.example.tvs.weatherapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class WeatherListAdapter extends ArrayAdapter<HourlyForecast>{
    Context context;
    int resource;
    List<HourlyForecast> objects;

    public WeatherListAdapter(Context context, int resource, List<HourlyForecast> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);
        }

        HourlyForecast detail = objects.get(position);

        Picasso.with(context).load(detail.icon_url).into((ImageView) convertView.findViewById(R.id.thumbnail));
        ((TextView) convertView.findViewById(R.id.time)).setText(detail.FCTTIME.civil);
        ((TextView) convertView.findViewById(R.id.condition)).setText(detail.condition);
        ((TextView) convertView.findViewById(R.id.temperature)).setText(detail.temp.english + " F");

        return convertView;
    }
}