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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.Region;
import com.ahmed.othman.akhysai.ui.viewModels.AkhysaiViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Cities;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.LanguageIso;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.logged_in;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.toolbar;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.updateNavDrawer;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.CitiesString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Regions;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.RegionsString;

public class SignUpPageTwoFragment extends Fragment {

    public SignUpPageTwoFragment() {
        // Required empty public constructor
    }


    CardView sign_up_second_card;
    TextView sign_up;
    TextInputLayout phone, birthday;
    Spinner city, area;
    RadioGroup genderRadio;
    RadioButton male, female;
    View pick_birthday;
    TextView page_num;


    String name_text = "", email_text = "", password_text = "", phone_text = "", birthday_text = "";
    int city_index = 0, area_index = 0;
    boolean gender = true;

    String Type = "";
    private String goTo = "";

    AkhysaiViewModel akhysaiViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_page_two, container, false);

        akhysaiViewModel = ViewModelProviders.of(requireActivity()).get(AkhysaiViewModel.class);


        sign_up_second_card = view.findViewById(R.id.sign_up_second_card);
        sign_up = view.findViewById(R.id.sign_up);
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
            goTo = arg.getString("goTo", "");
            Log.w("GOTO2", "goto: " + goTo);

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
        if (Type.equalsIgnoreCase("patient")) {
            birthday.setVisibility(View.VISIBLE);
            genderRadio.setVisibility(View.VISIBLE);
            sign_up.setText(getContext().getResources().getString(R.string.sign_up_button));
            page_num.setText("2 / 2");
        } else if (Type.equalsIgnoreCase("akhysai")) {
            birthday.setVisibility(View.VISIBLE);
            genderRadio.setVisibility(View.VISIBLE);
            sign_up.setText(getContext().getResources().getString(R.string.next_button));
            page_num.setText("2 / 3");
        } else if (Type.equalsIgnoreCase("clinic")) {
            birthday.setVisibility(View.GONE);
            genderRadio.setVisibility(View.GONE);
            page_num.setText("2 / 2");
            sign_up.setText(getContext().getResources().getString(R.string.sign_up_button));
        }


        pick_birthday.setOnClickListener(v -> open_pick_birthday());

        sign_up.setOnClickListener(v -> {
            if (Type.equalsIgnoreCase("patient") && patient_sign_up(v)) {
                requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).edit()
                        .putBoolean(logged_in, true)
                        .putString("userType", "Patient")
                        .apply();
                updateNavDrawer(requireActivity());
                if (goTo.isEmpty())
                    Navigation.findNavController(v).navigate(R.id.action_signUpPageTwoFragment_to_homeFragment);
                else if (goTo.equalsIgnoreCase("oneAkhysaiFragmntWriteReview"))
                    Navigation.findNavController(v).popBackStack(R.id.oneAkhysaiFragment, false);
                else if (goTo.equalsIgnoreCase("BookOneAkhysaiFragment"))
                    Navigation.findNavController(v).popBackStack(R.id.bookOneAkhysaiFragment, false);

            } else if (Type.equalsIgnoreCase("akhysai") && akhysai_sign_up(v)) {
                Bundle bundle = new Bundle();
                bundle.putString("goTo", goTo);
                Navigation.findNavController(v).navigate(R.id.action_signUpPageTwoFragment_to_signUpPageThreeFragment, bundle);
            } else if (Type.equalsIgnoreCase("clinic") && clinic_sign_up(v)) {
                requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).edit()
                        .putBoolean(logged_in, true)
                        .putString("userType", "Clinic")
                        .apply();
                updateNavDrawer(requireActivity());
                if (goTo.isEmpty())
                    Navigation.findNavController(v).navigate(R.id.action_signUpPageTwoFragment_to_homeFragment);
                else if (goTo.equalsIgnoreCase("oneAkhysaiFragmntWriteReview"))
                    Navigation.findNavController(v).popBackStack(R.id.oneAkhysaiFragment, false);
                else if (goTo.equalsIgnoreCase("BookOneAkhysaiFragment"))
                    Navigation.findNavController(v).popBackStack(R.id.bookOneAkhysaiFragment, false);
            }


        });

        phone.getEditText().setOnEditorActionListener((v1, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (phone.getEditText().getText().toString().trim().isEmpty()) {
                    birthday.setError(null);
                    phone.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                    phone.requestFocus();
                    open_keyboard(phone.getEditText());
                } else if (phone.getEditText().getText().toString().trim().length() < 9) {
                    birthday.setError(null);
                    phone.setError("Enter valid phone number");
                    phone.requestFocus();
                    open_keyboard(phone.getEditText());
                } else if (!Type.equalsIgnoreCase("clinic")) {
                    phone.setError(null);
                    open_pick_birthday();
                }
                return true;
            }
            return false;
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, CitiesString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                area.setVisibility(position > 0 ? View.VISIBLE : View.GONE);
                if (position > 0) {
                    akhysaiViewModel.getAllRegionsByCityId(requireActivity().getSharedPreferences(shared_pref,MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()),Cities.get(position-1).getCityId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        akhysaiViewModel.regionsMutableLiveData.observe(requireActivity(), regions -> {
            Regions = new ArrayList<>(regions);
            RegionsString.clear();
            RegionsString.add(requireActivity().getResources().getString(R.string.choose_region));
            for (Region region : Regions)
                RegionsString.add(region.getRegionName());

            ArrayAdapter<String> area_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, RegionsString);
            area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(area_adapter);
        });

        return view;
    }

    private boolean patient_sign_up(View v) {
        if (phone.getEditText().getText().toString().trim().isEmpty()) {
            birthday.setError(null);
            phone.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
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
            birthday.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
            open_pick_birthday();
            return false;
        } else if (city.getSelectedItemPosition() == 0) {
            phone.setError(null);
            birthday.setError(null);
            city.setBackgroundResource(R.drawable.background_spinner_error);
            Toast.makeText(getContext(), "Choose your city", Toast.LENGTH_SHORT).show();
            return false;
        } else if (area.getSelectedItemPosition() == 0) {
            phone.setError(null);
            birthday.setError(null);
            city.setBackgroundResource(R.drawable.background_spinner);
            area.setBackgroundResource(R.drawable.background_spinner_error);
            Toast.makeText(getContext(), "Choose your Area", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            phone.setError(null);
            birthday.setError(null);
            close_keyboard();
            return true;
        }
    }

    private boolean akhysai_sign_up(View v) {
        if (phone.getEditText().getText().toString().trim().isEmpty()) {
            birthday.setError(null);
            phone.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
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
            birthday.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
            open_pick_birthday();
            return false;
        } else if (city.getSelectedItemPosition() == 0) {
            phone.setError(null);
            birthday.setError(null);
            city.setBackgroundResource(R.drawable.background_spinner_error);
            Toast.makeText(getContext(), "Choose your city", Toast.LENGTH_SHORT).show();
            return false;
        } else if (area.getSelectedItemPosition() == 0) {
            phone.setError(null);
            birthday.setError(null);
            city.setBackgroundResource(R.drawable.background_spinner);
            area.setBackgroundResource(R.drawable.background_spinner_error);
            Toast.makeText(getContext(), "Choose your Area", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            phone.setError(null);
            birthday.setError(null);
            close_keyboard();
            return true;
        }
    }

    private boolean clinic_sign_up(View v) {
        if (phone.getEditText().getText().toString().trim().isEmpty()) {
            birthday.setError(null);
            phone.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
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
            city.setBackgroundResource(R.drawable.background_spinner_error);
            Toast.makeText(getContext(), "Choose your city", Toast.LENGTH_SHORT).show();
            return false;
        } else if (area.getSelectedItemPosition() == 0) {
            phone.setError(null);
            birthday.setError(null);
            city.setBackgroundResource(R.drawable.background_spinner);
            area.setBackgroundResource(R.drawable.background_spinner_error);
            Toast.makeText(getContext(), "Choose your Area", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            phone.setError(null);
            birthday.setError(null);
            close_keyboard();
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

    private void close_keyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);     // Context.INPUT_METHOD_SERVICE
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}