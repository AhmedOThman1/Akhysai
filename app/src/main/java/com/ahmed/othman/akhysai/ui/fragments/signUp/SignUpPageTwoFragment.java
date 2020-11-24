package com.ahmed.othman.akhysai.ui.fragments.signUp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ahmed.othman.akhysai.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.logged_in;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.updateNavDrawer;

public class SignUpPageTwoFragment extends Fragment {

    public SignUpPageTwoFragment() {
        // Required empty public constructor
    }


    CardView sign_up_second_card;
    TextView sign_up;
    TextInputLayout phone, birthday;
    ImageView back;
    Spinner city, area;
    RadioGroup genderRadio;
    RadioButton male, female;
    View pick_birthday;
    TextView page_num;


    String name_text = "", email_text = "", password_text = "", phone_text = "", birthday_text = "";
    int city_index = 0, area_index = 0;
    boolean gender = true;

    String Type = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_page_two, container, false);


        sign_up_second_card = view.findViewById(R.id.sign_up_second_card);
        sign_up = view.findViewById(R.id.sign_up);
        back = view.findViewById(R.id.back);
        phone = view.findViewById(R.id.phone);
        birthday = view.findViewById(R.id.birthday);
        pick_birthday = view.findViewById(R.id.pick_birthday);
        city = view.findViewById(R.id.city);
        area = view.findViewById(R.id.area);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);
        genderRadio = view.findViewById(R.id.gender);
        page_num = view.findViewById(R.id.page_num);

        toolbar.setVisibility(View.GONE);

        phone.requestFocus();
        open_keyboard(phone.getEditText());

        Bundle arg = getArguments();

        if (arg != null) {

            Type = arg.getString("Type");

            phone_text = arg.getString("phone");
            if (phone_text != null)
                phone.getEditText().setText(phone_text);

            Log.w("TypeSignUp2", "Type: " + Type);
//            Toast.makeText(getContext(), "2"+Type, Toast.LENGTH_SHORT).show();
//            name_text = arg.getString("name", "");
//            email_text = arg.getString("email", "");
//            password_text = arg.getString("password", "");
//            phone_text = arg.getString("phone", "");
//            birthday_text = arg.getString("birthday", "");
//            city_index = arg.getInt("city_index", 0);
//            area_index = arg.getInt("area_index", 0);
//            gender = arg.getBoolean("gender", true);
//
//            if (!phone_text.trim().isEmpty())
//                phone.getEditText().setText(phone_text);
//            if (!birthday_text.trim().isEmpty())
//                birthday.getEditText().setText(birthday_text);
//            if (gender)
//                male.setChecked(true);
//            else
//                female.setChecked(true);

        }

        Log.w("TypeSignUp3", "Type: " + Type);
//        Toast.makeText(getContext(), "3"+Type, Toast.LENGTH_SHORT).show();
        if (Type.equals("patient")) {
            birthday.setVisibility(View.VISIBLE);
            genderRadio.setVisibility(View.VISIBLE);
            sign_up.setText(getContext().getResources().getString(R.string.sign_up_button));
            page_num.setText("2 / 2");
        } else if (Type.equals("akhysai")) {
            birthday.setVisibility(View.VISIBLE);
            genderRadio.setVisibility(View.VISIBLE);
            sign_up.setText(getContext().getResources().getString(R.string.next_button));
            page_num.setText("2 / 3");
        } else if (Type.equals("clinic")) {
            birthday.setVisibility(View.GONE);
            genderRadio.setVisibility(View.GONE);
            page_num.setText("2 / 2");
            sign_up.setText(getContext().getResources().getString(R.string.sign_up_button));
        }
        back.setOnClickListener(v ->
//                {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("name", name_text);
//                    bundle.putString("email", email_text);
//                    bundle.putString("password", password_text);
//                    bundle.putString("phone", phone.getEditText().getText().toString().trim());
//                    bundle.putString("birthday", birthday.getEditText().getText().toString().trim());
//                    bundle.putInt("city_index", city_index);
//                    bundle.putInt("area_index", area_index);
//                    bundle.putBoolean("gender", male.isChecked());
//                    Navigation.findNavController(v).navigate(R.id.action_patientSignUpPageTwoFragment_to_patientSignUpPageOneFragment,bundle);
//                }
                        requireActivity().onBackPressed()

        );

        pick_birthday.setOnClickListener(v -> open_pick_birthday());

        sign_up.setOnClickListener(v -> {
            if (Type.equals("patient") && patient_sign_up(v)) {
                //TODO
            } else if (Type.equals("akhysai") && akhysai_sign_up(v)) {
                //TODO
            } else if (Type.equals("clinic") && clinic_sign_up(v)) {
                //TODO
            }


        });

        phone.getEditText().setOnEditorActionListener((v1, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (phone.getEditText().getText().toString().trim().isEmpty()) {
                    birthday.setError(null);
                    phone.setError("Can't be empty");
                    phone.requestFocus();
                    open_keyboard(phone.getEditText());
                } else if (phone.getEditText().getText().toString().trim().length() < 9) {
                    birthday.setError(null);
                    phone.setError("Enter valid phone number");
                    phone.requestFocus();
                    open_keyboard(phone.getEditText());
                } else if(!Type.equals("clinic")) {
                    phone.setError(null);
                    open_pick_birthday();
                }
                return true;
            }
            return false;
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, getContext().getResources().getStringArray(R.array.cities));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                area.setVisibility(position > 0 ? View.VISIBLE : View.GONE);
                if (position > 0) {
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                            position == 1 ? getContext().getResources().getStringArray(R.array.cairo) :
                                    position == 2 ? getContext().getResources().getStringArray(R.array.giza) :
                                            position == 3 ? getContext().getResources().getStringArray(R.array.alex) :
                                                    getContext().getResources().getStringArray(R.array.mansoura));
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    area.setAdapter(adapter1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private boolean patient_sign_up(View v) {
        if (phone.getEditText().getText().toString().trim().isEmpty()) {
            birthday.setError(null);
            phone.setError("Can't be empty");
            phone.requestFocus();
            open_keyboard(phone.getEditText());
            return false;
        } else if (phone.getEditText().getText().toString().trim().length() < 9) {
            birthday.setError(null);
            phone.setError("Enter valid phone number");
            phone.requestFocus();
            open_keyboard(phone.getEditText());
            return false;
        } else if (birthday.getEditText().getText().toString().trim().isEmpty()) {
            phone.setError(null);
            birthday.setError("Can't be empty");
            open_pick_birthday();
            return false;
        } else if (city.getSelectedItemPosition() == 0) {
            phone.setError(null);
            birthday.setError(null);
            Toast.makeText(getContext(), "Choose your city", Toast.LENGTH_SHORT).show();
            return false;
        } else if (area.getSelectedItemPosition() == 0) {
            phone.setError(null);
            birthday.setError(null);
            Toast.makeText(getContext(), "Choose your Area", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            phone.setError(null);
            birthday.setError(null);
            Navigation.findNavController(v).navigate(R.id.action_signUpPageTwoFragment_to_homeFragment);
            requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).edit().putBoolean(logged_in, true).apply();
            updateNavDrawer(requireActivity());
            return true;
        }
    }

    private boolean akhysai_sign_up(View v) {
        if (phone.getEditText().getText().toString().trim().isEmpty()) {
            birthday.setError(null);
            phone.setError("Can't be empty");
            phone.requestFocus();
            open_keyboard(phone.getEditText());
            return false;
        } else if (phone.getEditText().getText().toString().trim().length() < 9) {
            birthday.setError(null);
            phone.setError("Enter valid phone number");
            phone.requestFocus();
            open_keyboard(phone.getEditText());
            return false;
        } else if (birthday.getEditText().getText().toString().trim().isEmpty()) {
            phone.setError(null);
            birthday.setError("Can't be empty");
            open_pick_birthday();
            return false;
        } else if (city.getSelectedItemPosition() == 0) {
            phone.setError(null);
            birthday.setError(null);
            Toast.makeText(getContext(), "Choose your city", Toast.LENGTH_SHORT).show();
            return false;
        } else if (area.getSelectedItemPosition() == 0) {
            phone.setError(null);
            birthday.setError(null);
            Toast.makeText(getContext(), "Choose your Area", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            phone.setError(null);
            birthday.setError(null);
            Navigation.findNavController(v).navigate(R.id.action_signUpPageTwoFragment_to_signUpPageThreeFragment);
            return true;
        }
    }

    private boolean clinic_sign_up(View v) {
        if (phone.getEditText().getText().toString().trim().isEmpty()) {
            birthday.setError(null);
            phone.setError("Can't be empty");
            phone.requestFocus();
            open_keyboard(phone.getEditText());
            return false;
        } else if (phone.getEditText().getText().toString().trim().length() < 9) {
            birthday.setError(null);
            phone.setError("Enter valid phone number");
            phone.requestFocus();
            open_keyboard(phone.getEditText());
            return false;
        } else if (city.getSelectedItemPosition() == 0) {
            phone.setError(null);
            birthday.setError(null);
            Toast.makeText(getContext(), "Choose your city", Toast.LENGTH_SHORT).show();
            return false;
        } else if (area.getSelectedItemPosition() == 0) {
            phone.setError(null);
            birthday.setError(null);
            Toast.makeText(getContext(), "Choose your Area", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            phone.setError(null);
            birthday.setError(null);
            Navigation.findNavController(v).navigate(R.id.action_signUpPageTwoFragment_to_homeFragment);
            requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).edit().putBoolean(logged_in, true).apply();
            updateNavDrawer(requireActivity());
            return true;
        }
    }

    Calendar calendar = Calendar.getInstance();
    private void open_pick_birthday() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog pickerDialog = new DatePickerDialog(requireContext(), (view, year1, month1, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year1);
            calendar.set(Calendar.MONTH, month1);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String this_day = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "/" + ((month1 + 1) < 10 ? "0" + (month1 + 1) : (month1 + 1)) + "/" + year1;
            birthday.getEditText().setText(this_day);
            birthday.setError(null);

        }, year, month, day);
        pickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        pickerDialog.show();
    }

    private void open_keyboard(EditText textInputLayout) {
        textInputLayout.requestFocus();     // editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);     // Context.INPUT_METHOD_SERVICE
        assert imm != null;
        imm.showSoftInput(textInputLayout, InputMethodManager.SHOW_IMPLICIT); //    first param -> editText

    }
}