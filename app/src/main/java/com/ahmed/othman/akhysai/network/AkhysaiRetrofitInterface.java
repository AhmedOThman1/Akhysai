package com.ahmed.othman.akhysai.network;

import com.ahmed.othman.akhysai.pojo.Article;
import com.ahmed.othman.akhysai.pojo.AvailableDate;
import com.ahmed.othman.akhysai.pojo.BlogCategories;
import com.ahmed.othman.akhysai.pojo.City;
import com.ahmed.othman.akhysai.pojo.DirectoryCategories;
import com.ahmed.othman.akhysai.pojo.Field;
import com.ahmed.othman.akhysai.pojo.Qualification;
import com.ahmed.othman.akhysai.pojo.Region;
import com.ahmed.othman.akhysai.pojo.Service;
import com.ahmed.othman.akhysai.pojo.Speciality;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AkhysaiRetrofitInterface {

    @GET("{languageIso}/cities")
    public Call<List<City>> getAllCities(@Path("languageIso") String languageIso);

    @GET("{languageIso}/regions/{id}")
    public Call<List<Region>> getAllRegionsByCityId(@Path("languageIso") String languageIso, @Path("id") int CityId);

    @GET("{languageIso}/fields")
    public Call<List<Field>> getAllFields(@Path("languageIso") String languageIso);

    @GET("{languageIso}/specialities/{id}")
    public Call<List<Speciality>> getAllSpecialitiesByFieldId(@Path("languageIso") String languageIso, @Path("id") int fieldId);

    @GET("{languageIso}/blog/categories")
    public Call<List<BlogCategories>> getAllBlogCategories(@Path("languageIso") String languageIso);

    @GET("{languageIso}/directory/categories")
    public Call<List<DirectoryCategories>> getAllDirectoryCategories(@Path("languageIso") String languageIso);


    @GET("{languageIso}/qualifications")
    public Call<List<Qualification>> getAllQualifications(@Path("languageIso") String languageIso);


    @POST("{languageIso}/login/specialist")
    @Headers("Accept: application/json")
    public Call<JsonObject> LoginRequest(@Path("languageIso") String languageIso, @Body JsonObject loginData);

    @POST("{languageIso}/register/specialist")
    @Headers("Accept: application/json")
    public Call<JsonObject> RegisterRequest(@Path("languageIso") String languageIso, @Body JsonObject registerData);

    @GET("specialist/")
    @Headers("Accept: application/json")
    public Call<JsonObject> getSpecialistData(@Header("Authorization") String Authorization);

    @POST("specialist/profile/complete/")
    @Headers("Accept: application/json")
    public Call<JsonObject> CompleteProfile(@Header("Authorization") String Authorization, @Body JsonObject profileData);


    @Multipart
    @POST("specialist/profile/complete/")
    @Headers("Accept: application/json")
    public Call<JsonObject> CompleteProfile(@Header("Authorization") String Authorization,
                                            @Part("name[ar]") RequestBody nameAr,
                                            @Part("name[en]") RequestBody nameEn,
                                            @Part("city") RequestBody city,
                                            @Part("region") RequestBody region,
                                            @Part("national_id") RequestBody national_id,
                                            @Part("birth_date") RequestBody birth_date,
                                            @Part("gender") RequestBody gender,
                                            @Part("phone") RequestBody phone,
                                            @Part("years_of_experience") RequestBody years_of_experience,
                                            @Part("bio[ar]") RequestBody bioAr,
                                            @Part("bio[en]") RequestBody bioEn,
                                            @Part MultipartBody.Part profile_picture,
                                            @Part("address[ar]") RequestBody addressAr,
                                            @Part("address[en]") RequestBody addressEn,
                                            @Part("field") RequestBody field,
                                            @Part("speciality") RequestBody speciality,
                                            @Part("qualification") RequestBody qualification);

    @GET("specialist/articles")
    @Headers("Accept: application/json")
    public Call<ArrayList<Article>> getAkhysaiArticles(@Header("Authorization") String Authorization);


//    @POST("specialist/articles/add")
//    @Headers("Accept: application/json")
//    public Call<JsonObject> AddNewArticle(@Header("Authorization") String Authorization, @Body JsonObject articleData);

    @Multipart
    @POST("specialist/articles/add")
    @Headers("Accept: application/json")
    public Call<JsonObject> AddNewArticle(@Header("Authorization") String Authorization, @Part("title") RequestBody title, @Part("category") RequestBody category, @Part("body") RequestBody body, @Part("language") RequestBody language, @Part MultipartBody.Part picture);


    @POST("specialist/articles/delete")
    @Headers("Accept: application/json")
    public Call<JsonObject> DeleteArticle(@Header("Authorization") String Authorization, @Body JsonObject jsonObject);


    @GET("specialist/services")
    @Headers("Accept: application/json")
    public Call<ArrayList<Service>> getAkhysaiServices(@Header("Authorization") String Authorization);

    @POST("specialist/services/add")
    @Headers("Accept: application/json")
    public Call<JsonObject> AddNewService(@Header("Authorization") String Authorization, @Body JsonObject servicesData);


    @POST("specialist/services/delete")
    @Headers("Accept: application/json")
    public Call<JsonObject> DeleteService(@Header("Authorization") String Authorization, @Body JsonObject jsonObject);


    @GET("specialist/timetable")
    @Headers("Accept: application/json")
    public Call<ArrayList<AvailableDate>> getAkhysaiTimetable(@Header("Authorization") String Authorization);

}
