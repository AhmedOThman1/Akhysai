package com.ahmed.othman.akhysai.ui.fragments.signUp;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.network.ApiClient;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.AKHYSAI;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.LanguageIso;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.PATIENT;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Token;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.close_loading_dialog;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.logged_in;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.open_loading_dialog;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.userType;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.toolbar;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.updateNavDrawer;

public class SignUpFragment extends Fragment {

    public SignUpFragment() {
        // Required empty public constructor
    }


    CardView sign_up_first_card;
    TextView next;
    TextInputLayout name, email, password;

    String phone = "", birthday = "", name_text = "", email_text = "", password_text = "";
    int city_index = 0, area_index = 0;
    boolean gender = true, HasPhone = false;
    View view;
    String Type = "", sign_from;
    private String goTo = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        sign_up_first_card = view.findViewById(R.id.sign_up_first_card);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        next = view.findViewById(R.id.next);

        toolbar.setVisibility(View.GONE);

        name.requestFocus();
        open_keyboard(name.getEditText());


        Bundle arg = getArguments();
        if (arg != null) {

            Type = arg.getString("Type");
            Log.w("TypeSignUp1", "Type: " + Type);

            sign_from = arg.getString("sign_from");
            goTo = arg.getString("goTo", "");
            Log.w("GOTO1", "goto: " + goTo);
            if (sign_from == null) {
                //don't do any thing
            } else if (sign_from.equals("google") || sign_from.equals("facebook")) {
                name.getEditText().setText(arg.getString("name"));
                email.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
            } else if (sign_from.equals("phone")) {
                HasPhone = true;
                phone = arg.getString("phone");
                email.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
            }

//            Toast.makeText(getContext(), ""+Type, Toast.LENGTH_SHORT).show();
//
//            name_text = arg.getString("name","");
//            email_text = arg.getString("email","");
//            password_text = arg.getString("password","");
//            phone = arg.getString("phone","");
//            birthday = arg.getString("birthday","");
//            city_index = arg.getInt("city_index",0);
//            area_index = arg.getInt("area_index",0);
//            gender = arg.getBoolean("gender",true);
//
//            if(!name_text.trim().isEmpty())
//                name.getEditText().setText(name_text);
//            if(!email_text.trim().isEmpty())
//                email.getEditText().setText(email_text);
//            if(!password_text.trim().isEmpty())
//                password.getEditText().setText(password_text);

        }
        next.setOnClickListener(v -> {
            if (name.getEditText().getText().toString().trim().isEmpty()) {
                password.setError(null);
                email.setError(null);
                name.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                name.requestFocus();
                open_keyboard(name.getEditText());
            } else if (name.getEditText().getText().toString().trim().length() < 6) {
                password.setError(null);
                email.setError(null);
                name.setError(requireActivity().getResources().getString(R.string.can_not_be_less_than_6));
                name.requestFocus();
                open_keyboard(name.getEditText());
            } else if (email.getVisibility() == View.VISIBLE && email.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                password.setError(null);
                email.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                email.requestFocus();
                open_keyboard(email.getEditText());
            } else if (email.getVisibility() == View.VISIBLE && !Patterns.EMAIL_ADDRESS.matcher(email.getEditText().getText().toString().trim()).matches()) {
                name.setError(null);
                password.setError(null);
                email.setError(requireActivity().getResources().getString(R.string.enter_valid_email));
                email.requestFocus();
                open_keyboard(email.getEditText());
            } else if (password.getVisibility() == View.VISIBLE && password.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                email.setError(null);
                password.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                password.requestFocus();
                open_keyboard(password.getEditText());
            } else if (password.getVisibility() == View.VISIBLE && password.getEditText().getText().toString().trim().length() < 6) {
                name.setError(null);
                email.setError(null);
                password.setError(requireActivity().getResources().getString(R.string.can_not_be_less_than_6));
                password.requestFocus();
                open_keyboard(password.getEditText());
            } else {
//                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_out_right,R.anim.slide_in_left,R.anim.slide_out_left,R.anim.slide_in_right).add(R.id.nav_host_fragment, new SignUpPageTwoFragment()).addToBackStack(null).commit();
                name.setError(null);
                email.setError(null);
                password.setError(null);
                close_keyboard();
                if (Type.equalsIgnoreCase(AKHYSAI))
                    AkhysaiRegisterRequest(
                            requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()),
                            name.getEditText().getText().toString().trim(),
                            email.getEditText().getText().toString().trim(),
                            password.getEditText().getText().toString().trim());

                else if (Type.equalsIgnoreCase(PATIENT))
                    PatientRegisterRequest(
                            requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()),
                            name.getEditText().getText().toString().trim(),
                            email.getEditText().getText().toString().trim(),
                            password.getEditText().getText().toString().trim());

            }

        });

        return view;
    }

    public void AkhysaiRegisterRequest(String languageIso, String name, String email_text, String password) {

        open_loading_dialog(requireContext(), getLayoutInflater());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("email", email_text);
        jsonObject.addProperty("password", password);

        ApiClient.getINSTANCE().RegisterRequest(languageIso, "specialist", jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.w("Register_Response", response.body() + "");
                close_loading_dialog();
                if (response.isSuccessful()) {
                    if (response.body().get("status").getAsString().equals("success")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("Type", Type);
                        bundle.putString("goTo", goTo);
                        requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE)
                                .edit()
                                .putBoolean(logged_in, true)
                                .putString(userType, Type)
                                .putString(Token, response.body().get("data").getAsString())
                                .apply();
                        Toast.makeText(getContext(), "Complete Your Profile", Toast.LENGTH_SHORT).show();
                        if (HasPhone)
                            bundle.putString("phone", phone);
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_signUpFragment_to_completeProfileFragment, bundle);
                    } else if (response.body().get("status").getAsString().equals("error") && response.body().get("data").getAsString().contains("Data validation error")) {
                        email.setError(response.body().get("message").getAsJsonObject().get("email").getAsJsonArray().get(0).getAsString());
                        email.requestFocus();
                        open_keyboard(email.getEditText());
                    }

                } else {
                    Toast.makeText(requireContext(), "error -> response is not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                close_loading_dialog();
                if (t.getMessage().contains("Unable to resolve host"))
                    Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                            .setAction(R.string.go_to_setting, v -> requireContext().startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)))
                            .show();
            }
        });
    }


    public void PatientRegisterRequest(String languageIso, String name, String email_text, String password) {

        open_loading_dialog(requireContext(), getLayoutInflater());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("email", email_text);
        jsonObject.addProperty("password", password);

        ApiClient.getINSTANCE().RegisterRequest(languageIso, "user", jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.w("Register_Response", response.body() + "");
                close_loading_dialog();
                if (response.isSuccessful()) {
                    if (response.body().get("status").getAsString().equals("success")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("Type", Type);
                        bundle.putString("goTo", goTo);
                        requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE)
                                .edit()
                                .putBoolean(logged_in, true)
                                .putString(userType, Type)
                                .putString(Token, response.body().get("data").getAsString())
                                .apply();
                        updateNavDrawer(requireActivity());
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_signUpFragment_to_homeFragment, bundle);
                    } else if (response.body().get("status").getAsString().equals("error") && response.body().get("data").getAsString().contains("Data validation error")) {
                        email.setError(response.body().get("message").getAsJsonObject().get("email").getAsJsonArray().get(0).getAsString());
                        email.requestFocus();
                        open_keyboard(email.getEditText());
                    }

                } else {
                    Toast.makeText(requireContext(), "error -> response is not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                close_loading_dialog();
                if (t.getMessage().contains("Unable to resolve host"))
                    Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                            .setAction(R.string.go_to_setting, v -> requireContext().startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)))
                            .show();
            }
        });
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