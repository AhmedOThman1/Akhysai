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
import android.widget.TextView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;

public class PickSignUpTypeFragment extends Fragment {

    public PickSignUpTypeFragment() {
        // Required empty public constructor
    }

    Context context;
    boolean Login = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pick_sign_up_type, container, false);

        context = getContext();

        toolbar.setVisibility(View.GONE);

        Bundle args = getArguments();
        if (args != null) {
            Login = args.getBoolean("Login", false);
//            if(Login){
//                ((TextView)view.findViewById(R.id.sign_up_patient)).setText(requireActivity().getResources().getString(R.string.login_to_book_session));
//                ((TextView)view.findViewById(R.id.sign_up_akhysai)).setText();
//                ((TextView)view.findViewById(R.id.sign_up_clinic)).setText();
//            }

        }

        view.findViewById(R.id.image1).setOnClickListener(v -> {
            goNextWithType(LauncherActivity.PATIENT,v);
        });
        view.findViewById(R.id.sign_up_patient).setOnClickListener(v -> {
            goNextWithType(LauncherActivity.PATIENT,v);
        });
        view.findViewById(R.id.image2).setOnClickListener(v -> {
            goNextWithType(LauncherActivity.AKHYSAI,v);
        });
        view.findViewById(R.id.sign_up_akhysai).setOnClickListener(v -> {
            goNextWithType(LauncherActivity.AKHYSAI,v);
        });
        view.findViewById(R.id.image3).setOnClickListener(v -> {
          goNextWithType(LauncherActivity.CLINIC,v);
        });
        view.findViewById(R.id.sign_up_clinic).setOnClickListener(v -> {
            goNextWithType(LauncherActivity.CLINIC,v);
        });

        return view;
    }

    private void goNextWithType(String Type,View v){
        Bundle bundle = new Bundle();
        if (getArguments() != null) bundle.putAll(getArguments());
        bundle.putString("Type", Type);
        Navigation.findNavController(v).navigate(
                Login ? R.id.action_pickSignUpTypeFragment_to_loginFragment : R.id.action_pickSignUpTypeFragment_to_signUpFragment
                , bundle);
    }
}