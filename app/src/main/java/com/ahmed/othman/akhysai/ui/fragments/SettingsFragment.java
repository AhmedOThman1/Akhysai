package com.ahmed.othman.akhysai.ui.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.PopupMenu;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.network.ApiClient;
import com.ahmed.othman.akhysai.pojo.Akhysai;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.BlogCategories;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Cities;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.DirectoryCategories;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Fields;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.LanguageIso;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Qualifications;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Token;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.currentAkhysai;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.navigation_view;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.toolbar;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        toolbar.setVisibility(View.VISIBLE);
        navigation_view.setCheckedItem(R.id.nav_settings);

        view.findViewById(R.id.language).setOnClickListener(v -> chooseLanguage());
        view.findViewById(R.id.language_text).setOnClickListener(v -> chooseLanguage());

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(shared_pref, MODE_PRIVATE);
        String userType = sharedPreferences.getString(LauncherActivity.userType, LauncherActivity.PATIENT);
        if (userType.equalsIgnoreCase(LauncherActivity.PATIENT)||userType.isEmpty()) {
            view.findViewById(R.id.profile).setVisibility(View.GONE);
            view.findViewById(R.id.line0).setVisibility(View.GONE);
        }

        view.findViewById(R.id.profile).setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), v);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.specialties:
                        Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_mySpecialtiesFragment);
                        return true;
                    case R.id.edit_profile:
                        if (userType.equalsIgnoreCase(LauncherActivity.AKHYSAI) && currentAkhysai.getApiToken() !=null)
                            Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_editAkhysaiDataFragment);
                        else if (userType.equalsIgnoreCase(LauncherActivity.AKHYSAI)) {
                            justOnce = true;
                            getCurrentAkhysaiData(sharedPreferences.getString(Token, ""));
                        } else
                            Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_editClinicFragment);
                        return true;
                    case R.id.change_password:

                        return true;

                    default:
                        return false;
                }
            });
            popupMenu.inflate(R.menu.profile_settings_menu);
            popupMenu.getMenu().findItem(R.id.specialties).setVisible(userType.equalsIgnoreCase(LauncherActivity.AKHYSAI));
            popupMenu.show();
        });

        view.findViewById(R.id.about_us).setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_aboutUsFragment));

        view.findViewById(R.id.terms_and_conditions).setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_termsAndConditionsFragment));

        view.findViewById(R.id.privacy_policy).setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_privacyPolicyFragment));

        view.findViewById(R.id.contact_us).setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_callUsFragment));


        return view;
    }

    AlertDialog dialog;
    boolean isLanguageAr = false;

    private void chooseLanguage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View change_language_dialog = getLayoutInflater().inflate(R.layout.change_language_dialog, null);

        change_language_dialog.findViewById(R.id.cancel).setOnClickListener(view -> dialog.dismiss());
        change_language_dialog.findViewById(R.id.continue_text).setOnClickListener(view -> {
            requireActivity().getSharedPreferences(shared_pref, MODE_PRIVATE).edit().putString(LanguageIso, isLanguageAr ? "ar" : "en").apply();
            dialog.dismiss();
            // Restart the app

            Cities = new ArrayList<>();
            Fields = new ArrayList<>();
//            BlogCategories = new ArrayList<>();
            DirectoryCategories = new ArrayList<>();
            Qualifications = new ArrayList<>();

            Intent restartIntent = requireActivity().getBaseContext()
                    .getPackageManager()
                    .getLaunchIntentForPackage(requireActivity().getBaseContext().getPackageName());
            assert restartIntent != null;
            restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            requireActivity().startActivity(restartIntent);
            requireActivity().finish();

//            Intent restartIntent = new Intent(requireContext(), LauncherActivity.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 123, restartIntent, PendingIntent.FLAG_CANCEL_CURRENT);
//            AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
//            assert alarmManager != null;
//            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 110, pendingIntent);
//
//            new Handler().postDelayed(() -> System.exit(0), 100);
        });

        RadioButton ar_check = change_language_dialog.findViewById(R.id.ar_check),
                en_check = change_language_dialog.findViewById(R.id.en_check);
        if (requireActivity().getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, "").equalsIgnoreCase("ar")) {
            ar_check.setChecked(true);
        } else {
            en_check.setChecked(true);
        }

        ar_check.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isLanguageAr = true;
                en_check.setChecked(false);
            }
        });
        en_check.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isLanguageAr = false;
                ar_check.setChecked(false);
            }
        });

        change_language_dialog.findViewById(R.id.language_en_layout).setOnClickListener(v -> {
            isLanguageAr = false;
            ar_check.setChecked(false);
            en_check.setChecked(true);
        });

        change_language_dialog.findViewById(R.id.language_ar_layout).setOnClickListener(v -> {
            isLanguageAr = true;
            ar_check.setChecked(true);
            en_check.setChecked(false);
        });

        builder.setView(change_language_dialog);
        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    boolean justOnce = true;

    private void getCurrentAkhysaiData(String token) {

        ApiClient.getINSTANCE().getSpecialistData("Bearer " + token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.w("specialistData", response.body().get("status").getAsString());

                    if (response.body().get("status").getAsString().equals("success")) {
                        currentAkhysai = new Gson().fromJson(response.body().get("data").getAsJsonObject().get("specialist").getAsJsonObject().toString(), Akhysai.class);
                        if (currentAkhysai != null)
                            Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_editAkhysaiDataFragment);

                    }


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

                if (justOnce && t.getMessage().contains("Unable to resolve host")) {
                    Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                            .setAction(R.string.go_to_setting, v -> requireContext().startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)))
//                            .setActionTextColor(Color.WHITE)
                            .show();
                    justOnce = false;

                }

            }
        });
    }

}