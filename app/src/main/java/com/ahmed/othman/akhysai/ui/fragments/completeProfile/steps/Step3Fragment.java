package com.ahmed.othman.akhysai.ui.fragments.completeProfile.steps;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.network.ApiClient;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.ahmed.othman.akhysai.ui.fragments.completeProfile.CompleteProfileFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.CODE1_PERMISSION;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.CODE2_PERMISSION;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.GAL_CODE;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.GAL_CODE2;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.PDF_CODE;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Token;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.close_loading_dialog;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.logged_in;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.open_loading_dialog;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.profileComplete;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.updateNavDrawer;

public class Step3Fragment extends Fragment {

    public Step3Fragment() {
        // Required empty public constructor
    }

    CardView sign_up_three_card;
    TextView sign_up;
    TextInputLayout years_of_experience, id_card_number, about_doctor_en, about_doctor_ar;
    ImageView pdf_cv, image_cv;
    CircleImageView doctor_photo;

    ArrayList<String> Images = new ArrayList<>();

    Uri ImageUri = null;
    View view;
    private String goTo = "";

    JsonObject ProfileData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_step_3, container, false);


        sign_up_three_card = view.findViewById(R.id.sign_up_second_card);
        sign_up = view.findViewById(R.id.sign_up);
//        back = view.findViewById(R.id.back);
        years_of_experience = view.findViewById(R.id.years_of_experience);
        id_card_number = view.findViewById(R.id.id_card_number);
        about_doctor_en = view.findViewById(R.id.about_doctor_en);
        about_doctor_ar = view.findViewById(R.id.about_doctor_ar);
        pdf_cv = view.findViewById(R.id.pdf_cv);
        image_cv = view.findViewById(R.id.image_cv);
        doctor_photo = view.findViewById(R.id.doctor_photo);

        Bundle arg = getArguments();
//
//        back.setOnClickListener(v ->
//                requireActivity().onBackPressed()
//        );

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
                //TO DONE
                CompleteProfileFragment.stepView.done(true);
                close_keyboard();
                fillProfileData();
                uploadSignUpData();
            }


        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                Navigation.findNavController(requireActivity(), R.id.frame_stepper).popBackStack(R.id.step2Fragment, false);
                CompleteProfileFragment.stepView.go(1, true);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
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
        } else if (about_doctor_en.getEditText().getText().toString().trim().isEmpty()) {
            years_of_experience.setError(null);
            id_card_number.setError(null);
            about_doctor_en.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
            about_doctor_en.requestFocus();
            open_keyboard(about_doctor_en.getEditText());
            return false;
        } else if (about_doctor_ar.getEditText().getText().toString().trim().isEmpty()) {
            years_of_experience.setError(null);
            id_card_number.setError(null);
            about_doctor_en.setError(null);
            about_doctor_ar.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
            about_doctor_ar.requestFocus();
            open_keyboard(about_doctor_ar.getEditText());
            return false;
        }
//        else if (Images.isEmpty()) {
//            years_of_experience.setError(null);
//            id_card_number.setError(null);
//            about_doctor_en.setError(null);
//            about_doctor_ar.setError(null);
//            Toast.makeText(getContext(), "Upload your CV first", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        else if (ImageUri == null) {
            years_of_experience.setError(null);
            id_card_number.setError(null);
            about_doctor_en.setError(null);
            about_doctor_ar.setError(null);

            doctor_photo.setBorderWidth(2);
            doctor_photo.setBorderColor(getContext().getResources().getColor(R.color.error_red));

            Toast.makeText(getContext(), "Upload your profile picture", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            years_of_experience.setError(null);
            id_card_number.setError(null);
            about_doctor_en.setError(null);
            about_doctor_ar.setError(null);

            return true;
        }
    }

    private void fillProfileData() {

        CompleteProfileFragment.ProfieData.addProperty("years_of_experience", years_of_experience.getEditText().getText().toString().trim());
        CompleteProfileFragment.ProfieData.addProperty("national_id", id_card_number.getEditText().getText().toString().trim());
        CompleteProfileFragment.ProfieData.addProperty("bio[en]", about_doctor_en.getEditText().getText().toString().trim());
        CompleteProfileFragment.ProfieData.addProperty("bio[ar]", about_doctor_ar.getEditText().getText().toString().trim());

        CompleteProfileFragment.ProfieData.addProperty("qualification", 0);
        CompleteProfileFragment.ProfieData.addProperty("field", 0);
        CompleteProfileFragment.ProfieData.addProperty("speciality", 0);
        CompleteProfileFragment.ProfieData.addProperty("address[ar]", "null");
        CompleteProfileFragment.ProfieData.addProperty("address[en]", "null");

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), ImageUri);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            CompleteProfileFragment.ProfieData.addProperty("profile_picture", Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT));
        } catch (IOException e) {
            e.printStackTrace();
            CompleteProfileFragment.ProfieData.addProperty("profile_picture", "");
        }

    }

    void uploadSignUpData() {
        CompleteProfileFragment.ProfieData.addProperty("profile_picture", "");

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE);
        String UserToken = sharedPreferences.getString(Token, "");

        File file = saveFile(ImageUri);
        if (file.exists()) {
            open_loading_dialog(requireContext(), getLayoutInflater());

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            ApiClient.getINSTANCE().CompleteProfile("Bearer " + UserToken,
                    RequestBody.create(MediaType.parse("multipart/form-data"), CompleteProfileFragment.ProfieData.get("name[ar]").getAsString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), CompleteProfileFragment.ProfieData.get("name[en]").getAsString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), CompleteProfileFragment.ProfieData.get("city").getAsString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), CompleteProfileFragment.ProfieData.get("region").getAsString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), CompleteProfileFragment.ProfieData.get("national_id").getAsString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), CompleteProfileFragment.ProfieData.get("birth_date").getAsString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), CompleteProfileFragment.ProfieData.get("gender").getAsString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), CompleteProfileFragment.ProfieData.get("phone").getAsString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), CompleteProfileFragment.ProfieData.get("years_of_experience").getAsString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), CompleteProfileFragment.ProfieData.get("bio[ar]").getAsString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), CompleteProfileFragment.ProfieData.get("bio[en]").getAsString()),
                    MultipartBody.Part.createFormData("profile_picture", file.getName(), requestFile),
                    RequestBody.create(MediaType.parse("multipart/form-data"), CompleteProfileFragment.ProfieData.get("address[ar]").getAsString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), CompleteProfileFragment.ProfieData.get("address[en]").getAsString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), CompleteProfileFragment.ProfieData.get("field").getAsString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), CompleteProfileFragment.ProfieData.get("speciality").getAsString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), CompleteProfileFragment.ProfieData.get("qualification").getAsString())
            ).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.w("CompleteProfileResponse", response.body() + "");
                    close_loading_dialog();
                    if (response.isSuccessful()) {
                        Log.w("JSON", "" + response.body().get("status").getAsString());
                        if (response.body().get("status").getAsString().equalsIgnoreCase("success")) {
                            requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).edit()
                                    .putBoolean(logged_in, true)
                                    .putBoolean(profileComplete, true)
                                    .putString("userType", LauncherActivity.AKHYSAI)
                                    .apply();
                            updateNavDrawer(requireActivity());
                            if (goTo.isEmpty())
                                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_completeProfileFragment_to_homeFragment);
                            else if (goTo.equalsIgnoreCase("oneAkhysaiFragmntWriteReview"))
                                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack(R.id.oneAkhysaiFragment, false);
                            else if (goTo.equalsIgnoreCase("BookOneAkhysaiFragment"))
                                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack(R.id.bookOneAkhysaiFragment, false);
                        }
                    } else {
                        Log.w("CompNotSucc", "" + response);
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_completeProfileFragment_to_homeFragment);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    close_loading_dialog();
                    if (t.getMessage().contains("Unable to resolve host"))
                        Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                                .setAction(R.string.go_to_setting, v -> requireContext().startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)))
                                .show();
                }
            });

        }
        else
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_completeProfileFragment_to_homeFragment);

    }

    private File saveFile(Uri uri){
        String sourseFileName = uri.getPath();
        //                    get image type ( png , jpg , webp )
        ContentResolver cR = requireContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(uri));
        Log.w("Log", "image1 type : " + type);
        // end of get image type
        String destinationFileName = android.os.Environment.getExternalStorageDirectory().getPath()+File.separatorChar+"Image"+type;
        BufferedInputStream bis =null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourseFileName));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFileName, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                if(bis!=null)bis.close();
                if(bos!=null)bos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return new File(destinationFileName);
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