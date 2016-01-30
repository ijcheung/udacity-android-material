package com.example.xyzreader.model;

import com.google.gson.annotations.SerializedName;

public class Article {
    private int id;
    private String photo;
    private String thumb;
    @SerializedName("aspect_ratio") private Float aspectRatio;
    private String author;
    private String title;
    @SerializedName("published_date") private String publishedDate;
    private String body;

    public Article(int id, String photo, String thumb, Float aspectRatio, String author, String title, String publishedDate, String body) {
        this.id = id;
        this.photo = photo;
        this.thumb = thumb;
        this.aspectRatio = aspectRatio;
        this.author = author;
        this.title = title;
        this.publishedDate = publishedDate;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(Float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
