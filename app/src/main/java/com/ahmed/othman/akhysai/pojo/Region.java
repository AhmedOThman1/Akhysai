package com.ahmed.othman.akhysai.pojo;

import com.google.gson.annotations.SerializedName;

public class Region {

    /*
    * {
      "id":4,
      "city_id":"11",
      "is_active":"1",
      "longitude":null,
      "latitude":null,
      "created_at":"2020-11-30 21:54:00",
      "updated_at":"2020-11-30 22:05:28",
      "name":"Faisal"
   }
   *
   */

    @SerializedName("id")
    private int regionId;
    @SerializedName("city_id")
    private String cityId;
    @SerializedName("is_active")
    private String isActive;
    private Long latitude;
    private Long longitude;
    @SerializedName("name")
    private String regionName;

    public Region(int regionId, String cityId, String isActive, Long latitude, Long longitude, String regionName) {
        this.regionId = regionId;
        this.cityId = cityId;
        this.isActive = isActive;
        this.latitude = latitude;
        this.longitude = longitude;
        this.regionName = regionName;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
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

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}
