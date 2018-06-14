package com.arcadiasoftworks.stormy.weather;


import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;

import java.util.Date;


public class Hour {
    private long time;
    private String summary;
    private double temperature;
    private String icon;
    private String timeZone;


    public Hour() {
    }

    public Hour(long time, String summary, double temperature, String icon, String timeZone) {
        this.time = time;
        this.summary = summary;
        this.temperature = temperature;
        this.icon = icon;
        this.timeZone = timeZone;
    }

    public String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h a");
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));

        Date dateTime = new Date(time * 1000);
        return formatter.format(dateTime);
    }

    public void setTime(long time) {
        time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        summary = summary;
    }

    public double getTemperature() {
        return (int)Math.round(temperature);
    }

    public void setTemperature(double temperature) {
        temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        icon = icon;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timezone) {
        timezone = timezone;
    }



}
