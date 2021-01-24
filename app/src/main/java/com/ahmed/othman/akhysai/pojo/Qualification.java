package com.ahmed.othman.akhysai.pojo;

import com.google.gson.annotations.SerializedName;

public class Qualification {

    private String name;

    @SerializedName("id")
    private int qualificationId;

    @SerializedName("is_active")
    private String isActive;

    public Qualification(String name, int qualificationId, String isActive) {
        this.name = name;
        this.qualificationId = qualificationId;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(int qualificationId) {
        this.qualificationId = qualificationId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
