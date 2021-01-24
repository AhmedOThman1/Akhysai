package com.ahmed.othman.akhysai.ui.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ahmed.othman.akhysai.network.ApiClient;
import com.ahmed.othman.akhysai.pojo.Article;
import com.ahmed.othman.akhysai.pojo.BlogCategories;
import com.ahmed.othman.akhysai.pojo.City;
import com.ahmed.othman.akhysai.pojo.DirectoryCategories;
import com.ahmed.othman.akhysai.pojo.Field;
import com.ahmed.othman.akhysai.pojo.Qualification;
import com.ahmed.othman.akhysai.pojo.Region;
import com.ahmed.othman.akhysai.pojo.Service;
import com.ahmed.othman.akhysai.pojo.Speciality;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;

public class AkhysaiViewModel extends ViewModel {

    public MutableLiveData<List<City>> citiesMutableLiveData = new MutableLiveData<>();

    public void getAllCities(String languageIso) {
        ApiClient.getINSTANCE().getAllCities(languageIso).enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(@NonNull Call<List<City>> call, @NonNull Response<List<City>> response) {
                if (response.isSuccessful())
                    citiesMutableLiveData.setValue(response.body());
                Log.w("Cities", response.body() + "");
            }

            @Override
            public void onFailure(@NonNull Call<List<City>> call, @NonNull Throwable t) {

            }
        });
    }


    public MutableLiveData<List<Region>> regionsMutableLiveData = new MutableLiveData<>();

    public void getAllRegionsByCityId(String languageIso, int CityId) {
        ApiClient.getINSTANCE().getAllRegionsByCityId(languageIso, CityId).enqueue(new Callback<List<Region>>() {
            @Override
            public void onResponse(@NonNull Call<List<Region>> call, @NonNull Response<List<Region>> response) {
                if (response.isSuccessful())
                    regionsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Region>> call, @NonNull Throwable t) {

            }
        });
    }

    public MutableLiveData<List<Field>> fieldsMutableLiveData = new MutableLiveData<>();

    public void getAllFields(String languageIso) {
        ApiClient.getINSTANCE().getAllFields(languageIso).enqueue(new Callback<List<Field>>() {
            @Override
            public void onResponse(@NonNull Call<List<Field>> call, @NonNull Response<List<Field>> response) {
                if (response.isSuccessful())
                    fieldsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Field>> call, @NonNull Throwable t) {

            }
        });
    }

    public MutableLiveData<List<Speciality>> specialitiesMutableLiveData = new MutableLiveData<>();

    public void getAllSpecialitiesByFieldId(String languageIso, int FieldId) {
        ApiClient.getINSTANCE().getAllSpecialitiesByFieldId(languageIso, FieldId).enqueue(new Callback<List<Speciality>>() {
            @Override
            public void onResponse(@NonNull Call<List<Speciality>> call, @NonNull Response<List<Speciality>> response) {
                if (response.isSuccessful())
                    specialitiesMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<Speciality>> call, @NonNull Throwable t) {

            }
        });
    }

    public MutableLiveData<List<BlogCategories>> blogCategoriesMutableLiveData = new MutableLiveData<>();

    public void getAllBlogCategories(String languageIso) {
        ApiClient.getINSTANCE().getAllBlogCategories(languageIso).enqueue(new Callback<List<BlogCategories>>() {
            @Override
            public void onResponse(@NonNull Call<List<BlogCategories>> call, @NonNull Response<List<BlogCategories>> response) {
                if (response.isSuccessful())
                    blogCategoriesMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<BlogCategories>> call, @NonNull Throwable t) {

            }
        });
    }

    public MutableLiveData<List<DirectoryCategories>> directoryCategoriesMutableLiveData = new MutableLiveData<>();

    public void getAllDirectoryCategories(String languageIso) {
        ApiClient.getINSTANCE().getAllDirectoryCategories(languageIso).enqueue(new Callback<List<DirectoryCategories>>() {
            @Override
            public void onResponse(@NonNull Call<List<DirectoryCategories>> call, @NonNull Response<List<DirectoryCategories>> response) {
                if (response.isSuccessful())
                    directoryCategoriesMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<List<DirectoryCategories>> call, @NonNull Throwable t) {

            }
        });
    }

    public MutableLiveData<List<Qualification>> qualificationsMutableLiveData = new MutableLiveData<>();

    public void getAllQualifications(String languageIso) {
        ApiClient.getINSTANCE().getAllQualifications(languageIso).enqueue(new Callback<List<Qualification>>() {
            @Override
            public void onResponse(@NonNull Call<List<Qualification>> call, @NonNull Response<List<Qualification>> response) {
                if (response.isSuccessful())
                    qualificationsMutableLiveData.setValue(response.body());
                Log.w("Qualifications", response.body() + "");
            }

            @Override
            public void onFailure(@NonNull Call<List<Qualification>> call, @NonNull Throwable t) {

            }
        });
    }


    public MutableLiveData<ArrayList<String>> LoginCurrentUserToken = new MutableLiveData<>();

    public void LoginRequest(String languageIso, String email, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", password);

        ApiClient.getINSTANCE().LoginRequest(languageIso, jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.w("Login_Response", response.body() + "");
                if (response.isSuccessful()) {
                    ArrayList temp = new ArrayList();
                    Log.w("Login", "AkhysaiViewModel_LoginRequest_onResponse, Body: " + response.body() + " ,status: " + response.body().get("status").getAsString() + " ,Token: " + response.body().get("data").getAsString());
                    temp.add(response.body().get("status").getAsString());
                    temp.add(response.body().get("data").getAsString());
                    LoginCurrentUserToken.setValue(temp);

                } else {
                    ArrayList temp = new ArrayList();
                    temp.add("error");
                    temp.add("response is not successful");
                    LoginCurrentUserToken.setValue(temp);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ArrayList temp = new ArrayList();
                temp.add("error");
                temp.add(t.getMessage());
                LoginCurrentUserToken.setValue(temp);
            }
        });
    }

    public MutableLiveData<ArrayList<String>> RegisterCurrentUserToken = new MutableLiveData<>();

    public void RegisterRequest(String languageIso, String name, String email, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", password);

        ApiClient.getINSTANCE().RegisterRequest(languageIso, jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.w("Register_Response", response.body() + "");
                if (response.isSuccessful()) {
                    ArrayList temp = new ArrayList();
                    Log.w("Register", "AkhysaiViewModel_RegisterRequest_onResponse, Body: " + response.body() + " ,status: " + response.body().get("status").getAsString() + " ,Token: " + response.body().get("data").getAsString());
                    temp.add(response.body().get("status").getAsString());
                    temp.add(response.body().get("data").getAsString());
                    if (response.body().get("status").getAsString().equals("error"))
                        temp.add(response.body().get("message").getAsJsonObject().get("email").getAsJsonArray().get(0).getAsString());
                    RegisterCurrentUserToken.setValue(temp);

                } else {
                    ArrayList temp = new ArrayList();
                    temp.add("error");
                    temp.add("response is not successful");
                    RegisterCurrentUserToken.setValue(temp);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ArrayList temp = new ArrayList();
                temp.add("error");
                temp.add(t.getMessage());
                RegisterCurrentUserToken.setValue(temp);
            }
        });
    }

    public MutableLiveData<JsonObject> SpecialistDataMutableLiveData = new MutableLiveData<>();

    public void getSpecialistData(String Authorization) {
        ApiClient.getINSTANCE().getSpecialistData(Authorization).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful())
                    SpecialistDataMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public MutableLiveData<JsonObject> profileCompleteMutableLiveData = new MutableLiveData<>();

    public void CompleteProfile(String Authorization, JsonObject profileData) {
        ApiClient.getINSTANCE().CompleteProfile(Authorization, profileData).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.w("CompleteProfileResponse", response.body() + "");
                if (response.isSuccessful()) {
                    profileCompleteMutableLiveData.setValue(response.body());
                } else
                    profileCompleteMutableLiveData.setValue(null);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                profileCompleteMutableLiveData.setValue(null);
            }
        });
    }


    public MutableLiveData<ArrayList<Article>> akhysaiArticlesMutableLiveData = new MutableLiveData<>();

    public void getAkhysaiArticles(String Authorization) {
        ApiClient.getINSTANCE().getAkhysaiArticles(Authorization).enqueue(new Callback<ArrayList<Article>>() {
            @Override
            public void onResponse(Call<ArrayList<Article>> call, Response<ArrayList<Article>> response) {
                if (response.isSuccessful())
                    akhysaiArticlesMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Article>> call, Throwable t) {
                akhysaiArticlesMutableLiveData.setValue(null);
            }
        });
    }

//    public MutableLiveData<ArrayList<String>> newArticleMutableLiveData = new MutableLiveData<>();
//    public void AddNewArticle(String Authorization, JsonObject articleData) {
//        ApiClient.getINSTANCE().AddNewArticle(Authorization, articleData).enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//
//                ArrayList<String> strings = new ArrayList<>();
//
//                if (response.isSuccessful()) {
//                    strings.add(response.body().get("status").getAsString());
//                    strings.add(response.body().get("data").getAsJsonObject().toString());
//                } else {
//                    strings.add("error");
//                    strings.add("response is not successful");
//                }
//
//                newArticleMutableLiveData.setValue(strings);
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                ArrayList<String> strings = new ArrayList<>();
//
//                strings.add("error");
//                strings.add(t.getMessage());
//                newArticleMutableLiveData.setValue(strings);
//
//            }
//        });
//    }


    public MutableLiveData<ArrayList<String>> deleteArticleMutableLiveData = new MutableLiveData<>();

    public void DeleteArticle(String Authorization, JsonObject jsonObject) {
        ApiClient.getINSTANCE().DeleteArticle(Authorization, jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                ArrayList<String> strings = new ArrayList<>();

                if (response.isSuccessful()) {
                    strings.add(response.body().get("status").getAsString());
                    strings.add(response.body().get("data").getAsJsonObject().toString());
                } else {
                    strings.add("error");
                    strings.add("response is not successful");
                }

                deleteArticleMutableLiveData.setValue(strings);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ArrayList<String> strings = new ArrayList<>();

                strings.add("error");
                strings.add(t.getMessage());
                deleteArticleMutableLiveData.setValue(strings);

            }
        });
    }


    public MutableLiveData<ArrayList<Service>> akhysaiServicesMutableLiveData = new MutableLiveData<>();

    public void getAkhysaiService(String Authorization) {
        ApiClient.getINSTANCE().getAkhysaiServices(Authorization).enqueue(new Callback<ArrayList<Service>>() {
            @Override
            public void onResponse(Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {
                if (response.isSuccessful())
                    akhysaiServicesMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Service>> call, Throwable t) {
                akhysaiServicesMutableLiveData.setValue(null);
            }
        });
    }

    public MutableLiveData<ArrayList<String>> newServiceMutableLiveData = new MutableLiveData<>();

    public void AddNewService(String Authorization, JsonObject serviceData) {
        ApiClient.getINSTANCE().AddNewService(Authorization, serviceData).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                ArrayList<String> strings = new ArrayList<>();

                if (response.isSuccessful()) {
                    strings.add(response.body().get("status").getAsString());
                    strings.add(response.body().get("data").getAsJsonObject().toString());
                } else {
                    strings.add("error");
                    strings.add("response is not successful");
                }

                newServiceMutableLiveData.setValue(strings);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ArrayList<String> strings = new ArrayList<>();

                strings.add("error");
                strings.add(t.getMessage());
                newServiceMutableLiveData.setValue(strings);

            }
        });
    }

    public MutableLiveData<ArrayList<String>> deleteServiceMutableLiveData = new MutableLiveData<>();

    public void DeleteService(String Authorization, JsonObject jsonObject) {
        ApiClient.getINSTANCE().DeleteService(Authorization, jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                ArrayList<String> strings = new ArrayList<>();

                if (response.isSuccessful()) {
                    strings.add(response.body().get("status").getAsString());
                    strings.add(response.body().get("data").getAsJsonObject().toString());
                } else {
                    strings.add("error");
                    strings.add("response is not successful");
                }

                deleteServiceMutableLiveData.setValue(strings);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                ArrayList<String> strings = new ArrayList<>();

                strings.add("error");
                strings.add(t.getMessage());
                deleteServiceMutableLiveData.setValue(strings);

            }
        });
    }


}
