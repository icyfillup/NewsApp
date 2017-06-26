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

    public NewsItem(String Title, String Description, URL url) {
        this.Title = Title;
        this.Description = Description;
        this.url = url;
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
