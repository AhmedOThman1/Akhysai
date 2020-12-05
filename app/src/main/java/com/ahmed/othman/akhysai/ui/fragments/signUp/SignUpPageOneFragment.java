package com.ahmed.othman.akhysai.ui.fragments.signUp;

import android.content.Context;
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
import com.google.android.material.textfield.TextInputLayout;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;

public class SignUpPageOneFragment extends Fragment {

    public SignUpPageOneFragment() {
        // Required empty public constructor
    }


    CardView sign_up_first_card;
    TextView next;
    TextInputLayout name, email, password;

    String phone = "", birthday = "", name_text = "", email_text = "", password_text = "";
    int city_index = 0, area_index = 0;
    boolean gender = true, HasPhone = false;

    String Type = "", sign_from;
    private String goTo="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_page_one, container, false);

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
            goTo = arg.getString("goTo","");
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
                name.setError("Can't be empty");
                name.requestFocus();
                open_keyboard(name.getEditText());
            } else if (name.getEditText().getText().toString().trim().length() < 6) {
                password.setError(null);
                email.setError(null);
                name.setError("can't be less than 6 characters");
                name.requestFocus();
                open_keyboard(name.getEditText());
            } else if (email.getVisibility() == View.VISIBLE && email.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                password.setError(null);
                email.setError("Can't be empty");
                email.requestFocus();
                open_keyboard(email.getEditText());
            } else if (email.getVisibility() == View.VISIBLE && !Patterns.EMAIL_ADDRESS.matcher(email.getEditText().getText().toString().trim()).matches()) {
                name.setError(null);
                password.setError(null);
                email.setError("Enter valid email");
                email.requestFocus();
                open_keyboard(email.getEditText());
            } else if (password.getVisibility() == View.VISIBLE && password.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                email.setError(null);
                password.setError("Can't be empty");
                password.requestFocus();
                open_keyboard(password.getEditText());
            } else if (password.getVisibility() == View.VISIBLE && password.getEditText().getText().toString().trim().length() < 6) {
                name.setError(null);
                email.setError(null);
                password.setError("can't be less than 6 characters");
                password.requestFocus();
                open_keyboard(password.getEditText());
            } else {
//                ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_out_right,R.anim.slide_in_left,R.anim.slide_out_left,R.anim.slide_in_right).add(R.id.nav_host_fragment, new SignUpPageTwoFragment()).addToBackStack(null).commit();
                name.setError(null);
                email.setError(null);
                password.setError(null);
                close_keyboard();

                Bundle bundle = new Bundle();
                bundle.putString("Type", Type);
                bundle.putString("goTo", goTo);

//                bundle.putString("name", name.getEditText().getText().toString().trim());
//                bundle.putString("email", email.getEditText().getText().toString().trim());
//                bundle.putString("password", password.getEditText().getText().toString().trim());
//                bundle.putString("phone", phone);
//                bundle.putString("birthday", birthday);
//                bundle.putInt("city_index", city_index);
//                bundle.putInt("area_index", area_index);
//                bundle.putBoolean("gender", gender);
                if (HasPhone)
                    bundle.putString("phone", phone);
                Navigation.findNavController(v).navigate(R.id.action_signUpPageOneFragment_to_signUpPageTwoFragment, bundle);
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