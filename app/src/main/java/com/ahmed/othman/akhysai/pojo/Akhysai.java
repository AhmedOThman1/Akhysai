package com.ahmed.othman.akhysai.pojo;

import java.util.ArrayList;

public class Akhysai {
    private String akhysai_id;
    private String photo;
    private String name;
    private String description;
    private String about_akhysai;
    private int experience_years;
    private float rate;
    private int visitor_num;
    private int price;
    private String phone_num;
    private long birthday;
    private boolean gender;
    private String id_card_num;

    private ArrayList<Review> Reviews = new ArrayList<>();
    private ArrayList<Article> Articles = new ArrayList<>();

    public Akhysai() {
    }

    public Akhysai(String photo, String name, String description, int experience_years, float rate, int visitor_num, int price) {
        this.photo = photo;
        this.name = name;
        this.description = description;
        this.experience_years = experience_years;
        this.rate = rate;
        this.visitor_num = visitor_num;
        this.price = price;
    }

    public Akhysai(String photo, String name, String description, String about_akhysai, int experience_years, float rate, int visitor_num, int price) {
        this.photo = photo;
        this.name = name;
        this.description = description;
        this.about_akhysai = about_akhysai;
        this.experience_years = experience_years;
        this.rate = rate;
        this.visitor_num = visitor_num;
        this.price = price;
    }

    public Akhysai(String photo, String name, String description, String about_akhysai, int experience_years, float rate, int visitor_num, int price, ArrayList<Review> reviews) {
        this.photo = photo;
        this.name = name;
        this.description = description;
        this.about_akhysai = about_akhysai;
        this.experience_years = experience_years;
        this.rate = rate;
        this.visitor_num = visitor_num;
        this.price = price;
        Reviews = reviews;
    }

    public Akhysai(String photo, String name, String description, String about_akhysai, int experience_years, float rate, int visitor_num, int price, String phone_num, long birthday, boolean gender, String id_card_num, ArrayList<Review> reviews, ArrayList<Article> articles) {
        this.photo = photo;
        this.name = name;
        this.description = description;
        this.about_akhysai = about_akhysai;
        this.experience_years = experience_years;
        this.rate = rate;
        this.visitor_num = visitor_num;
        this.price = price;
        this.phone_num = phone_num;
        this.birthday = birthday;
        this.gender = gender;
        this.id_card_num = id_card_num;
        Reviews = reviews;
        Articles = articles;
    }

    public ArrayList<Review> getReviews() {
        return Reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        Reviews = reviews;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public int getExperience_years() {
        return experience_years;
    }

    public void setExperience_years(int experience_years) {
        this.experience_years = experience_years;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getVisitor_num() {
        return visitor_num;
    }

    public void setVisitor_num(int visitor_num) {
        this.visitor_num = visitor_num;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAkhysai_id() {
        return akhysai_id;
    }

    public void setAkhysai_id(String akhysai_id) {
        this.akhysai_id = akhysai_id;
    }

    public String getAbout_akhysai() {
        return about_akhysai;
    }

    public void setAbout_akhysai(String about_akhysai) {
        this.about_akhysai = about_akhysai;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getId_card_num() {
        return id_card_num;
    }

    public void setId_card_num(String id_card_num) {
        this.id_card_num = id_card_num;
    }

    public ArrayList<Article> getArticles() {
        return Articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        Articles = articles;
    }
}
