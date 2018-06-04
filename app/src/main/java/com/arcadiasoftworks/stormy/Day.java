package com.arcadiasoftworks.stormy;


public class Day {
    private long mTime;
    private String mSummary;
    private double mTempertare;
    private String mIcon;
    private String mTimezone;


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

    public double getTempertare() {
        return mTempertare;
    }

    public void setTempertare(double tempertare) {
        mTempertare = tempertare;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getTimezone() {
        return mTimezone;
    }

    public void setTimezone(String timezone) {
        mTimezone = timezone;
    }
}
