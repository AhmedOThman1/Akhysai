package com.ahmed.othman.akhysai.ui.fragments.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ahmed.othman.akhysai.R;
import com.chaos.view.PinView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.logged_in;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.updateNavDrawer;

public class PhoneAuthPageTwoFragment extends Fragment {

    public PhoneAuthPageTwoFragment() {
        // Required empty public constructor
    }

    Context context;
    Activity activity;
    PinView PinView;
    View view;
    String phone;

    FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_phone_auth_page_two, container, false);

        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("en");
        toolbar.setVisibility(View.GONE);

        Log.w("Page two", "Here!");
        PinView = view.findViewById(R.id.PinView);

        activity = requireActivity();
        context = getContext();

        Bundle arg = getArguments();
        if (arg != null) {
            phone = arg.getString("phone");
            Log.w("PHONEA", "phone: " + phone);
            Toast.makeText(context, "phone: " + phone, Toast.LENGTH_SHORT).show();
            sendVerificationCode(phone);
        } else {
            Log.w("NULLLLL", arg + "#");
            Log.w("NULLLLL", savedInstanceState + "");
        }


        view.findViewById(R.id.back).setOnClickListener(v -> requireActivity().onBackPressed());
        view.findViewById(R.id.login_with_phone).setOnClickListener(v -> {
            if (PinView.getText().toString().trim().length() == 6 && PinView.getText().toString().trim().equals(code)) {
                PinView.setError(null);
                verifyVerificationCode(code);
            } else {
                Log.w("CodeError",code+", he wrote: "+PinView.getText().toString().trim());
                PinView.setError("Enter the correct code");
                PinView.setText("");
                PinView.requestFocus();
            }
        });

        PinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String temp = s.toString().trim();
                if (temp.length() == 6 && temp.equals(code)) {
                    PinView.setError(null);
                    verifyVerificationCode(code);
                } else if (temp.length() == 6) {
                    Log.w("CodeError",code+", he wrote: "+temp);
                    PinView.setError("Enter the correct code");
                    PinView.setText("");
                    PinView.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }


    // phone auth
    String mVerificationId;
    String code;

    private void sendVerificationCode(String mobile) {

//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(mAuth)
//                        .setPhoneNumber("+20"+mobile)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(activity)                 // Activity (for callback binding)
//                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+20" + mobile,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                activity,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        //Getting the code sent by SMS
                        Log.w("WTH", "Kong FU2");
                        code = phoneAuthCredential.getSmsCode();

                        Log.w("CODE", "" + code);
                        Toast.makeText(context, "Code: " + code, Toast.LENGTH_SHORT).show();
                        //sometime the code is not detected automatically
                        //in this case the code will be null
                        //so user has to manually enter the code

                        if (code != null) {

                            PinView.setText(code);
                            PinView.setError(null);

                            //verifying the code
                            verifyVerificationCode(code);
                        }
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        Toast.makeText(context, "error" + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.w("ERROR_AUTH", e.getMessage());
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);

                        //storing the verification id that is sent to the user
                        mVerificationId = s;
                    }


                });
    }

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {

                        // TODO write the correct condition
                        if (true == true && !false == !true)     // logged in before ?
                        {
                            requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).edit().putBoolean(logged_in, true).apply();
                            updateNavDrawer(requireActivity());
                            Navigation.findNavController(view).navigate(R.id.action_phoneAuthPageTwoFragment_to_homeFragment);
                        } else {
                            // new user ?
                            Bundle bundle = new Bundle();
                            bundle.putString("sign_from", "phone");
                            bundle.putString("phone", phone);
                            Navigation.findNavController(view).navigate(R.id.action_phoneAuthPageTwoFragment_to_pickSignUpTypeFragment, bundle);
                        }
                    } else {
                        String message = "Something is wrong, we will fix it soon...";

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered...";
                        }
                        Toast.makeText(context, message + "", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}