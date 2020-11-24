package com.ahmed.othman.akhysai.pojo;

public class Clinic {
    private String clinic_id;
    private String image;
    private String name;
    private String category;
    private String phone;
    private String website;
    private String company_name;
    private String details;
    private String city;
    private String area;

    public Clinic() {
    }

    public Clinic(String image, String name, String category, String phone, String website, String company_name, String details) {
        this.image = image;
        this.name = name;
        this.category = category;
        this.phone = phone;
        this.website = website;
        this.company_name = company_name;
        this.details = details;
    }

    public Clinic(String image, String name, String category, String phone, String website, String company_name, String details, String city, String area) {
        this.image = image;
        this.name = name;
        this.category = category;
        this.phone = phone;
        this.website = website;
        this.company_name = company_name;
        this.details = details;
        this.city = city;
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getClinic_id() {
        return clinic_id;
    }

    public void setClinic_id(String clinic_id) {
        this.clinic_id = clinic_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
