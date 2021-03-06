package com.ahmed.othman.akhysai.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.network.ApiClient;
import com.ahmed.othman.akhysai.network.NetworkChangeReceiver;
import com.ahmed.othman.akhysai.pojo.Akhysai;
import com.ahmed.othman.akhysai.pojo.BlogCategories;
import com.ahmed.othman.akhysai.pojo.City;
import com.ahmed.othman.akhysai.pojo.DirectoryCategories;
import com.ahmed.othman.akhysai.pojo.Field;
import com.ahmed.othman.akhysai.pojo.Qualification;
import com.ahmed.othman.akhysai.pojo.Region;
import com.ahmed.othman.akhysai.pojo.Speciality;
import com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity;
import com.ahmed.othman.akhysai.ui.viewModels.AkhysaiViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LauncherActivity extends AppCompatActivity {

    public LauncherActivity() {
        // Required empty public constructor
    }

    public static Akhysai currentAkhysai = new Akhysai();
    public static List<City> Cities = new ArrayList<>();
    public static List<Region> Regions = new ArrayList<>();
    public static List<Field> Fields = new ArrayList<>();
    public static List<BlogCategories> BlogCategories = new ArrayList<>();
    public static List<DirectoryCategories> DirectoryCategories = new ArrayList<>();
    public static List<Qualification> Qualifications = new ArrayList<>();
    public static List<Speciality> Specialties = new ArrayList<>();
    public static List<String> CitiesString = new ArrayList<>(), RegionsString = new ArrayList<>(), FieldsString = new ArrayList<>(), BlogCategoriesString = new ArrayList<>(), DirectoryCategoriesString = new ArrayList<>(), SpecialtiesString = new ArrayList<>(), QualificationsString = new ArrayList<>();
    public static AkhysaiViewModel akhysaiViewModel;
    public static String AppLanguage;

    public final static String shared_pref = "shared_pref";
    public final static String LanguageIso = "language_iso";
    public final static String full_name = "full_name";
    public final static String logged_in = "logged_in";
    public final static String userType = "userType";
    public final static String profileComplete = "profile_complete";
    public final static String profileVerify = "profile_verify";
    public final static String Token = "token";
    public final static String AKHYSAI = "akhysai";
    public final static String PATIENT = "patient";
    public final static String CLINIC = "clinic";


    public final static String ImagesLink = "https://sp.xative.com/public/";
    public final static int CODE1_PERMISSION = 10001;
    public final static int CODE2_PERMISSION = 10002;
    public final static int CODE3_PERMISSION = 10003;
    public final static int PDF_CODE = 20001;
    public final static int GAL_CODE = 20002;
    public final static int GAL_CODE2 = 20003;

    public static AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLanguage = getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage());
        MainActivity.setAppLocale(getResources(), AppLanguage);
        setContentView(R.layout.activity_launcher);

        /* change status bar color **/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.primary));
        }


        akhysaiViewModel = ViewModelProviders.of(this).get(AkhysaiViewModel.class);
//        initSpinnerData();
        new Handler().postDelayed(() -> {
            startActivity(new Intent(LauncherActivity.this, MainActivity.class));
            finish();
        }, Movie.decodeStream(getResources().openRawResource(R.raw.launcher_gif)).duration());

        SharedPreferences sharedPreferences = getSharedPreferences(shared_pref, MODE_PRIVATE);
        if (sharedPreferences.getString(userType, "").equalsIgnoreCase(AKHYSAI))
            getCurrentAkhysaiData(sharedPreferences.getString(Token, ""));
    }

    private void initSpinnerData() {
        akhysaiViewModel.getAllCities(getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()));
        akhysaiViewModel.getAllFields(getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()));
        akhysaiViewModel.getAllBlogCategories(getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()));
        akhysaiViewModel.getAllDirectoryCategories(getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()));
        akhysaiViewModel.getAllQualifications(getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()));
    }

    private void getCurrentAkhysaiData(String token) {

        ApiClient.getINSTANCE().getSpecialistData("Bearer " + token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.w("specialistData", response.body().get("status").getAsString());

                    if (response.body().get("status").getAsString().equals("success")) {
                        currentAkhysai = new Gson().fromJson(response.body().get("data").getAsJsonObject().get("specialist").getAsJsonObject().toString(), Akhysai.class);
                        getSharedPreferences(shared_pref, MODE_PRIVATE)
                                .edit()
                                .putBoolean(profileComplete, true)
                                .putBoolean(profileVerify, true)
                                .apply();
                    } else if (response.body().get("status").getAsString().equalsIgnoreCase("warning")
                            && response.body().get("data").getAsString().equalsIgnoreCase("profile_not_completed")) {
                        getSharedPreferences(shared_pref, MODE_PRIVATE)
                                .edit()
                                .putBoolean(profileComplete, false)
                                .apply();
                    } else if (response.body().get("status").getAsString().equalsIgnoreCase("warning")
                            && response.body().get("data").getAsString().equalsIgnoreCase("waiting_to_verify")) {
                        currentAkhysai = new Gson().fromJson(response.body().get("specialist").getAsJsonObject().get("specialist").getAsJsonObject().toString(), Akhysai.class);
                        getSharedPreferences(shared_pref, MODE_PRIVATE)
                                .edit()
                                .putBoolean(profileComplete, true)
                                .putBoolean(profileVerify, false)
                                .apply();
                    } else if (response.body().get("message").getAsString().equalsIgnoreCase("Unauthenticated")) {
                        getSharedPreferences(shared_pref, MODE_PRIVATE)
                                .edit()
                                .putBoolean(logged_in, false)
                                .apply();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    public static void open_loading_dialog(Context requireContext, LayoutInflater layoutInflater) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext);
        View loading_dialog = layoutInflater.inflate(R.layout.loading_dialog, null);
        builder.setView(loading_dialog).setCancelable(false);
        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static void close_loading_dialog() {
        Log.w("CloseDialog", "CLOSE! " + (dialog == null));
        if (dialog != null) {
            dialog.dismiss();
            dialog.cancel();
        }
    }

    public static NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
    }

}