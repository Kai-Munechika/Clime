package edu.kaimbu.stormy3.weather;

/**
 * Created by KaiM on 11/23/16.
 */

public class Hour {

    private long mTime;
    private String mSummary;
    private String mIcon;
    private double mTemperature;
    private String mTimezone;





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

    public double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }
}
