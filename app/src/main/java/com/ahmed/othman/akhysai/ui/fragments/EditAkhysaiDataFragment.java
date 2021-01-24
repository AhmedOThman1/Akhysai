package com.ahmed.othman.akhysai.ui.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import android.widget.ScrollView;
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
import com.ahmed.othman.akhysai.pojo.Region;
import com.ahmed.othman.akhysai.pojo.Speciality;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.CODE2_PERMISSION;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Cities;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Fields;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.FieldsString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.GAL_CODE2;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.ImagesLink;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.LanguageIso;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Qualifications;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.QualificationsString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Regions;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Specialties;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.SpecialtiesString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.akhysaiViewModel;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.currentAkhysai;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.CitiesString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.RegionsString;

public class EditAkhysaiDataFragment extends Fragment {

    public EditAkhysaiDataFragment() {
        // Required empty public constructor
    }

    CircleImageView akhysai_image;
    RadioGroup gender;
    RadioButton female, male;
    Spinner city,
            area,
            qualification,
            field,
            specialty;

    TextInputLayout name_in_en,
            name_in_ar,
            birthday,
            id_card_number,
    //////////////////
    address_in_ar,
            address_in_en,
            phone,
    ////////////////
    years_of_experience, about_doctor_en, about_doctor_ar, akhysai_price;


    Uri ImageUri;
    Calendar calendar = Calendar.getInstance();
    ScrollView scroll;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_akhysai_profile, container, false);

        toolbar.setVisibility(View.VISIBLE);
//        initSpinners();

        akhysai_image = view.findViewById(R.id.akhysai_image);

        gender = view.findViewById(R.id.gender);
        female = view.findViewById(R.id.female);
        male = view.findViewById(R.id.male);

        city = view.findViewById(R.id.city);
        area = view.findViewById(R.id.area);
        qualification = view.findViewById(R.id.qualification);
        field = view.findViewById(R.id.field);
        specialty = view.findViewById(R.id.specialty);

        name_in_en = view.findViewById(R.id.name_in_en);
        name_in_ar = view.findViewById(R.id.name_in_ar);
        birthday = view.findViewById(R.id.birthday);
        id_card_number = view.findViewById(R.id.id_card_number);
        address_in_en = view.findViewById(R.id.address_in_en);
        address_in_ar = view.findViewById(R.id.address_in_ar);
        phone = view.findViewById(R.id.phone);
        years_of_experience = view.findViewById(R.id.years_of_experience);
        about_doctor_en = view.findViewById(R.id.about_doctor_en);
        about_doctor_ar = view.findViewById(R.id.about_doctor_ar);
        akhysai_price = view.findViewById(R.id.akhysai_price);
        scroll = view.findViewById(R.id.scroll);

        akhysai_image.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE2_PERMISSION);
            } else {
                Intent gal = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gal, "select media file"), GAL_CODE2);
            }
        });


        ArrayAdapter<String> city_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, CitiesString);
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(city_adapter);
        ArrayAdapter<String> field_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, FieldsString);
        field_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        field.setAdapter(field_adapter);
        ArrayAdapter<String> qualification_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, QualificationsString);
        qualification_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qualification.setAdapter(qualification_adapter);

        Glide.with(view)
                .load(ImagesLink + currentAkhysai.getProfile_picture())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.background_circle_shimmer)
                .into(akhysai_image);

        name_in_ar.getEditText().setText(currentAkhysai.getAr().getName());
        name_in_en.getEditText().setText(currentAkhysai.getEn().getName());
        birthday.getEditText().setText(currentAkhysai.getBirthDate());
        String year = currentAkhysai.getBirthDate().substring(0, 4),
                month = currentAkhysai.getBirthDate().substring(5, 7),
                day = currentAkhysai.getBirthDate().substring(8, 10);
        calendar.set(Calendar.YEAR, Integer.valueOf(year));
        calendar.set(Calendar.MONTH, Integer.valueOf(month)-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(day));
        id_card_number.getEditText().setText(currentAkhysai.getNationalId());
        if (currentAkhysai.getGender().equalsIgnoreCase("m")) {
            male.setChecked(true);
        } else {
            female.setChecked(true);
        }
        for (int i = 0; i < Cities.size(); i++) {
            Log.w("CITY_IDS", "currentAkhysai.getCityId: " + currentAkhysai.getCityId() + ", Cities.get(i).getCityId: " + Cities.get(i).getCityId());
            if (String.valueOf(Cities.get(i).getCityId()).equalsIgnoreCase(currentAkhysai.getCityId())) {
                city.setSelection(i + 1);
                break;
            }
        }

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    akhysaiViewModel.getAllRegionsByCityId(requireActivity().getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()),
                            Cities.get(position - 1).getCityId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        address_in_ar.getEditText().setText(currentAkhysai.getAr().getAddress());
        address_in_en.getEditText().setText(currentAkhysai.getEn().getAddress());
        phone.getEditText().setText(currentAkhysai.getPhone());

        for (int i = 0; i < Qualifications.size(); i++)
            if (String.valueOf(Qualifications.get(i).getQualificationId()).equalsIgnoreCase(currentAkhysai.getQualificationId())) {
                qualification.setSelection(i+1);
                break;
            }

        for (int i = 0; i < Fields.size(); i++)
            if (String.valueOf(Fields.get(i).getFieldId()).equalsIgnoreCase(currentAkhysai.getFieldId())) {
                field.setSelection(i+1);
                break;
            }

        field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    akhysaiViewModel.getAllSpecialitiesByFieldId(requireActivity().getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()),
                            Fields.get(position - 1).getFieldId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        years_of_experience.getEditText().setText(currentAkhysai.getExperienceYears());
        about_doctor_ar.getEditText().setText(currentAkhysai.getAr().getBio());
        about_doctor_en.getEditText().setText(currentAkhysai.getEn().getBio());

        view.findViewById(R.id.edit_akhysai_profile).setOnClickListener(v -> {
            if (name_in_ar.getEditText().getText().toString().trim().isEmpty()) {
                name_in_ar.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                name_in_ar.requestFocus();
                open_keyboard(name_in_ar.getEditText());
            } else if (name_in_ar.getEditText().getText().toString().trim().length() < 6) {
                name_in_ar.setError(requireActivity().getResources().getString(R.string.can_not_be_less_than_6));
                name_in_ar.requestFocus();
                open_keyboard(name_in_ar.getEditText());
            } else if (name_in_en.getEditText().getText().toString().trim().isEmpty()) {
                name_in_ar.setError(null);
                name_in_en.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                name_in_en.requestFocus();
                open_keyboard(name_in_en.getEditText());
            } else if (name_in_en.getEditText().getText().toString().trim().length() < 6) {
                name_in_ar.setError(null);
                name_in_en.setError(requireActivity().getResources().getString(R.string.can_not_be_less_than_6));
                name_in_en.requestFocus();
                open_keyboard(name_in_en.getEditText());
            } else if (birthday.getEditText().getText().toString().trim().isEmpty()) {
                name_in_ar.setError(null);
                name_in_en.setError(null);
                birthday.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                open_pick_birthday();
            } else if (id_card_number.getEditText().getText().toString().trim().isEmpty()) {
                name_in_ar.setError(null);
                name_in_en.setError(null);
                birthday.setError(null);
                id_card_number.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                id_card_number.requestFocus();
                open_keyboard(id_card_number.getEditText());
            }
            /// Sec card
            else if (city.getSelectedItemPosition() == 0) {
                name_in_ar.setError(null);
                name_in_en.setError(null);
                birthday.setError(null);
                id_card_number.setError(null);
                city.setBackgroundResource(R.drawable.background_spinner_error);
                scroll.smoothScrollTo(0,view.findViewById(R.id.edit_akhysai_address_and_communication_card).getTop());
                Toast.makeText(requireContext(), requireActivity().getResources().getString(R.string.choose_city), Toast.LENGTH_SHORT).show();
            } else if (area.getSelectedItemPosition() == 0) {
                name_in_ar.setError(null);
                name_in_en.setError(null);
                birthday.setError(null);
                id_card_number.setError(null);
                city.setBackgroundResource(R.drawable.background_spinner);
                area.setBackgroundResource(R.drawable.background_spinner_error);
                scroll.smoothScrollTo(0,view.findViewById(R.id.edit_akhysai_address_and_communication_card).getTop());
                Toast.makeText(requireContext(), requireActivity().getResources().getString(R.string.choose_region), Toast.LENGTH_SHORT).show();
            } else if (address_in_ar.getEditText().getText().toString().trim().isEmpty()) {
                name_in_ar.setError(null);
                name_in_en.setError(null);
                birthday.setError(null);
                id_card_number.setError(null);
                city.setBackgroundResource(R.drawable.background_spinner);
                area.setBackgroundResource(R.drawable.background_spinner);
                address_in_ar.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                address_in_ar.requestFocus();
                open_keyboard(address_in_ar.getEditText());
            } else if (address_in_en.getEditText().getText().toString().trim().isEmpty()) {
                name_in_ar.setError(null);
                name_in_en.setError(null);
                birthday.setError(null);
                id_card_number.setError(null);
                city.setBackgroundResource(R.drawable.background_spinner);
                area.setBackgroundResource(R.drawable.background_spinner);
                address_in_ar.setError(null);
                address_in_en.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                address_in_en.requestFocus();
                open_keyboard(address_in_en.getEditText());
            } else if (phone.getEditText().getText().toString().trim().isEmpty()) {
                name_in_ar.setError(null);
                name_in_en.setError(null);
                birthday.setError(null);
                id_card_number.setError(null);
                city.setBackgroundResource(R.drawable.background_spinner);
                area.setBackgroundResource(R.drawable.background_spinner);
                address_in_ar.setError(null);
                address_in_en.setError(null);
                phone.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                phone.requestFocus();
                open_keyboard(phone.getEditText());
            } else if (phone.getEditText().getText().toString().trim().length() < 9) {
                name_in_ar.setError(null);
                name_in_en.setError(null);
                birthday.setError(null);
                id_card_number.setError(null);
                city.setBackgroundResource(R.drawable.background_spinner);
                area.setBackgroundResource(R.drawable.background_spinner);
                address_in_ar.setError(null);
                address_in_en.setError(null);
                phone.setError("Enter valid phone number");
                phone.requestFocus();
                open_keyboard(phone.getEditText());
            }
            /// third card
            else if (qualification.getSelectedItemPosition() == 0) {
                name_in_ar.setError(null);
                name_in_en.setError(null);
                birthday.setError(null);
                id_card_number.setError(null);
                city.setBackgroundResource(R.drawable.background_spinner);
                area.setBackgroundResource(R.drawable.background_spinner);
                address_in_ar.setError(null);
                address_in_en.setError(null);
                phone.setError(null);
                qualification.setBackgroundResource(R.drawable.background_spinner_error);
                scroll.smoothScrollTo(0,view.findViewById(R.id.edit_akhysai_professional_information_card).getTop());
                Toast.makeText(requireContext(), requireActivity().getResources().getString(R.string.choose_qualification), Toast.LENGTH_SHORT).show();
            } else if (field.getSelectedItemPosition() == 0) {
                name_in_ar.setError(null);
                name_in_en.setError(null);
                birthday.setError(null);
                id_card_number.setError(null);
                city.setBackgroundResource(R.drawable.background_spinner);
                area.setBackgroundResource(R.drawable.background_spinner);
                address_in_ar.setError(null);
                address_in_en.setError(null);
                phone.setError(null);
                qualification.setBackgroundResource(R.drawable.background_spinner);
                field.setBackgroundResource(R.drawable.background_spinner_error);
                scroll.smoothScrollTo(0,view.findViewById(R.id.edit_akhysai_professional_information_card).getTop());
                Toast.makeText(requireContext(), requireActivity().getResources().getString(R.string.choose_field), Toast.LENGTH_SHORT).show();
            } else if (specialty.getSelectedItemPosition() == 0) {
                name_in_ar.setError(null);
                name_in_en.setError(null);
                birthday.setError(null);
                id_card_number.setError(null);
                city.setBackgroundResource(R.drawable.background_spinner);
                area.setBackgroundResource(R.drawable.background_spinner);
                address_in_ar.setError(null);
                address_in_en.setError(null);
                phone.setError(null);
                qualification.setBackgroundResource(R.drawable.background_spinner);
                field.setBackgroundResource(R.drawable.background_spinner);
                specialty.setBackgroundResource(R.drawable.background_spinner_error);
                scroll.smoothScrollTo(0,view.findViewById(R.id.edit_akhysai_professional_information_card).getTop());
                Toast.makeText(requireContext(), requireActivity().getResources().getString(R.string.choose_specialty), Toast.LENGTH_SHORT).show();
            } else if (years_of_experience.getEditText().getText().toString().trim().isEmpty()) {
                name_in_ar.setError(null);
                name_in_en.setError(null);
                birthday.setError(null);
                id_card_number.setError(null);
                city.setBackgroundResource(R.drawable.background_spinner);
                area.setBackgroundResource(R.drawable.background_spinner);
                address_in_ar.setError(null);
                address_in_en.setError(null);
                phone.setError(null);
                qualification.setBackgroundResource(R.drawable.background_spinner);
                field.setBackgroundResource(R.drawable.background_spinner);
                specialty.setBackgroundResource(R.drawable.background_spinner);
                years_of_experience.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                years_of_experience.requestFocus();
                open_keyboard(years_of_experience.getEditText());
            } else if (about_doctor_en.getEditText().getText().toString().trim().isEmpty()) {
                name_in_ar.setError(null);
                name_in_en.setError(null);
                birthday.setError(null);
                id_card_number.setError(null);
                city.setBackgroundResource(R.drawable.background_spinner);
                area.setBackgroundResource(R.drawable.background_spinner);
                address_in_ar.setError(null);
                address_in_en.setError(null);
                phone.setError(null);
                qualification.setBackgroundResource(R.drawable.background_spinner);
                field.setBackgroundResource(R.drawable.background_spinner);
                specialty.setBackgroundResource(R.drawable.background_spinner);
                years_of_experience.setError(null);
                about_doctor_en.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                about_doctor_en.requestFocus();
                open_keyboard(about_doctor_en.getEditText());
            } else if (about_doctor_ar.getEditText().getText().toString().trim().isEmpty()) {
                name_in_ar.setError(null);
                name_in_en.setError(null);
                birthday.setError(null);
                id_card_number.setError(null);
                city.setBackgroundResource(R.drawable.background_spinner);
                area.setBackgroundResource(R.drawable.background_spinner);
                address_in_ar.setError(null);
                address_in_en.setError(null);
                phone.setError(null);
                qualification.setBackgroundResource(R.drawable.background_spinner);
                field.setBackgroundResource(R.drawable.background_spinner);
                specialty.setBackgroundResource(R.drawable.background_spinner);
                years_of_experience.setError(null);
                about_doctor_en.setError(null);
                about_doctor_ar.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                about_doctor_ar.requestFocus();
                open_keyboard(about_doctor_ar.getEditText());
            } else {
                name_in_ar.setError(null);
                name_in_en.setError(null);
                birthday.setError(null);
                id_card_number.setError(null);
                city.setBackgroundResource(R.drawable.background_spinner);
                area.setBackgroundResource(R.drawable.background_spinner);
                address_in_ar.setError(null);
                address_in_en.setError(null);
                phone.setError(null);
                qualification.setBackgroundResource(R.drawable.background_spinner);
                field.setBackgroundResource(R.drawable.background_spinner);
                specialty.setBackgroundResource(R.drawable.background_spinner);
                years_of_experience.setError(null);
                about_doctor_en.setError(null);
                about_doctor_ar.setError(null);
                Toast.makeText(requireContext(), "Uploading here...", Toast.LENGTH_SHORT).show();
//                Akhysai akhysai = new Akhysai(String.valueOf(ImageUri),
//                        name.getEditText().getText().toString().trim(),
//                        akhysai_description.getEditText().getText().toString().trim(),
//                        about_akhysai.getEditText().getText().toString().trim(),
//                        Integer.parseInt(years_of_experience.getEditText().getText().toString().trim()),
//                        (float) 4,//put the real rate here
//                        19,// put the real visitor num here
//                        Integer.parseInt(akhysai_price.getEditText().getText().toString().trim()),
//                        phone.getEditText().getText().toString().trim(),
//                        calendar.getTimeInMillis(),
//                        male.isChecked(),
//                        id_card_number.getEditText().getText().toString().trim(), new ArrayList<>(), new ArrayList<>());
//                postEditAkhysaiProfileData(akhysai);
            }
        });

        name_in_ar.getEditText().setOnEditorActionListener((v1, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (name_in_ar.getEditText().getText().toString().trim().isEmpty()) {
                    name_in_en.setError(null);
                    name_in_ar.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                    name_in_ar.requestFocus();
                    open_keyboard(name_in_ar.getEditText());
                } else {
                    name_in_en.setError(null);
                    name_in_ar.setError(null);
                    open_pick_birthday();
                }
                return true;
            }
            return false;
        });
        view.findViewById(R.id.pick_birthday).setOnClickListener(v -> open_pick_birthday());

        akhysaiViewModel.regionsMutableLiveData.observe(getViewLifecycleOwner(), regions -> {
            Regions = new ArrayList<>(regions);
            RegionsString.clear();
            RegionsString.add(getResources().getString(R.string.choose_region));
            for (Region region : Regions)
                RegionsString.add(region.getRegionName());

            ArrayAdapter<String> area_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, RegionsString);
            area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(area_adapter);

            for (int i = 0; i < Regions.size(); i++)
                if (String.valueOf(Regions.get(i).getRegionId()).equalsIgnoreCase(currentAkhysai.getRegionId())) {
                    area.setSelection(i+1);
                    break;
                }
        });

        akhysaiViewModel.specialitiesMutableLiveData.observe(getViewLifecycleOwner(), specialities -> {
            Specialties = new ArrayList<>(specialities);
            SpecialtiesString.clear();
            SpecialtiesString.add(getResources().getString(R.string.choose_specialty));
            for (Speciality speciality : Specialties)
                SpecialtiesString.add(speciality.getSpecialityName());

            ArrayAdapter<String> specialty_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, SpecialtiesString);
            specialty_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            specialty.setAdapter(specialty_adapter);
            for (int i = 0; i < Specialties.size(); i++)
                if (String.valueOf(Specialties.get(i).getSpecialityId()).equalsIgnoreCase(currentAkhysai.getSpecialityId())) {
                    specialty.setSelection(i+1);
                    break;
                }
        });

        return view;
    }


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

//    private void initSpinners() {
//        Cities = Arrays.asList(requireContext().getResources().getStringArray(R.array.cities));
//        Areas = Arrays.asList(requireContext().getResources().getStringArray(R.array.cairo));
//    }

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

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // This callback will only be called when MyFragment is at least Started.
//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                // Handle the back button event
////                NavOptions navOptions = new NavOptions.Builder()
////                        .setPopUpTo(R.id.homeFragment, true)
////                        .setEnterAnim(R.anim.slide_in_right)
////                        .setExitAnim(R.anim.slide_out_left)
////                        .setPopEnterAnim(R.anim.slide_in_left)
////                        .setPopExitAnim(R.anim.slide_out_right)
////                        .build();
////                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.homeFragment, null, navOptions);
//
//                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack(R.id.homeFragment, false);
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
//    }
}