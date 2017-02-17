package com.example.tvs.weatherapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DailyWeatherRecyclerAdapter extends RecyclerView.Adapter<DailyWeatherRecyclerAdapter.ViewHolder> {

    Context context;
    int resource;
    List<WeatherResponse.CustomDetails> objects;

    public DailyWeatherRecyclerAdapter(Context context, int resource, List<WeatherResponse.CustomDetails> objects) {
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dailyDate, dailyTemp;
        public ImageView dailyPic;

        public ViewHolder(View itemView) {
            super(itemView);
            this.dailyDate = (TextView) itemView.findViewById(R.id.dailyDate);
            this.dailyTemp = (TextView) itemView.findViewById(R.id.dailyTemp);
            this.dailyPic =  (ImageView) itemView.findViewById(R.id.dailyImage);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, null);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.dailyDate.setText(objects.get(position).date);
        Picasso.with(context).load(CityWeather.IMAGE_ICON_URL.replace("%iconNum%", objects.get(position).icon)).into(holder.dailyPic);
        if(MainActivity.TEMP_UNIT==0)
            holder.dailyTemp.setText(objects.get(position).temp + "o C");
        else holder.dailyTemp.setText(MainActivity.round(Double.parseDouble(objects.get(position).temp) + 273.15, 2) + "o F");
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }
}
