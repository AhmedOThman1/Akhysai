package com.ahmed.othman.akhysai.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.network.ApiClient;
import com.ahmed.othman.akhysai.pojo.Akhysai;
import com.ahmed.othman.akhysai.pojo.BlogCategories;
import com.ahmed.othman.akhysai.pojo.City;
import com.ahmed.othman.akhysai.pojo.DirectoryCategories;
import com.ahmed.othman.akhysai.pojo.Field;
import com.ahmed.othman.akhysai.pojo.Qualification;
import com.ahmed.othman.akhysai.pojo.Region;
import com.ahmed.othman.akhysai.pojo.Speciality;
import com.ahmed.othman.akhysai.ui.viewModels.AkhysaiViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.updateNavDrawer;

public class LauncherActivity extends AppCompatActivity {

    public LauncherActivity() {
        // Required empty public constructor
    }

    public static Akhysai currentAkhysai;
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
    public final static String Token = "token";
    public final static String AKHYSAI = "akhysai";
    public final static String PATIENT = "patient";
    public final static String CLINIC = "clinic";


    public final static String ImagesLink = "https://sp.xative.com/public/";
    public final static int CODE1_PERMISSION = 10001;
    public final static int CODE2_PERMISSION = 10002;
    public final static int PDF_CODE = 10003;
    public final static int GAL_CODE = 10004;
    public final static int GAL_CODE2 = 10005;

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
        initSpinnerData();
        new Handler().postDelayed(() -> {
            startActivity(new Intent(LauncherActivity.this, MainActivity.class));
            finish();
        }, Movie.decodeStream(getResources().openRawResource(R.raw.launcher_gif)).duration());

        SharedPreferences sharedPreferences = getSharedPreferences(shared_pref,MODE_PRIVATE);
        if(sharedPreferences.getString(userType,"").equalsIgnoreCase(AKHYSAI))
            getCurrentAkhysaiData(sharedPreferences.getString(Token,""));
    }

    private void initSpinnerData() {
        akhysaiViewModel.getAllCities(getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()));
        akhysaiViewModel.getAllFields(getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()));
        akhysaiViewModel.getAllBlogCategories(getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()));
        akhysaiViewModel.getAllDirectoryCategories(getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()));
        akhysaiViewModel.getAllQualifications(getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()));

//        akhysaiViewModel.citiesMutableLiveData.observe(this, cities -> {
//            Cities = new ArrayList<>(cities);
//            CitiesString.clear();
//            CitiesString.add(getResources().getString(R.string.choose_city));
//            for (City city : Cities)
//                CitiesString.add(city.getCityName());
//
//        });
//
//        akhysaiViewModel.regionsMutableLiveData.observe(this, regions -> {
//            Regions = new ArrayList<>(regions);
//            RegionsString.clear();
//            RegionsString.add(getResources().getString(R.string.choose_region));
//            for (Region region : Regions)
//                RegionsString.add(region.getRegionName());
//
//        });
//
//        akhysaiViewModel.fieldsMutableLiveData.observe(this, fields -> {
//            Fields = new ArrayList<>(fields);
//            FieldsString.clear();
//            FieldsString.add(getResources().getString(R.string.choose_field));
//            for (Field field : Fields)
//                FieldsString.add(field.getFieldName());
//
//        });
//
//        akhysaiViewModel.specialitiesMutableLiveData.observe(this, specialities -> {
//            Specialties = new ArrayList<>(specialities);
//            SpecialtiesString.clear();
//            SpecialtiesString.add(getResources().getString(R.string.choose_specialty));
//            for (Speciality speciality : Specialties)
//                SpecialtiesString.add(speciality.getSpecialityName());
//
//        });


//        Cities = Arrays.asList(requireContext().getResources().getStringArray(R.array.cities));
//        Areas = Arrays.asList(requireContext().getResources().getStringArray(R.array.cairo));
//        Fields = Arrays.asList(requireContext().getResources().getStringArray(R.array.fields));
//        Specialties = Arrays.asList(requireContext().getResources().getStringArray(R.array.specialties_with_special_needs));
    }

    private void getCurrentAkhysaiData(String token) {

        ApiClient.getINSTANCE().getSpecialistData("Bearer " + token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.w("specialistData", response.body().get("status").getAsString());

                    if (response.body().get("status").getAsString().equals("success"))
                        currentAkhysai = new Gson().fromJson(response.body().get("data").getAsJsonObject().get("specialist").getAsJsonObject().toString(), Akhysai.class);


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
}