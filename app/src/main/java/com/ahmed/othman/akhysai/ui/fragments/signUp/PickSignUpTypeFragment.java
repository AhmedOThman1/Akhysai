package com.ahmed.othman.akhysai.ui.fragments.signUp;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahmed.othman.akhysai.R;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;

public class PickSignUpTypeFragment extends Fragment {

    public PickSignUpTypeFragment() {
        // Required empty public constructor
    }

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pick_sign_up_type, container, false);

        context = getContext();

        toolbar.setVisibility(View.GONE);

        view.findViewById(R.id.image1).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if(getArguments()!=null) bundle.putAll(getArguments());
            bundle.putString("Type", "patient");
            Navigation.findNavController(v).navigate(R.id.action_pickSignUpTypeFragment_to_signUpPageOneFragment, bundle);
        });
        view.findViewById(R.id.sign_up_patient).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if(getArguments()!=null) bundle.putAll(getArguments());
            bundle.putString("Type", "patient");
            Navigation.findNavController(v).navigate(R.id.action_pickSignUpTypeFragment_to_signUpPageOneFragment, bundle);
        });
        view.findViewById(R.id.image2).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if(getArguments()!=null) bundle.putAll(getArguments());
            bundle.putString("Type", "akhysai");
            Navigation.findNavController(v).navigate(R.id.action_pickSignUpTypeFragment_to_signUpPageOneFragment, bundle);
        });
        view.findViewById(R.id.sign_up_akhysai).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if(getArguments()!=null) bundle.putAll(getArguments());
            bundle.putString("Type", "akhysai");
            Navigation.findNavController(v).navigate(R.id.action_pickSignUpTypeFragment_to_signUpPageOneFragment, bundle);
        });
        view.findViewById(R.id.image3).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if(getArguments()!=null) bundle.putAll(getArguments());
            bundle.putString("Type", "clinic");
            Navigation.findNavController(v).navigate(R.id.action_pickSignUpTypeFragment_to_signUpPageOneFragment, bundle);
        });
        view.findViewById(R.id.sign_up_clinic).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            if(getArguments()!=null) bundle.putAll(getArguments());
            bundle.putString("Type", "clinic");
            Navigation.findNavController(v).navigate(R.id.action_pickSignUpTypeFragment_to_signUpPageOneFragment, bundle);
        });

        return view;
    }
}