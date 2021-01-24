package com.ahmed.othman.akhysai.ui.fragments.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ahmed.othman.akhysai.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;

public class PhoneAuthFragment extends Fragment {

    public PhoneAuthFragment() {
        // Required empty public constructor
    }

    TextInputLayout phone_number;
    Context context;
    Activity activity;
    private String goTo="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_phone_auth, container, false);

        phone_number = view.findViewById(R.id.phone_number);
        activity = requireActivity();
        context = getContext();
        phone_number.requestFocus();
        open_keyboard(phone_number.getEditText());

        toolbar.setVisibility(View.GONE);

        Bundle args = getArguments();
        if (args != null) {
            goTo = args.getString("goTo", "");
            Log.w("GOTO P1", "goto: " + goTo);
        }

        view.findViewById(R.id.back).setOnClickListener(v -> requireActivity().onBackPressed());
        view.findViewById(R.id.login_with_phone).setOnClickListener(v -> {
            if (phone_number.getEditText().getText().toString().trim().isEmpty()) {
                phone_number.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                phone_number.requestFocus();
                open_keyboard(phone_number.getEditText());
            } else if (phone_number.getEditText().getText().toString().trim().length() < 9) {
                phone_number.setError("Enter valid phone number");
                phone_number.requestFocus();
                open_keyboard(phone_number.getEditText());
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("phone", phone_number.getEditText().getText().toString().trim());
                bundle.putString("goTo",goTo);
                Log.w("PHONEAUTH",phone_number.getEditText().getText().toString().trim());
                Navigation.findNavController(v).navigate(R.id.action_phoneAuthFragment_to_phoneAuthPageTwoFragment, bundle);
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
}