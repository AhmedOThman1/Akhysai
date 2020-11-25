package com.ahmed.othman.akhysai.ui.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.Akhysai;
import com.ahmed.othman.akhysai.pojo.Clinic;
import com.google.android.material.textfield.TextInputLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.CODE2_PERMISSION;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.GAL_CODE2;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;

public class EditAkhysaiDataFragment extends Fragment {

    public EditAkhysaiDataFragment() {
        // Required empty public constructor
    }

    CircleImageView akhysai_image;
    TextInputLayout name, email, password, phone, birthday, years_of_experience, akhysai_price, id_card_number, akhysai_description, about_akhysai;
    Spinner city, area;
    RadioGroup gender;
    RadioButton male;
    Uri ImageUri;

    List<String> Cities = new ArrayList<>();
    List<String> Areas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_akhysai_profile, container, false);

        toolbar.setVisibility(View.VISIBLE);
        initSpinners();

        akhysai_image = view.findViewById(R.id.akhysai_image);

        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        phone = view.findViewById(R.id.phone);
        birthday = view.findViewById(R.id.birthday);
        gender = view.findViewById(R.id.gender);
        years_of_experience = view.findViewById(R.id.years_of_experience);
        city = view.findViewById(R.id.city);
        area = view.findViewById(R.id.area);
        akhysai_price = view.findViewById(R.id.akhysai_price);
        id_card_number = view.findViewById(R.id.id_card_number);
        akhysai_description = view.findViewById(R.id.akhysai_description);
        about_akhysai = view.findViewById(R.id.about_akhysai);
        akhysai_price = view.findViewById(R.id.akhysai_price);
        male = view.findViewById(R.id.male);

        akhysai_image.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE2_PERMISSION);
            } else {
                Intent gal = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gal, "select media file"), GAL_CODE2);
            }
        });


        ArrayAdapter<String> city_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Cities);
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(city_adapter);

        ArrayAdapter<String> area_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Areas);
        area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        area.setAdapter(area_adapter);

        view.findViewById(R.id.edit_akhysai_profile).setOnClickListener(v -> {
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
            } else if (phone.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                phone.setError("Can't be empty");
                phone.requestFocus();
                open_keyboard(phone.getEditText());
            } else if (phone.getEditText().getText().toString().trim().length() < 9) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                phone.setError("Enter valid phone number");
                phone.requestFocus();
                open_keyboard(phone.getEditText());
            } else if (birthday.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                phone.setError(null);
                birthday.setError("Can't be empty");
                open_pick_birthday();
            } else if (city.getSelectedItemPosition() == 0) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                birthday.setError(null);
                phone.setError(null);
                city.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner_error));
                Toast.makeText(getContext(), "Choose your city", Toast.LENGTH_SHORT).show();
            } else if (area.getSelectedItemPosition() == 0) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                birthday.setError(null);
                phone.setError(null);
                city.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                area.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner_error));
                Toast.makeText(getContext(), "Choose your Area", Toast.LENGTH_SHORT).show();
            } else if (years_of_experience.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                birthday.setError(null);
                phone.setError(null);
                city.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                area.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                years_of_experience.setError("Can't be empty");
                years_of_experience.requestFocus();
                open_keyboard(years_of_experience.getEditText());
            } else if (akhysai_price.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                birthday.setError(null);
                phone.setError(null);
                years_of_experience.setError(null);
                city.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                area.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                akhysai_price.setError("Enter valid phone number");
                akhysai_price.requestFocus();
                open_keyboard(akhysai_price.getEditText());
            } else if (id_card_number.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                birthday.setError(null);
                phone.setError(null);
                years_of_experience.setError(null);
                akhysai_price.setError(null);
                city.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                area.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                id_card_number.setError("Enter valid phone number");
                id_card_number.requestFocus();
                open_keyboard(id_card_number.getEditText());
            } else if (akhysai_description.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                birthday.setError(null);
                phone.setError(null);
                years_of_experience.setError(null);
                akhysai_price.setError(null);
                id_card_number.setError(null);
                city.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                area.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                akhysai_description.setError("Enter valid phone number");
                akhysai_description.requestFocus();
                open_keyboard(akhysai_description.getEditText());
            } else if (about_akhysai.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                birthday.setError(null);
                phone.setError(null);
                years_of_experience.setError(null);
                akhysai_price.setError(null);
                id_card_number.setError(null);
                akhysai_description.setError(null);
                city.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                area.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                about_akhysai.setError("Enter valid phone number");
                about_akhysai.requestFocus();
                open_keyboard(about_akhysai.getEditText());
            } else if (ImageUri == null) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                birthday.setError(null);
                phone.setError(null);
                years_of_experience.setError(null);
                city.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                area.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                akhysai_image.setBorderWidth(2);
                akhysai_image.setBorderColor(getContext().getResources().getColor(R.color.error_red));
                Toast.makeText(getContext(), "Upload your profile picture", Toast.LENGTH_SHORT).show();
            } else {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                birthday.setError(null);
                phone.setError(null);
                years_of_experience.setError(null);
                city.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                area.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                akhysai_image.setBorderWidth(0);
                Akhysai akhysai = new Akhysai(String.valueOf(ImageUri),
                        name.getEditText().getText().toString().trim(),
                        akhysai_description.getEditText().getText().toString().trim(),
                        about_akhysai.getEditText().getText().toString().trim(),
                        Integer.parseInt(years_of_experience.getEditText().getText().toString().trim()),
                        (float) 4,//put the real rate here
                        19,// put the real visitor num here
                        Integer.parseInt(akhysai_price.getEditText().getText().toString().trim()),
                        phone.getEditText().getText().toString().trim(),
                        calendar.getTimeInMillis(),
                        male.isChecked(),
                        id_card_number.getEditText().getText().toString().trim(), new ArrayList<>(), new ArrayList<>());
                postEditAkhysaiProfileData(akhysai);
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
                } else {
                    phone.setError(null);
                    open_pick_birthday();
                }
                return true;
            }
            return false;
        });

        return view;
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

    private void initSpinners() {
        Cities = Arrays.asList(getContext().getResources().getStringArray(R.array.cities));
        Areas = Arrays.asList(getContext().getResources().getStringArray(R.array.cairo));
    }

    private void postEditAkhysaiProfileData(Akhysai akhysai) {
        Toast.makeText(requireContext(), "Your data has been changed successfully", Toast.LENGTH_SHORT).show();
        requireActivity().onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE2_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent gal = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gal, "select media file"), GAL_CODE2);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GAL_CODE2 && resultCode == RESULT_OK) {
            // image from gallery

            Uri imageUri = data.getData();
            if (imageUri != null) {
                // one image
                akhysai_image.setImageURI(imageUri);
                akhysai_image.setBorderWidth(0);
                ImageUri = imageUri;
            }
        }
    }

    private void open_keyboard(EditText textInputLayout) {
        textInputLayout.requestFocus();     // editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);     // Context.INPUT_METHOD_SERVICE
        assert imm != null;
        imm.showSoftInput(textInputLayout, InputMethodManager.SHOW_IMPLICIT); //    first param -> editText
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment, true)
                        .setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left)
                        .setPopEnterAnim(R.anim.slide_in_left)
                        .setPopExitAnim(R.anim.slide_out_right)
                        .build();
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.homeFragment, null, navOptions);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}