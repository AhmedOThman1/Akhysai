package com.ahmed.othman.akhysai.ui.fragments.completeProfile.steps;

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

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.Region;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.ahmed.othman.akhysai.ui.fragments.HomeFragment;
import com.ahmed.othman.akhysai.ui.fragments.completeProfile.CompleteProfileFragment;
import com.ahmed.othman.akhysai.ui.viewModels.AkhysaiViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.AKHYSAI;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.CLINIC;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Cities;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.LanguageIso;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.PATIENT;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.logged_in;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.userType;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.updateNavDrawer;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.CitiesString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Regions;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.RegionsString;


public class Step2Fragment extends Fragment {

    public Step2Fragment() {
        // Required empty public constructor
    }


    CardView sign_up_second_card;
    TextView sign_up;
    TextInputLayout phone, birthday;
    Spinner city, area;
    RadioGroup genderRadio;
    RadioButton male, female;
    View pick_birthday;


    String name_text = "", email_text = "", password_text = "", phone_text = "", birthday_text = "";
    int city_index = 0, area_index = 0;
    boolean gender = true;

    String Type = "";
    private String goTo = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_2, container, false);


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

        phone.requestFocus();
        open_keyboard(phone.getEditText());

        Type = CompleteProfileFragment.Type;
        goTo = CompleteProfileFragment.goTo;
        phone_text = CompleteProfileFragment.phone_text;
        if (phone_text != null)
            phone.getEditText().setText(phone_text);

        if (Type.equalsIgnoreCase("patient")) {
            birthday.setVisibility(View.VISIBLE);
            genderRadio.setVisibility(View.VISIBLE);
            sign_up.setText(getContext().getResources().getString(R.string.sign_up_button));
        } else if (Type.equalsIgnoreCase("akhysai")) {
            birthday.setVisibility(View.VISIBLE);
            genderRadio.setVisibility(View.VISIBLE);
            sign_up.setText(getContext().getResources().getString(R.string.next_button));
        } else if (Type.equalsIgnoreCase("clinic")) {
            birthday.setVisibility(View.GONE);
            genderRadio.setVisibility(View.GONE);
            sign_up.setText(getContext().getResources().getString(R.string.sign_up_button));
        }


        pick_birthday.setOnClickListener(v -> open_pick_birthday());

        sign_up.setOnClickListener(v -> {
            if (Type.equalsIgnoreCase(PATIENT) && patient_sign_up(v)) {
                requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).edit()
                        .putBoolean(logged_in, true)
                        .putString(userType, PATIENT)
                        .apply();
                CompleteProfileFragment.stepView.done(true);
                updateNavDrawer(requireActivity());

                fillProfileData(Cities.get(city.getSelectedItemPosition()-1).getCityId(),
                        Regions.get(area.getSelectedItemPosition()-1).getRegionId(),
                        birthday_text,
                        male.isChecked() ? "M" : "F",
                        phone.getEditText().getText().toString().trim());


                if (goTo.isEmpty())
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_completeProfileFragment_to_homeFragment);
                else if (goTo.equalsIgnoreCase("oneAkhysaiFragmntWriteReview"))
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack(R.id.oneAkhysaiFragment, false);
                else if (goTo.equalsIgnoreCase("BookOneAkhysaiFragment"))
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack(R.id.bookOneAkhysaiFragment, false);

            } else if (Type.equalsIgnoreCase(AKHYSAI) && akhysai_sign_up(v)) {

                fillProfileData(Cities.get(city.getSelectedItemPosition()-1).getCityId(),
                        Regions.get(area.getSelectedItemPosition()-1).getRegionId(),
                        birthday_text,
                        male.isChecked() ? "M" : "F",
                        phone.getEditText().getText().toString().trim());

                CompleteProfileFragment.stepView.go(2, true);
                Navigation.findNavController(requireActivity(), R.id.frame_stepper).navigate(R.id.action_step2Fragment_to_step3Fragment);
            } else if (Type.equalsIgnoreCase(CLINIC) && clinic_sign_up(v)) {
                requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).edit()
                        .putBoolean(logged_in, true)
                        .putString(userType, CLINIC)
                        .apply();
                CompleteProfileFragment.stepView.done(true);
                updateNavDrawer(requireActivity());

                fillProfileData(Cities.get(city.getSelectedItemPosition()-1).getCityId(),
                        Regions.get(area.getSelectedItemPosition()-1).getRegionId(),
                        birthday_text,
                        "",
                        phone.getEditText().getText().toString().trim());

                if (goTo.isEmpty())
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_completeProfileFragment_to_homeFragment);
                else if (goTo.equalsIgnoreCase("oneAkhysaiFragmntWriteReview"))
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack(R.id.oneAkhysaiFragment, false);
                else if (goTo.equalsIgnoreCase("BookOneAkhysaiFragment"))
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack(R.id.bookOneAkhysaiFragment, false);
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
                } else if (!Type.equalsIgnoreCase(CLINIC)) {
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
                    LauncherActivity.akhysaiViewModel.getAllRegionsByCityId(requireActivity().getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()), Cities.get(position - 1).getCityId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        LauncherActivity.akhysaiViewModel.regionsMutableLiveData.observe(requireActivity(), regions -> {
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

    private void fillProfileData(int cityId, int regionId, String birthday, String gender, String phone) {
        CompleteProfileFragment.ProfieData.addProperty("city", cityId);
        CompleteProfileFragment.ProfieData.addProperty("region", regionId);
        CompleteProfileFragment.ProfieData.addProperty("birth_date", birthday);
        CompleteProfileFragment.ProfieData.addProperty("gender",gender );
        CompleteProfileFragment.ProfieData.addProperty("phone", phone);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                Navigation.findNavController(requireActivity(), R.id.frame_stepper).popBackStack(R.id.step1Fragment, false);
                CompleteProfileFragment.stepView.go(0, true);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
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

            birthday_text = String.valueOf(year1) + "-" + ((month1 + 1) < 10 ? "0" + (month1 + 1) : (month1 + 1)) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
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