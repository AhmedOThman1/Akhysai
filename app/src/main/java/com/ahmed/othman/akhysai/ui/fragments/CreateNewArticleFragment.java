package com.ahmed.othman.akhysai.ui.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.network.ApiClient;
import com.ahmed.othman.akhysai.pojo.Article;
import com.ahmed.othman.akhysai.pojo.Review;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.ahmed.othman.akhysai.ui.fragments.completeProfile.CompleteProfileFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.BlogCategories;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.BlogCategoriesString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.CODE2_PERMISSION;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.GAL_CODE2;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.LanguageIso;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Token;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.close_loading_dialog;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.full_name;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.open_loading_dialog;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;

public class CreateNewArticleFragment extends Fragment {

    public CreateNewArticleFragment() {
        // Required empty public constructor
    }

    RoundedImageView article_image;
    TextInputLayout article_title, article_body;
    Spinner article_category;
    CardView add_image;
    Uri ImageUri = null;
    Chip ArabicChip, EnglishChip;

    String base64;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_new_article, container, false);

        toolbar.setVisibility(View.VISIBLE);

        article_image = view.findViewById(R.id.article_image);
        article_title = view.findViewById(R.id.article_title);
        article_category = view.findViewById(R.id.article_category);
        article_body = view.findViewById(R.id.article_body);
        add_image = view.findViewById(R.id.add_image);
        EnglishChip = view.findViewById(R.id.english_chip);
        ArabicChip = view.findViewById(R.id.arabic_chip);

        article_image.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE2_PERMISSION);
            } else {
                Intent gal = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gal, "select media file"), GAL_CODE2);
            }
        });

        add_image.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE2_PERMISSION);
            } else {
                Intent gal = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gal, "select media file"), GAL_CODE2);
            }
        });

        if (BlogCategoriesString.isEmpty())
            LauncherActivity.akhysaiViewModel.getAllBlogCategories(requireActivity().getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()));

        ArrayAdapter<String> categories_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, BlogCategoriesString);
        categories_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        article_category.setAdapter(categories_adapter);

        view.findViewById(R.id.create_new_article).setOnClickListener(v -> {
            if (article_category.getSelectedItemPosition() == 0) {
                Toast.makeText(requireContext(), "select category", Toast.LENGTH_SHORT).show();
                article_category.setBackgroundResource(R.drawable.background_spinner_error);
            } else if (article_title.getEditText().getText().toString().trim().isEmpty()) {
                article_category.setBackgroundResource(R.drawable.background_spinner);
                article_title.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                article_title.requestFocus();
                open_keyboard(article_title.getEditText());
            } else if (article_body.getEditText().getText().toString().trim().isEmpty()) {
                article_title.setError(null);
                article_category.setBackgroundResource(R.drawable.background_spinner);
                article_body.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                article_body.requestFocus();
                open_keyboard(article_body.getEditText());
            } else if (ImageUri == null) {
                article_title.setError(null);
                article_body.setError(null);
                article_category.setBackgroundResource(R.drawable.background_spinner);

                article_image.setBorderWidth((float) 2);
                article_image.setBorderColor(requireContext().getResources().getColor(R.color.error_red));
                Toast.makeText(requireContext(), "Upload your profile picture", Toast.LENGTH_SHORT).show();

            } else {
                article_title.setError(null);
                article_body.setError(null);
                article_category.setBackgroundResource(R.drawable.background_spinner);

                postNewArticle(
                        article_title.getEditText().getText().toString().trim(),
                        BlogCategories.get(article_category.getSelectedItemPosition() - 1).getId(),
                        article_body.getEditText().getText().toString().trim(),
                        ArabicChip.isChecked() ? "ar" : "en");
            }
        });

        article_title.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (isArticleWrittenInArOrEn().equals("en"))
                    EnglishChip.setChecked(true);
                else
                    ArabicChip.setChecked(true);
            }
        });
        article_body.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (isArticleWrittenInArOrEn().equals("en"))
                    EnglishChip.setChecked(true);
                else
                    ArabicChip.setChecked(true);
            }
        });


        return view;
    }

    private String isArticleWrittenInArOrEn() {
        int ar_count = 0, en_count = 0;
        String title = article_title.getEditText().getText().toString().trim(),
                body = article_body.getEditText().getText().toString().trim();
        for (int i = 0; i < title.length(); i++) {
            if (Character.UnicodeBlock.of(title.charAt(i)) == Character.UnicodeBlock.ARABIC)
                ar_count++;
            else
                en_count++;
        }

        for (int i = 0; i < body.length(); i++) {
            if (Character.UnicodeBlock.of(body.charAt(i)) == Character.UnicodeBlock.ARABIC)
                ar_count++;
            else
                en_count++;
        }

        return ar_count > en_count ? "ar" : "en";
    }

    private void postNewArticle(String title, int categoryId, String body, String language) {

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE);
        String UserToken = sharedPreferences.getString(Token, "");
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("title", title);
//        jsonObject.addProperty("category", categoryId);
//        jsonObject.addProperty("body", body);
//        jsonObject.addProperty("language", language);
//        jsonObject.addProperty("picture", picture);
//        LauncherActivity.akhysaiViewModel.AddNewArticle("Bearer " + UserToken, jsonObject);

//        String filePath = getRealPathFromUri(ImageUri);
//        if (filePath != null && !filePath.isEmpty()) {
//            File file = new File(filePath);
//            if (file.exists()) {
//                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                AddNewArticle("Bearer " + UserToken,
//                        RequestBody.create(MediaType.parse("multipart/form-data"), title),
//                        RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(categoryId)),
//                        RequestBody.create(MediaType.parse("multipart/form-data"), body),
//                        RequestBody.create(MediaType.parse("multipart/form-data"), language),
//                        MultipartBody.Part.createFormData("picture", file.getName(), requestFile));
//                open_loading_dialog(requireContext(),getLayoutInflater());
//                //TO DO show progress bar
//            }
//        }

        File file = new File(ImageUri.getPath());
        if (file.exists()) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            AddNewArticle("Bearer " + UserToken,
                    RequestBody.create(MediaType.parse("multipart/form-data"), title),
                    RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(categoryId)),
                    RequestBody.create(MediaType.parse("multipart/form-data"), body),
                    RequestBody.create(MediaType.parse("multipart/form-data"), language),
                    MultipartBody.Part.createFormData("picture", file.getName(), requestFile));
            open_loading_dialog(requireContext(),getLayoutInflater());
            //TO DO show progress bar
        }
    }


    private void AddNewArticle(String Authorization, RequestBody title, RequestBody category, RequestBody body, RequestBody language, MultipartBody.Part picture) {
        ApiClient.getINSTANCE().AddNewArticle(Authorization, title, category, body, language, picture).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    if (response.body().get("status").getAsString().equals("success")) {
                        Toast.makeText(requireContext(), "Article added successfully", Toast.LENGTH_SHORT).show();
                        close_loading_dialog();
                        requireActivity().onBackPressed();
                    }

                } else {
                    close_loading_dialog();
                    Toast.makeText(requireContext(), "response is not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (t.getMessage().contains("Unable to resolve host")) {
                    Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                            .setAction(R.string.go_to_setting, v -> requireContext().startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)))
//                            .setActionTextColor(Color.WHITE)
                            .show();
                    close_loading_dialog();
                } else {
                    close_loading_dialog();
                    Toast.makeText(requireContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
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
                if (add_image.getVisibility() != View.GONE)
                    add_image.setVisibility(View.GONE);
                article_image.setImageURI(imageUri);
                article_image.setBorderWidth((float) 0);
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

    public String getRealPathFromUri(final Uri uri) {
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(requireContext(), uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(requireContext(), contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(requireContext(), contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(requireContext(), uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}