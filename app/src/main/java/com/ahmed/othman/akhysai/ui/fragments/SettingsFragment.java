package com.ahmed.othman.akhysai.ui.fragments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.LanguageIso;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.navigation_view;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.setAppLocale;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        toolbar.setVisibility(View.VISIBLE);
        navigation_view.setCheckedItem(R.id.nav_settings);

        view.findViewById(R.id.language).setOnClickListener(v -> chooseLanguage());
        view.findViewById(R.id.language_text).setOnClickListener(v -> chooseLanguage());

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

}