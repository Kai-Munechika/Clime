package edu.kaimbu.stormy3.weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by KaiM on 11/23/16.
 */

public class Day implements Parcelable{

    private long mTime;
    private String mSummary;
    private String mIcon;
    private int mTemperatureMax;
    private String mTimezone;


    // empty constructor; needed since default constructor isn't used when making this call
    // for some reason
    public Day() {}

    /* Parcelable methods */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mTime);
        dest.writeString(mSummary);
        dest.writeString(mIcon);
        dest.writeInt(mTemperatureMax);
        dest.writeString(mTimezone);
    }

    public Day(Parcel in) {
        mTime = in.readLong();
        mSummary = in.readString();
        mIcon = in.readString();
        mTemperatureMax = in.readInt();
        mTimezone = in.readString();
    }

    // generates instances of Day from a parcel
    public static final Creator<Day> CREATOR = new Creator<Day>() {

        //Create a new instance of the Parcelable class, instantiating it from the given Parcel
        // whose data had previously been written by Parcelable.writeToParcel().
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        //Create a new array of the Parcelable class.
        //used for generating parcelable array
        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    /* Getters and Setters */

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public int getTemperatureMax() {
        return mTemperatureMax;
    }

    public void setTemperatureMax(double temperatureMax) {
        mTemperatureMax = (int) Math.round(temperatureMax);
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }
    public String getDayOfTheWeek() {
        //need to convert mTime (seconds
        Date date = new Date(mTime*1000);
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimezone));
        return formatter.format(date);

    }


}
