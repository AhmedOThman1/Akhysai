package com.ahmed.othman.akhysai.pojo;

import com.google.gson.annotations.SerializedName;

public class Speciality {
    /*
    * {
      "id":1,
      "field_id":"1",
      "is_active":"1",
      "created_at":"2020-12-02 14:08:02",
      "updated_at":"2020-12-02 14:08:25",
      "name":"Bones"
   }
   * */
    @SerializedName("id")
    private int specialityId;
    @SerializedName("field_id")
    private String fieldId;
    @SerializedName("is_active")
    private String isActive;
    @SerializedName("name")
    private String specialityName;

    public Speciality(int specialityId, String fieldId, String isActive, String specialityName) {
        this.specialityId = specialityId;
        this.fieldId = fieldId;
        this.isActive = isActive;
        this.specialityName = specialityName;
    }

    public int getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(int specialityId) {
        this.specialityId = specialityId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }
}
