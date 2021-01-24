package com.ahmed.othman.akhysai.network;

import android.app.Activity;

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
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Part;
import retrofit2.http.Path;

import static android.content.Context.MODE_PRIVATE;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.AppLanguage;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.LanguageIso;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;

public class ApiClient {
    //    private static final String AR_BASE_URL = "https://sp.xative.com/public/api/ar/";
//    private static final String EN_BASE_URL = "https://sp.xative.com/public/api/en/";
    private String BASE_URL = "https://sp.xative.com/public/api/";
    private AkhysaiRetrofitInterface akhysaiRetrofitInterface;
    private static ApiClient INSTANCE;

    public ApiClient() {
//        if (AppLanguage.equalsIgnoreCase("ar"))
//            BASE_URL = AR_BASE_URL;
//        else if (AppLanguage.equalsIgnoreCase("en"))
//            BASE_URL = EN_BASE_URL;

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        akhysaiRetrofitInterface = retrofit.create(AkhysaiRetrofitInterface.class);

    }

    public static ApiClient getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ApiClient();
        }
        return INSTANCE;
    }

    public Call<List<City>> getAllCities(String languageIso) {
        return akhysaiRetrofitInterface.getAllCities(languageIso);
    }


    public Call<List<Region>> getAllRegionsByCityId(String languageIso, int CityId) {
        return akhysaiRetrofitInterface.getAllRegionsByCityId(languageIso, CityId);
    }

    public Call<List<Field>> getAllFields(String languageIso) {
        return akhysaiRetrofitInterface.getAllFields(languageIso);
    }

    public Call<List<Speciality>> getAllSpecialitiesByFieldId(String languageIso, int fieldId) {
        return akhysaiRetrofitInterface.getAllSpecialitiesByFieldId(languageIso, fieldId);
    }

    public Call<List<BlogCategories>> getAllBlogCategories(String languageIso) {
        return akhysaiRetrofitInterface.getAllBlogCategories(languageIso);
    }

    public Call<List<DirectoryCategories>> getAllDirectoryCategories(String languageIso) {
        return akhysaiRetrofitInterface.getAllDirectoryCategories(languageIso);
    }

    public Call<List<Qualification>> getAllQualifications(String languageIso){
        return akhysaiRetrofitInterface.getAllQualifications(languageIso);
    }

    public Call<JsonObject> LoginRequest(String languageIso, JsonObject jsonObject) {
        return akhysaiRetrofitInterface.LoginRequest(languageIso, jsonObject);
    }

    public Call<JsonObject> RegisterRequest(String languageIso, JsonObject jsonObject) {
        return akhysaiRetrofitInterface.RegisterRequest(languageIso, jsonObject);
    }

    public Call<JsonObject> getSpecialistData(String Authorization) {
        return akhysaiRetrofitInterface.getSpecialistData(Authorization);
    }

    public Call<JsonObject> CompleteProfile(String Authorization, JsonObject profileData) {
        return akhysaiRetrofitInterface.CompleteProfile(Authorization, profileData);
    }

    public Call<JsonObject> CompleteProfile(String Authorization, RequestBody nameAr, RequestBody nameEn, RequestBody city, RequestBody region, RequestBody national_id, RequestBody birth_date, RequestBody gender, RequestBody phone, RequestBody years_of_experience, RequestBody bioAr, RequestBody bioEn, MultipartBody.Part profile_picture, RequestBody addressAr, RequestBody addressEn, RequestBody field, RequestBody speciality, RequestBody qualification) {
        return akhysaiRetrofitInterface.CompleteProfile(Authorization, nameAr, nameEn, city, region, national_id, birth_date, gender, phone, years_of_experience, bioAr, bioEn, profile_picture, addressAr, addressEn, field, speciality, qualification);
    }

    public Call<ArrayList<Article>> getAkhysaiArticles(String Authorization) {
        return akhysaiRetrofitInterface.getAkhysaiArticles(Authorization);
    }

//    public Call<JsonObject> AddNewArticle(String Authorization, JsonObject articleData) {
//        return akhysaiRetrofitInterface.AddNewArticle(Authorization, articleData);
//    }

    public Call<JsonObject> AddNewArticle(String Authorization, RequestBody title, RequestBody category, RequestBody body, RequestBody language, MultipartBody.Part picture) {
        return akhysaiRetrofitInterface.AddNewArticle(Authorization, title, category, body, language, picture);
    }

    public Call<JsonObject> DeleteArticle(String Authorization, JsonObject jsonObject) {
        return akhysaiRetrofitInterface.DeleteArticle(Authorization, jsonObject);
    }

    public Call<ArrayList<Service>> getAkhysaiServices(String Authorization) {
        return akhysaiRetrofitInterface.getAkhysaiServices(Authorization);
    }

    public Call<JsonObject> AddNewService(String Authorization, JsonObject servicesData) {
        return akhysaiRetrofitInterface.AddNewService(Authorization, servicesData);
    }

    public Call<JsonObject> DeleteService(String Authorization, JsonObject jsonObject) {
        return akhysaiRetrofitInterface.DeleteService(Authorization, jsonObject);
    }


    public Call<ArrayList<AvailableDate>> getAkhysaiTimetable(String Authorization) {
        return akhysaiRetrofitInterface.getAkhysaiTimetable(Authorization);
    }

}