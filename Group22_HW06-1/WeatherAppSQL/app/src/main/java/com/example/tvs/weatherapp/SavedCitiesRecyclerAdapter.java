package com.example.tvs.weatherapp;

import android.content.Context;
import android.support.v7.view.menu.SubMenuBuilder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;

public class SavedCitiesRecyclerAdapter extends RecyclerView.Adapter<SavedCitiesRecyclerAdapter.ViewHolder> {

    Context context;
    int resource;
    ArrayList<City> objects;

    public SavedCitiesRecyclerAdapter(Context context, int resource, ArrayList<City> objects) {
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView city, temperature, updatedOn;
        ImageView star;
        OnClickEvents onClickEvents;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.onClickEvents = (OnClickEvents) context;
            this.city = (TextView) itemView.findViewById(R.id.favouriteCity);
            this.temperature = (TextView) itemView.findViewById(R.id.favoutiteTemp);
            this.updatedOn = (TextView) itemView.findViewById(R.id.lastUpdated);
            this.star = (ImageView) itemView.findViewById(R.id.star);

            city.setOnClickListener(this);
            temperature.setOnClickListener(this);
            updatedOn.setOnClickListener(this);
            star.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.star: onClickEvents.onRateClicked(getAdapterPosition(), v);
                                break;
                default:        onClickEvents.onClicked(getAdapterPosition());
                                break;
            }
        }

       /* @Override
        public boolean onLongClick(View v) {
            Log.d("Long", "Long click");
            onClickEvents.onLongClick(getAdapterPosition());
            return true;
        }*/

        public interface OnClickEvents {
            void onClicked(int position);
            /*void onLongClick(int position);*/
            void onRateClicked(int position, View v);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, null);
        SavedCitiesRecyclerAdapter.ViewHolder vh = new SavedCitiesRecyclerAdapter.ViewHolder(view, context);
        return vh;
    }

    @Override
    public void onBindViewHolder(SavedCitiesRecyclerAdapter.ViewHolder holder, int position) {
        holder.city.setText(objects.get(position).getCity() + ", " + objects.get(position).getCountry());
        if(MainActivity.TEMP_UNIT==0)
            holder.temperature.setText(Double.toString(MainActivity.round(objects.get(position).getTemp(), 2)) + "O C");
        else holder.temperature.setText(Double.toString(MainActivity.round(objects.get(position).getTemp() + 273.15, 2)) + "O F");
        holder.updatedOn.setText(objects.get(position).getUpdatedOn());
        if(objects.get(position).getFavourite()==0)
            holder.star.setImageResource(R.drawable.star_gray);
        else holder.star.setImageResource(R.drawable.star_gold);
    }


    @Override
    public int getItemCount() {
        return objects.size();
    }
}
