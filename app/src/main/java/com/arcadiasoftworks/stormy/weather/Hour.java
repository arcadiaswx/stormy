package com.arcadiasoftworks.stormy.weather;


public class Hour {
    private long time;
    private String summary;
    private double temperature;
    private String icon;
    private String timezone;


    public Hour() {
    }

    public Hour(long time, String summary, double temperature, String icon, String timezone) {
        this.time = time;
        this.summary = summary;
        this.temperature = temperature;
        this.icon = icon;
        this.timezone = timezone;
    }

    public long getTime() {
        return time;
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
        return temperature;
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
        return timezone;
    }

    public void setTimeZone(String timezone) {
        timezone = timezone;
    }



}
