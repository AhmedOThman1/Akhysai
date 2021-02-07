package com.ahmed.othman.akhysai.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Akhysai {
    @SerializedName("id")
    private int akhysai_id;
    @SerializedName("city_id")
    private String cityId;
    @SerializedName("region_id")
    private String regionId;
    @SerializedName("qualification_id")
    private String qualificationId;
    @SerializedName("field_id")
    private String fieldId;
    @SerializedName("speciality_id")
    private String specialityId;
    @SerializedName("national_id")
    private String nationalId;
    @SerializedName("birth_date")
    private String birthDate;
    @SerializedName("gender")
    private String gender;
    @SerializedName("phone")
    private String phone;
    @SerializedName("years_of_experience")
    private String experienceYears;
    @SerializedName("profile_picture")
    private String profile_picture;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("api_token")
    private String apiToken;
    @SerializedName("is_active")
    private String isActive;
    @SerializedName("is_verified")
    private String isVerified;
    @SerializedName("profile_completed")
    private String profileCompleted;
    @SerializedName("ar")
    private Language Ar;
    @SerializedName("en")
    private Language En;

    public Akhysai() {
    }

    public Akhysai(int akhysai_id, String cityId, String regionId, String qualificationId, String fieldId, String specialityId, String nationalId, String birthDate, String gender, String phone, String experienceYears, String profile_picture, String name, String apiToken, String isActive, String isVerified, String profileCompleted, Language ar, Language en) {
        this.akhysai_id = akhysai_id;
        this.cityId = cityId;
        this.regionId = regionId;
        this.qualificationId = qualificationId;
        this.fieldId = fieldId;
        this.specialityId = specialityId;
        this.nationalId = nationalId;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.experienceYears = experienceYears;
        this.profile_picture = profile_picture;
        this.name = name;
        this.apiToken = apiToken;
        this.isActive = isActive;
        this.isVerified = isVerified;
        this.profileCompleted = profileCompleted;
        Ar = ar;
        En = en;
    }

    public int getAkhysai_id() {
        return akhysai_id;
    }

    public void setAkhysai_id(int akhysai_id) {
        this.akhysai_id = akhysai_id;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(String qualificationId) {
        this.qualificationId = qualificationId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(String specialityId) {
        this.specialityId = specialityId;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(String experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getProfileCompleted() {
        return profileCompleted;
    }

    public void setProfileCompleted(String profileCompleted) {
        this.profileCompleted = profileCompleted;
    }

    public Language getAr() {
        return Ar;
    }

    public void setAr(Language ar) {
        Ar = ar;
    }

    public Language getEn() {
        return En;
    }

    public void setEn(Language en) {
        En = en;
    }

    public class Language {
        @SerializedName("id")
        private int id;
        @SerializedName("specialist_id")
        private String specialistId;
        @SerializedName("language")
        private String language;
        @SerializedName("name")
        private String name;
        @SerializedName("address")
        private String address;
        @SerializedName("bio")
        private String bio;

        public Language(int id, String specialistId, String language, String name, String address, String bio) {
            this.id = id;
            this.specialistId = specialistId;
            this.language = language;
            this.name = name;
            this.address = address;
            this.bio = bio;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSpecialistId() {
            return specialistId;
        }

        public void setSpecialistId(String specialistId) {
            this.specialistId = specialistId;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }
    }


}
