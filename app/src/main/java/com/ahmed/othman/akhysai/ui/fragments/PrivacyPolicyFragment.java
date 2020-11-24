package com.ahmed.othman.akhysai.ui.fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmed.othman.akhysai.R;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;

public class PrivacyPolicyFragment extends Fragment {

    public PrivacyPolicyFragment() {
        // Required empty public constructor
    }




    String PrivacyPolicyData ="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_privacy_policy, container, false);

        toolbar.setVisibility(View.VISIBLE);
        getPrivacyPolicyData();

        ((TextView) view.findViewById(R.id.privacy_policy)).setText(PrivacyPolicyData);
        return view;
    }


    private void getPrivacyPolicyData() {
        PrivacyPolicyData = "This Privacy Policy Data from server";
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment, true)
                        .setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left)
                        .setPopEnterAnim(R.anim.slide_in_left)
                        .setPopExitAnim(R.anim.slide_out_right)
                        .build();
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.homeFragment, null, navOptions);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }}