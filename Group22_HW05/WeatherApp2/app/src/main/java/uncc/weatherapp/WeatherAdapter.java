/*
Assignment: 07
Name: Sanju Kurubara Budi Hall Hriyanna Gowda
 */
package uncc.weatherapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sanju on 10/3/2016.
 */

public class WeatherAdapter extends ArrayAdapter<Weather.HourlyForecastBean> {
    List<Weather.HourlyForecastBean> mData;
    Context mContext;
    int mResource;

    public WeatherAdapter(Context context, int resource, List<Weather.HourlyForecastBean> objects) {
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

        Weather.HourlyForecastBean weatherHourlyForecastBean = mData.get(position);
        TextView cloudy = (TextView) convertView.findViewById(R.id.textViewCloudy);
        cloudy.setText(weatherHourlyForecastBean.getCondition());

        TextView time = (TextView) convertView.findViewById(R.id.textViewTime);
        time.setText(weatherHourlyForecastBean.getFCTTIME().getCivil());

        TextView temperature = (TextView) convertView.findViewById(R.id.textViewDegree);
        temperature.setText(weatherHourlyForecastBean.getTemp().getEnglish());

        ImageView image = (ImageView) convertView.findViewById(R.id.imageViewNews);
        Picasso.with(mContext).load(weatherHourlyForecastBean.getIcon_url()).into(image);
        /*
        TextView newsText = (TextView) convertView.findViewById(R.id.textViewNews);

        Weather newsData = mData.get(position);

        newsText.setText(newsData.getTitle());

        //colorValue.setText(color.getColorHex());
        ImageView image = (ImageView) convertView.findViewById(R.id.imageViewNews);
        Picasso.with(mContext).load(newsData.getThumbnailUrl()).into(image);
        if (MainActivity.searchMode == true && MainActivity.searchCount > position)
            convertView.setBackgroundColor(Color.BLUE);
        else
            convertView.setBackgroundColor(Color.WHITE);
            */
        return convertView;
    }
}