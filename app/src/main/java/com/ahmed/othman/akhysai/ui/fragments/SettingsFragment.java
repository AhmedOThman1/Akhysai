package com.ahmed.othman.akhysai.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.ahmed.othman.akhysai.R;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.navigation_view;
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

    private void chooseLanguage() {

    }

}