package com.ahmed.othman.akhysai.pojo;

public class Article {
    private String article_id;
    private String image;
    private String title;
    private String category;
    private long date;
    private String body;
    private String article_writer_id;

    public Article() {
    }

    public Article(String image, String title, String category) {
        this.image = image;
        this.title = title;
        this.category = category;
    }

    public Article(String image, String title, String category, long date, String body, String article_writer_id) {
        this.image = image;
        this.title = title;
        this.category = category;
        this.date = date;
        this.body = body;
        this.article_writer_id = article_writer_id;
    }

    public String getArticle_writer_id() {
        return article_writer_id;
    }

    public void setArticle_writer_id(String article_writer_id) {
        this.article_writer_id = article_writer_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public long getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getArticle_id() {
        return article_id;
    }

    public void setArticle_id(String article_id) {
        this.article_id = article_id;
    }
}
