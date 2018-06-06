package com.example.engmohamed.technonews;

import android.graphics.drawable.Drawable;

/**
 * An private object that simulate item_style XML
 */

public class TechnoNews {
    private Drawable image;
    private String title;
    private String description;
    private String author;
    private String url;
    private String date;
    private String section;


    TechnoNews(Drawable image, String title, String description, String author, String date, String section, String url) {

        this.image = image;
        this.title = title;
        this.description = description;
        this.author = author;
        this.url = url;
        this.date = date;
        this.section = section;
    }

    public Drawable getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public String getSection() {
        return section;
    }
}
