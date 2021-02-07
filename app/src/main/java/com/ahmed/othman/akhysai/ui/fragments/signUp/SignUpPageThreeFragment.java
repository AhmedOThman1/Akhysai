package com.ahmed.othman.akhysai.ui.fragments.signUp;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.CODE1_PERMISSION;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.CODE2_PERMISSION;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.GAL_CODE;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.GAL_CODE2;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.PDF_CODE;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.logged_in;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.toolbar;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.updateNavDrawer;

public class SignUpPageThreeFragment extends Fragment {

    public SignUpPageThreeFragment() {
        // Required empty public constructor
    }


    CardView sign_up_three_card;
    TextView sign_up;
    TextInputLayout years_of_experience, id_card_number, about_doctor;
    ImageView back, pdf_cv, image_cv;
    CircleImageView doctor_photo;

    ArrayList<String> Images = new ArrayList<>();

    Uri ImageUri = null;

    private String goTo="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_page_three, container, false);


        sign_up_three_card = view.findViewById(R.id.sign_up_second_card);
        sign_up = view.findViewById(R.id.sign_up);
        back = view.findViewById(R.id.back);
        years_of_experience = view.findViewById(R.id.years_of_experience);
        id_card_number = view.findViewById(R.id.id_card_number);
        about_doctor = view.findViewById(R.id.about_doctor);
        pdf_cv = view.findViewById(R.id.pdf_cv);
        image_cv = view.findViewById(R.id.image_cv);
        doctor_photo = view.findViewById(R.id.doctor_photo);

        toolbar.setVisibility(View.GONE);


        Bundle arg = getArguments();

        if (arg != null) {
            goTo = arg.getString("goTo", "");
            Log.w("GOTO3", "goto: " + goTo);
        }

        back.setOnClickListener(v ->
                requireActivity().onBackPressed()
        );

        pdf_cv.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE1_PERMISSION);
            } else {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a pdf !"), PDF_CODE);
            }
        });

        image_cv.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE2_PERMISSION);
            } else {
                Intent gal = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
                gal.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                gal.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*"});
                startActivityForResult(Intent.createChooser(gal, "select media file"), GAL_CODE);
            }

        });

        doctor_photo.setOnClickListener(v -> {
            Intent gal = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(gal, "select media file"), GAL_CODE2);
        });

        sign_up.setOnClickListener(v -> {
            if (akhysai_sign_up(v)) {
                //TODO
            }


        });

        return view;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE1_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a pdf !"), PDF_CODE);
        } else if (requestCode == CODE2_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent gal = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
            gal.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            gal.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*"});
            startActivityForResult(Intent.createChooser(gal, "select media file"), GAL_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GAL_CODE && resultCode == RESULT_OK) {
            // image from gallery
            assert data != null;
            Images.clear();
            Uri imageUri = data.getData();
            ClipData clipData = data.getClipData();
            if (imageUri != null) {
                // one image
                Images.add(String.valueOf(imageUri));
            } else if (clipData != null && clipData.toString().contains("image%")) {
                // multi image
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri image = clipData.getItemAt(i).getUri();
                    Images.add(String.valueOf(image));
                }
            }
        } else if (requestCode == PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Images.clear();
            Images.add(String.valueOf(data.getData()));
//            pdfUri = data.getData();
        } else if (requestCode == GAL_CODE2 && resultCode == RESULT_OK) {
            // image from gallery

            Uri imageUri = data.getData();
            if (imageUri != null) {
                // one image
                doctor_photo.setImageURI(imageUri);
                doctor_photo.setBorderWidth(2);
                doctor_photo.setBorderColor(getContext().getResources().getColor(R.color.primary));
                ImageUri = imageUri;
            }
        }
    }


    private boolean akhysai_sign_up(View v) {
        if (years_of_experience.getEditText().getText().toString().trim().isEmpty()) {
            years_of_experience.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
            years_of_experience.requestFocus();
            open_keyboard(years_of_experience.getEditText());
            return false;
        } else if (id_card_number.getEditText().getText().toString().trim().isEmpty()) {
            years_of_experience.setError(null);
            id_card_number.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
            id_card_number.requestFocus();
            open_keyboard(id_card_number.getEditText());
            return false;
        } else if (about_doctor.getEditText().getText().toString().trim().isEmpty()) {
            years_of_experience.setError(null);
            id_card_number.setError(null);
            about_doctor.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
            about_doctor.requestFocus();
            open_keyboard(about_doctor.getEditText());
            return false;
        } else if (Images.isEmpty()) {
            years_of_experience.setError(null);
            id_card_number.setError(null);
            about_doctor.setError(null);
            Toast.makeText(getContext(), "Upload your CV first", Toast.LENGTH_SHORT).show();
            return false;
        } else if (ImageUri == null) {
            years_of_experience.setError(null);
            id_card_number.setError(null);
            about_doctor.setError(null);

            doctor_photo.setBorderWidth(2);
            doctor_photo.setBorderColor(getContext().getResources().getColor(R.color.error_red));

            Toast.makeText(getContext(), "Upload your profile picture", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            years_of_experience.setError(null);
            id_card_number.setError(null);
            about_doctor.setError(null);
            close_keyboard();
            uploadSignUpData();
            requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).edit()
                    .putBoolean(logged_in, true)
                    .putString("userType", LauncherActivity.AKHYSAI)
                    .apply();
            updateNavDrawer(requireActivity());
            if (goTo.isEmpty())
                Navigation.findNavController(v).navigate(R.id.action_signUpPageThreeFragment_to_homeFragment);
            else if (goTo.equalsIgnoreCase("oneAkhysaiFragmntWriteReview"))
                Navigation.findNavController(v).popBackStack(R.id.oneAkhysaiFragment, false);
            else if (goTo.equalsIgnoreCase("BookOneAkhysaiFragment"))
                Navigation.findNavController(v).popBackStack(R.id.bookOneAkhysaiFragment, false);
            return true;
        }
    }

    void uploadSignUpData() {

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