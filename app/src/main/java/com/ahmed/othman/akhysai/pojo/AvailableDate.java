package com.ahmed.othman.akhysai.pojo;

import com.google.gson.annotations.SerializedName;

public class AvailableDate {

    private String id;
    @SerializedName("specialist_id")
    private String specialistId;
    private String day;
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("is_active")
    private String isActive;

    public AvailableDate(String id, String specialistId, String day, String startTime, String endTime, String isActive) {
        this.id = id;
        this.specialistId = specialistId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecialistId() {
        return specialistId;
    }

    public void setSpecialistId(String specialistId) {
        this.specialistId = specialistId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
