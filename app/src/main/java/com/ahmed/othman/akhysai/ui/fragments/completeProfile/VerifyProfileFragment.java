package com.ahmed.othman.akhysai.ui.fragments.completeProfile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.ahmed.othman.akhysai.ui.activities.mainActivity.DrawerLocker;
import com.google.firebase.auth.FirebaseAuth;

import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Token;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.currentAkhysai;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.profileComplete;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.profileVerify;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.userType;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.toolbar;

public class VerifyProfileFragment extends Fragment {

    public VerifyProfileFragment() {
        // Required empty public constructor
    }

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_verify_profile, container, false);

        toolbar.setVisibility(View.GONE);
        ((DrawerLocker)requireActivity()).setDrawerLocked(true);

        view.findViewById(R.id.go_to_complete_profile).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_verifyProfileFragment_to_completeProfileFragment);
        });

        return view;
    }


    AlertDialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                /* exit only if the user click the back button twice in the Main Activity**/
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                View exitLogout = getLayoutInflater().inflate(R.layout.exit_logout_dialog,null);
                exitLogout.findViewById(R.id.exit).setOnClickListener(v->{
                    dialog.dismiss();
                    requireActivity().finish();
                });
                exitLogout.findViewById(R.id.logout).setOnClickListener(v->{
                    dialog.dismiss();
                    FirebaseAuth.getInstance().signOut();
                    requireActivity().getSharedPreferences(LauncherActivity.shared_pref, Context.MODE_PRIVATE).edit()
                            .putBoolean(LauncherActivity.logged_in, false)
                            .putBoolean(profileComplete, false)
                            .putBoolean(profileVerify, false)
                            .putString(Token, "")
                            .putString(userType, "")
                            .apply();

                    currentAkhysai = null;

                    // Restart the app

                    Intent restartIntent = requireActivity().getBaseContext()
                            .getPackageManager()
                            .getLaunchIntentForPackage(requireActivity().getBaseContext().getPackageName());
                    assert restartIntent != null;
                    restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    requireActivity().startActivity(restartIntent);
                    requireActivity().finish();
                });

                builder.setView(exitLogout);
                dialog = builder.create();
                dialog.show();
                Window window = dialog.getWindow();
                assert window != null;
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((DrawerLocker)requireActivity()).setDrawerLocked(false);
    }
}