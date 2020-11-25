package com.ahmed.othman.akhysai.ui.fragments;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.Article;
import com.google.android.material.textfield.TextInputLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.CODE2_PERMISSION;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.GAL_CODE;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.GAL_CODE2;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.PDF_CODE;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;

public class CreateNewArticleFragment extends Fragment {

    public CreateNewArticleFragment() {
        // Required empty public constructor
    }

    RoundedImageView article_image;
    TextInputLayout article_title, article_body;
    Spinner article_category;
    List<String> Categories = new ArrayList<>();
    Uri ImageUri = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_new_article, container, false);

        toolbar.setVisibility(View.VISIBLE);
        getArticlesCategories();

        article_image = view.findViewById(R.id.article_image);
        article_title = view.findViewById(R.id.article_title);
        article_category = view.findViewById(R.id.article_category);
        article_body = view.findViewById(R.id.article_body);

        article_image.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE2_PERMISSION);
            } else {
                Intent gal = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gal, "select media file"), GAL_CODE2);
            }
        });

        ArrayAdapter<String> field_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Categories);
        field_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        article_category.setAdapter(field_adapter);

        view.findViewById(R.id.create_new_article).setOnClickListener(v -> {
            if (article_category.getSelectedItemPosition() == 0) {
                Toast.makeText(getContext(), "select category", Toast.LENGTH_SHORT).show();
                article_category.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner_error));
            } else if (article_title.getEditText().getText().toString().trim().isEmpty()) {
                article_category.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                article_title.setError("Can't be empty");
                article_title.requestFocus();
                open_keyboard(article_title.getEditText());
            } else if (article_body.getEditText().getText().toString().trim().isEmpty()) {
                article_title.setError(null);
                article_category.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                article_body.setError("Can't be empty");
                article_body.requestFocus();
                open_keyboard(article_body.getEditText());
            } else if (ImageUri == null) {
                article_title.setError(null);
                article_body.setError(null);
                article_category.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));

                article_image.setBorderWidth((float)2);
                article_image.setBorderColor(getContext().getResources().getColor(R.color.error_red));
                Toast.makeText(getContext(), "Upload your profile picture", Toast.LENGTH_SHORT).show();

            } else {
                article_title.setError(null);
                article_body.setError(null);
                article_category.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                Article article = new Article(String.valueOf(ImageUri),
                        article_title.getEditText().getText().toString().trim(),
                        Categories.get(article_category.getSelectedItemPosition()),
                        System.currentTimeMillis(),
                        article_body.getEditText().getText().toString().trim(),
                        "24323423234");

                postNewArticle(article);
            }
        });

        return view;
    }

    private void postNewArticle(Article article) {
        Toast.makeText(requireContext(), "Article added successfully", Toast.LENGTH_SHORT).show();
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
                article_image.setImageURI(imageUri);
                article_image.setBorderWidth((float)0);
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

    private void getArticlesCategories() {
        Categories = Arrays.asList(getContext().getResources().getStringArray(R.array.category));
    }
}