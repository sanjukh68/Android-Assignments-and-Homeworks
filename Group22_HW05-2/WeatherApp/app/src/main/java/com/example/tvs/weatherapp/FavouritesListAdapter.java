package com.example.tvs.weatherapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FavouritesListAdapter extends ArrayAdapter<CityWeather> {

    Context context;
    int resource;
    List<CityWeather> objects;

    public FavouritesListAdapter(Context context, int resource, List<CityWeather> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, parent, false);
        }

        CityWeather cityWeather = objects.get(position);
        ((TextView) convertView.findViewById(R.id.favouriteCity)).setText(cityWeather.city);
        ((TextView) convertView.findViewById(R.id.favoutiteTemp)).setText(cityWeather.avgTemp.english + " F");
        ((TextView) convertView.findViewById(R.id.textVilastUpdated))
                .setText("Updated on: " + cityWeather.dateUpdated);
        return convertView;
    }
}