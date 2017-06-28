package com.example.icyfillup.newsapp;

import android.service.quicksettings.Tile;

import java.net.URL;

/**
 * Created by icyfillup on 6/26/2017.
 */

public class NewsItem {
    private String Title;
    private String Description;
    private URL url;
    private String date;

    public NewsItem(String Title, String Description, String Date, URL url) {
        this.Title = Title;
        this.Description = Description;
        this.url = url;
        this.date = Date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public URL getUrl() {
        return url;

    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String toString() {
        return "Title: " + Title + "\nDescription: " + Description + "\nURL: " + url.toString();
    }
}
