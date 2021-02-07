package com.ahmed.othman.akhysai.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmed.othman.akhysai.R;

import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.toolbar;

public class TermsAndConditionsFragment extends Fragment {

    public TermsAndConditionsFragment() {
        // Required empty public constructor
    }

    String TermsAndConditionsData = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_terms_and_conditions, container, false);

        toolbar.setVisibility(View.VISIBLE);

        getTermsAndConditionsData();

        ((TextView) view.findViewById(R.id.terms_and_conditions)).setText(TermsAndConditionsData);
        return view;
    }

    private void getTermsAndConditionsData() {
        TermsAndConditionsData = "This Terms And Conditions Data from server";
    }

}