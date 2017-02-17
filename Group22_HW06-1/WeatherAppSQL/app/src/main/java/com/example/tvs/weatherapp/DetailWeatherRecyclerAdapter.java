package com.example.tvs.weatherapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;

public class DetailWeatherRecyclerAdapter extends RecyclerView.Adapter<DetailWeatherRecyclerAdapter.ViewHolder> {

    Context context;
    int resource;
    ArrayList<WeatherResponse.WeatherList> objects;

    public DetailWeatherRecyclerAdapter(Context context, int resource, ArrayList<WeatherResponse.WeatherList> objects) {
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView time, temperature, pressure, condition, humidity, wind;
        ImageView iconLarge;

        public ViewHolder(View itemView) {
            super(itemView);
            this.time = (TextView) itemView.findViewById(R.id.time);
            this.temperature = (TextView) itemView.findViewById(R.id.temp);
            this.pressure = (TextView) itemView.findViewById(R.id.pressure);
            this.condition = (TextView) itemView.findViewById(R.id.condition);
            this.humidity = (TextView) itemView.findViewById(R.id.humidity);
            this.wind = (TextView) itemView.findViewById(R.id.wind);
            this.iconLarge = (ImageView) itemView.findViewById(R.id.iconLarge);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        DetailWeatherRecyclerAdapter.ViewHolder vh = new DetailWeatherRecyclerAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.time.setText(MainActivity.rqTmFrmt.format(MainActivity.inptDtFrmt.parse(objects.get(position).dt_txt)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(MainActivity.TEMP_UNIT==0)
            holder.temperature.setText(objects.get(position).main.temp + "o C");
        else holder.temperature.setText(MainActivity.round(objects.get(position).main.temp + 273.15, 2) + "o F");
        holder.pressure.setText(objects.get(position).main.pressure.toString());
        holder.condition.setText(objects.get(position).weather.get(0).description);
        holder.humidity.setText(objects.get(position).main.humidity.toString());
        holder.wind.setText(objects.get(position).wind.speed + " mps, " + objects.get(position).wind.deg);
        Picasso.with(context).load(CityWeather.IMAGE_ICON_URL.replace("%iconNum%", objects.get(position).weather.get(0).icon)).into(holder.iconLarge);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }
}
