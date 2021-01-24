package com.ahmed.othman.akhysai.pojo;

import com.google.gson.annotations.SerializedName;

public class Article {


    @SerializedName("id")
    private String articleId;
    @SerializedName("category_id")
    private String categoryId;
    @SerializedName("specialist_id")
    private String specialistId;
    private String title;
    private String body;
    private String language;
    private String picture;
    @SerializedName("is_active")
    private String isActive;
    @SerializedName("is_public")
    private String isPublic;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;

    private boolean selected=false;

    public Article() {
    }

    public Article(String picture, String title, String categoryId) {
        this.picture = picture;
        this.title = title;
        this.categoryId = categoryId;
    }

    public Article(String articleId, String categoryId, String specialistId, String title, String body, String language, String picture, String isActive, String isPublic, String createdAt, String updatedAt) {
        this.articleId = articleId;
        this.categoryId = categoryId;
        this.specialistId = specialistId;
        this.title = title;
        this.body = body;
        this.language = language;
        this.picture = picture;
        this.isActive = isActive;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(String specialistId) {
        this.specialistId = specialistId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
}
