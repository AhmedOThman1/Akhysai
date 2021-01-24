package com.ahmed.othman.akhysai.pojo;

import com.google.gson.annotations.SerializedName;

public class Field {
    /*
    * {
      "id":1,
      "is_active":"1",
      "created_at":"2020-12-02 10:45:02",
      "updated_at":"2020-12-02 10:45:02",
      "name":"Medical"
   }
   * */
    @SerializedName("id")
    private int fieldId;
    @SerializedName("is_active")
    private String isActive;
    @SerializedName("name")
    private String fieldName;

    public Field(int fieldId, String isActive, String fieldName) {
        this.fieldId = fieldId;
        this.isActive = isActive;
        this.fieldName = fieldName;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
