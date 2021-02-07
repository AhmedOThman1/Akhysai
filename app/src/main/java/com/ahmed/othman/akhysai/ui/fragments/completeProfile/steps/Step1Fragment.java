package com.ahmed.othman.akhysai.ui.fragments.completeProfile.steps;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.ahmed.othman.akhysai.ui.fragments.completeProfile.CompleteProfileFragment;
import com.google.android.material.textfield.TextInputLayout;

public class Step1Fragment extends Fragment {

    public Step1Fragment() {
        // Required empty public constructor
    }
    int step = 0;

    TextInputLayout name_in_en,name_in_ar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_1, container, false);

        name_in_en = view.findViewById(R.id.name_in_en);
        name_in_ar = view.findViewById(R.id.name_in_ar);

        if(LauncherActivity.currentAkhysai.getAr()!=null && LauncherActivity.currentAkhysai.getAr().getName()!=null){
            name_in_ar.getEditText().setText(LauncherActivity.currentAkhysai.getAr().getName().trim());
        }
        if(LauncherActivity.currentAkhysai.getEn()!=null && LauncherActivity.currentAkhysai.getEn().getName()!=null){
            name_in_en.getEditText().setText(LauncherActivity.currentAkhysai.getEn().getName().trim());
        }

        view.findViewById(R.id.next).setOnClickListener(v -> {
            if(name_in_en.getEditText().getText().toString().trim().isEmpty()){
                name_in_en.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                name_in_en.requestFocus();
                open_keyboard(name_in_en.getEditText());
            } else if(name_in_ar.getEditText().getText().toString().trim().isEmpty()){
                name_in_en.setError(null);
                name_in_ar.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                name_in_ar.requestFocus();
                open_keyboard(name_in_ar.getEditText());
            }else {
                name_in_en.setError(null);
                name_in_ar.setError(null);

                CompleteProfileFragment.ProfieData.addProperty("name[en]", name_in_en.getEditText().getText().toString().trim());
                CompleteProfileFragment.ProfieData.addProperty("name[ar]", name_in_ar.getEditText().getText().toString().trim());

                CompleteProfileFragment.stepView.go(1, true);
                Navigation.findNavController(requireActivity(), R.id.frame_stepper).navigate(R.id.action_step1Fragment_to_step2Fragment);
            }
        });

        return view;

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