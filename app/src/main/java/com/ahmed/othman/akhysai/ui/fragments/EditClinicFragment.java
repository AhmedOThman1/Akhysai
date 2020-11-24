package com.ahmed.othman.akhysai.ui.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.Clinic;
import com.google.android.material.textfield.TextInputLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.CODE2_PERMISSION;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.GAL_CODE2;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;

public class EditClinicFragment extends Fragment {

    public EditClinicFragment() {
        // Required empty public constructor
    }

    RoundedImageView clinic_image;
    TextInputLayout name, email, password, company_name, phone,website,clinic_details;
    Spinner city, area, clinic_category;

    Uri ImageUri;

    List<String> Categories = new ArrayList<>();
    List<String> Cities = new ArrayList<>();
    List<String> Areas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_clinic_data, container, false);

        toolbar.setVisibility(View.VISIBLE);
        initSpinners();

        clinic_image = view.findViewById(R.id.clinic_image);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        company_name = view.findViewById(R.id.company_name);
        phone = view.findViewById(R.id.phone);
        website = view.findViewById(R.id.website);
        clinic_category = view.findViewById(R.id.clinic_category);
        city = view.findViewById(R.id.city);
        area = view.findViewById(R.id.area);
        clinic_details = view.findViewById(R.id.clinic_details);

        clinic_image.setOnClickListener(v -> {
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

        ArrayAdapter<String> field_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Categories);
        field_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clinic_category.setAdapter(field_adapter);


        view.findViewById(R.id.edit_clinic).setOnClickListener(v -> {
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
            } else if (company_name.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                company_name.setError("Can't be empty");
                company_name.requestFocus();
                open_keyboard(company_name.getEditText());
            } else if (phone.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                company_name.setError(null);
                phone.setError("Can't be empty");
                phone.requestFocus();
                open_keyboard(phone.getEditText());
            } else if (phone.getEditText().getText().toString().trim().length() < 9) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                company_name.setError(null);
                phone.setError("Enter valid phone number");
                phone.requestFocus();
                open_keyboard(phone.getEditText());
            }  else if (website.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                company_name.setError(null);
                phone.setError(null);
                website.setError("Can't be empty");
                website.requestFocus();
                open_keyboard(website.getEditText());
            } else if (clinic_category.getSelectedItemPosition() == 0) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                company_name.setError(null);
                phone.setError(null);
                clinic_category.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner_error));
                Toast.makeText(getContext(), "Choose your category", Toast.LENGTH_SHORT).show();
            } else if (city.getSelectedItemPosition() == 0) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                company_name.setError(null);
                phone.setError(null);
                clinic_category.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                city.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner_error));
                Toast.makeText(getContext(), "Choose your city", Toast.LENGTH_SHORT).show();
            } else if (area.getSelectedItemPosition() == 0) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                company_name.setError(null);
                phone.setError(null);
                clinic_category.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                city.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                area.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner_error));
                Toast.makeText(getContext(), "Choose your Area", Toast.LENGTH_SHORT).show();
            } else if (clinic_details.getEditText().getText().toString().trim().isEmpty()) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                company_name.setError(null);
                phone.setError(null);
                clinic_category.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                city.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                area.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                clinic_details.setError("Can't be empty");
                clinic_details.requestFocus();
                open_keyboard(clinic_details.getEditText());
            } else if (ImageUri == null) {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                company_name.setError(null);
                phone.setError(null);
                clinic_details.setError(null);
                clinic_category.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                city.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                area.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                clinic_image.setBorderWidth((float) 2);
                clinic_image.setBorderColor(getContext().getResources().getColor(R.color.error_red));
                Toast.makeText(getContext(), "Upload your profile picture", Toast.LENGTH_SHORT).show();
            } else {
                name.setError(null);
                email.setError(null);
                password.setError(null);
                company_name.setError(null);
                phone.setError(null);
                clinic_details.setError(null);
                clinic_category.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                city.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                area.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                clinic_image.setBorderWidth((float) 0);
                Clinic clinic = new Clinic(String.valueOf(ImageUri),
                        name.getEditText().getText().toString().trim(),
                        Categories.get(clinic_category.getSelectedItemPosition()),
                        phone.getEditText().getText().toString().trim(),
                        website.getEditText().getText().toString().trim(),
                        "", "",
                        Cities.get(city.getSelectedItemPosition()),
                        Areas.get(area.getSelectedItemPosition())
                );
                postEditClinicData(clinic);
            }
        });

        return view;
    }


    private void initSpinners() {
        Cities = Arrays.asList(getContext().getResources().getStringArray(R.array.cities));
        Areas = Arrays.asList(getContext().getResources().getStringArray(R.array.cairo));
        Categories = Arrays.asList(getContext().getResources().getStringArray(R.array.category));
    }

    private void postEditClinicData(Clinic clinic) {
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
                clinic_image.setImageURI(imageUri);
                clinic_image.setBorderWidth((float) 0);
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

}