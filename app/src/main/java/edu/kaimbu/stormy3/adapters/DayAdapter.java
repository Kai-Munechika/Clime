package edu.kaimbu.stormy3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edu.kaimbu.stormy3.R;
import edu.kaimbu.stormy3.weather.Day;

/**
 * Created by KaiM on 11/23/16.
 */

public class DayAdapter extends BaseAdapter{

    private Context mContext;
    private Day[] mDays;

    public DayAdapter(Context context, Day[] days) {
        mContext = context;
        mDays = days;
    }




    @Override
    public int getCount() {
        return mDays.length;
    }

    @Override
    public Object getItem(int position) {
        return mDays[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();

            //Initially, all of the views of the convertView have default values set in the layout;
            // we point the holder's references/fields to these views
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.iconImageView);
            holder.temperatureLabel = (TextView) convertView.findViewById(R.id.temperatureLabel);
            holder.dayLabel = (TextView) convertView.findViewById(R.id.dayNameLabel);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Day day = mDays[position];

        // the references stored in the holder point to the same Views in the convertView
        // we update the values in the view with the contents of Day
        holder.iconImageView.setImageResource(day.getIconId());
        holder.temperatureLabel.setText(String.valueOf(day.getTemperatureMax()));
        if(position == 0) {
            holder.dayLabel.setText("Today");
        }
        else {
            holder.dayLabel.setText(day.getDayOfTheWeek());
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView iconImageView;
        TextView temperatureLabel;
        TextView dayLabel;
    }
}
