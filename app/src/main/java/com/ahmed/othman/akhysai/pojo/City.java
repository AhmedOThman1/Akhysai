package com.ahmed.othman.akhysai.pojo;

import com.google.gson.annotations.SerializedName;

public class City {
    /*
    * {
      "id":8,
      "is_active":"1",
      "longitude":null,
      "latitude":null,
      "created_at":"2020-11-30 18:33:15",
      "updated_at":"2020-12-06 16:42:46",
      "name":"Cairo"
   }
   * */
    @SerializedName("id")
    private int cityId;
    @SerializedName("is_active")
    private String isActive;
    private Long latitude;
    private Long longitude;
    @SerializedName("name")
    private String cityName;

    public City(int cityId, String isActive, Long latitude, Long longitude, String cityName) {
        this.cityId = cityId;
        this.isActive = isActive;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cityName = cityName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
