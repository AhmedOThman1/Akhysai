package com.ahmed.othman.akhysai.ui.fragments.completeProfile;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.BlogCategories;
import com.ahmed.othman.akhysai.pojo.City;
import com.ahmed.othman.akhysai.pojo.DirectoryCategories;
import com.ahmed.othman.akhysai.pojo.Field;
import com.ahmed.othman.akhysai.pojo.Region;
import com.ahmed.othman.akhysai.pojo.Speciality;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.google.gson.JsonObject;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;

import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.BlogCategoriesString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Cities;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.CitiesString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.DirectoryCategoriesString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Fields;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.FieldsString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Regions;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.RegionsString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Specialties;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.SpecialtiesString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.akhysaiViewModel;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;

public class CompleteProfileFragment extends Fragment {

    public CompleteProfileFragment() {
        // Required empty public constructor
    }


    public static StepView stepView;
    public static JsonObject ProfieData = new JsonObject();
    public static String Type = "",phone_text = "", goTo = "";
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_complete_profile, container, false);

        toolbar.setVisibility(View.GONE);

        stepView = view.findViewById(R.id.step_view);


        if(Cities.isEmpty())
            initArraysData();

        Bundle arg = getArguments();
        if (arg != null) {
            Type = arg.getString("Type");
            if(Type.equalsIgnoreCase(LauncherActivity.AKHYSAI))
                stepView.setStepsNumber(3);
            else
                stepView.setStepsNumber(2);
            requireActivity().getSharedPreferences(shared_pref,Context.MODE_PRIVATE).edit().putString("userType",Type).apply();
            goTo = arg.getString("goTo", "");
            Log.w("GOTO2", "goto: " + goTo);
            phone_text = arg.getString("phone");
        }else{
            Type = requireActivity().getSharedPreferences(shared_pref,Context.MODE_PRIVATE).getString("userType","");
            Toast.makeText(requireContext(), ""+Type, Toast.LENGTH_SHORT).show();
            if(Type.equalsIgnoreCase(LauncherActivity.AKHYSAI))
                stepView.setStepsNumber(3);
            else
                stepView.setStepsNumber(2);
        }


        stepView.go(0,true);

        return view;
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                /* exit only if the user click the back button twice in the Main Activity**/
                if (doubleBackToExitPressedOnce) {
                    requireActivity().finish();
                }
                doubleBackToExitPressedOnce = true;
                Toast.makeText(requireContext(), getResources().getString(R.string.please_click_back_again_to_exit), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }


    private void initArraysData() {

        LauncherActivity.akhysaiViewModel.citiesMutableLiveData.observe(getViewLifecycleOwner(), cities -> {
            Cities = new ArrayList<>(cities);
            CitiesString.clear();
            CitiesString.add(getResources().getString(R.string.choose_city));
            for (City city : Cities)
                CitiesString.add(city.getCityName());

        });

        LauncherActivity.akhysaiViewModel.regionsMutableLiveData.observe(getViewLifecycleOwner(), regions -> {
            Regions = new ArrayList<>(regions);
            RegionsString.clear();
            RegionsString.add(getResources().getString(R.string.choose_region));
            for (Region region : Regions)
                RegionsString.add(region.getRegionName());
        });

        akhysaiViewModel.fieldsMutableLiveData.observe(getViewLifecycleOwner(), fields -> {
            Fields = new ArrayList<>(fields);
            FieldsString.clear();
            FieldsString.add(getResources().getString(R.string.choose_field));
            for (Field field : Fields)
                FieldsString.add(field.getFieldName());

        });

        akhysaiViewModel.specialitiesMutableLiveData.observe(getViewLifecycleOwner(), specialities -> {
            Specialties = new ArrayList<>(specialities);
            SpecialtiesString.clear();
            SpecialtiesString.add(getResources().getString(R.string.choose_specialty));
            for (Speciality speciality : Specialties)
                SpecialtiesString.add(speciality.getSpecialityName());
        });


        akhysaiViewModel.blogCategoriesMutableLiveData.observe(getViewLifecycleOwner(), blogCategories -> {
            LauncherActivity.BlogCategories = new ArrayList<>(blogCategories);
            BlogCategoriesString.clear();
            BlogCategoriesString.add(getResources().getString(R.string.choose_category));
            for (BlogCategories categories : LauncherActivity.BlogCategories)
                BlogCategoriesString.add(categories.getName());

        });


        akhysaiViewModel.directoryCategoriesMutableLiveData.observe(getViewLifecycleOwner(), directoryCategories -> {
            LauncherActivity.DirectoryCategories = new ArrayList<>(directoryCategories);
            DirectoryCategoriesString.clear();
            DirectoryCategoriesString.add(getResources().getString(R.string.choose_category));
            for (DirectoryCategories categories : LauncherActivity.DirectoryCategories)
                DirectoryCategoriesString.add(categories.getName());
        });

    }

    private void open_keyboard(EditText textInputLayout) {
        textInputLayout.requestFocus();     // editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);     // Context.INPUT_METHOD_SERVICE
        assert imm != null;
        imm.showSoftInput(textInputLayout, InputMethodManager.SHOW_IMPLICIT); //    first param -> editText
    }


    private void close_keyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);     // Context.INPUT_METHOD_SERVICE
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}