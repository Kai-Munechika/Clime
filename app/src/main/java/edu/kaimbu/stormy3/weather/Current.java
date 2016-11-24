package edu.kaimbu.stormy3.weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by KaiM on 11/22/16.
 */

public class Current {
    private String mIcon;
    private long mTime;
    private int mTemperature;
    private double mHumidity;
    private int mPrecipChance;
    private String mSummary;
    private String mTimezone;
    private double mApparentTemperature;

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimezone));
        Date dateTime = new Date(mTime * 1000);
        String timeString = formatter.format(dateTime);
        return timeString;
    }

    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }






    /* Getters and Setters */

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        mTemperature = (int) Math.round(temperature);
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public int getPrecipChance() {
        return mPrecipChance;
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = (int) Math.round(precipChance * 100);
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public double getApparentTemperature() {
        return mApparentTemperature;
    }
    public void setApparentTemperature(double apparentTemperature) {
        mApparentTemperature = apparentTemperature;
    }
    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }

    public String getTimezone() {
        return mTimezone;
    }


}
