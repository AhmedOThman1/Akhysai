package com.ahmed.othman.akhysai.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmed.othman.akhysai.R;

import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.toolbar;

public class AboutUsFragment extends Fragment {

    public AboutUsFragment() {
        // Required empty public constructor
    }

    String AboutUsData = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        toolbar.setVisibility(View.VISIBLE);
        getAboutUsData();

        ((TextView) view.findViewById(R.id.about_us)).setText(AboutUsData);

        return view;
    }

    private void getAboutUsData() {
        AboutUsData = "This About Us Data from server";
    }

}