package com.ahmed.othman.akhysai.pojo;

import java.util.ArrayList;

public class BlogCategories {
    private int id;
    private String photo;
    private String is_active;
    private String name;
    private String description;
    private int articles_count;
    private ArrayList<Article> articles;

    public BlogCategories(int id, String photo, String is_active, String name) {
        this.id = id;
        this.photo = photo;
        this.is_active = is_active;
        this.name = name;
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

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getArticles_count() {
        return articles_count;
    }

    public void setArticles_count(int articles_count) {
        this.articles_count = articles_count;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }
}
