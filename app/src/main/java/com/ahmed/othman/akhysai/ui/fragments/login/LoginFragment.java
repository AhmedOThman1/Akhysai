package com.ahmed.othman.akhysai.ui.fragments.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.network.ApiClient;
import com.ahmed.othman.akhysai.pojo.Akhysai;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.LanguageIso;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Token;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.close_loading_dialog;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.currentAkhysai;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.logged_in;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.open_loading_dialog;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.profileComplete;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.profileVerify;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.userType;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.toolbar;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.updateNavDrawer;

public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    TextInputLayout username, password;
    TextView sign_up, login;
    CircleImageView facebook, google, phone;
    Context context;
    Activity activity;
    View v;


    private CallbackManager callbackManager;
    private String goTo = "";
    private String Type;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

        context = getContext();
        activity = requireActivity();

        initGoogle();
        initFacebook();

        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        login = view.findViewById(R.id.login);
        sign_up = view.findViewById(R.id.sign_up);
        google = view.findViewById(R.id.google);
        facebook = view.findViewById(R.id.facebook);
        phone = view.findViewById(R.id.phone);

        toolbar.setVisibility(View.GONE);


        Bundle args = getArguments();
        if (args != null) {
            goTo = args.getString("goTo", "");
            Type = args.getString("Type", "");
        }

        login.setOnClickListener(v -> {
            if (username.getEditText().getText().toString().trim().isEmpty()) {
                password.setError(null);
                username.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                username.requestFocus();
                open_keyboard(username.getEditText());
            } else if (!Patterns.EMAIL_ADDRESS.matcher(username.getEditText().getText().toString().trim()).matches()) {
                password.setError(null);
                username.setError(requireActivity().getResources().getString(R.string.enter_valid_email));
                username.requestFocus();
                open_keyboard(username.getEditText());
            } else if (password.getEditText().getText().toString().trim().isEmpty()) {
                username.setError(null);
                password.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                password.requestFocus();
                open_keyboard(password.getEditText());
            } else {
                username.setError(null);
                password.setError(null);
                close_keyboard();
                String language = requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage());
                open_loading_dialog(requireContext(), getLayoutInflater());

                if (Type.equalsIgnoreCase(LauncherActivity.AKHYSAI))
                    AkhysaiLoginRequest(language,
                            username.getEditText().getText().toString().trim(),
                            password.getEditText().getText().toString().trim());
                else if(Type.equalsIgnoreCase(LauncherActivity.PATIENT))
                    PatientLoginRequest(language,
                            username.getEditText().getText().toString().trim(),
                            password.getEditText().getText().toString().trim());
                this.v = v;
//                requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).edit().putBoolean(logged_in, true).apply();
//                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
//                updateNavDrawer(requireActivity());
//                if (goTo.isEmpty())
//                    Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_homeFragment);
//                else if (goTo.equalsIgnoreCase("oneAkhysaiFragmntWriteReview"))
//                    Navigation.findNavController(v).popBackStack(R.id.oneAkhysaiFragment, false);
//                else if (goTo.equalsIgnoreCase("BookOneAkhysaiFragment"))
//                    Navigation.findNavController(v).popBackStack(R.id.bookOneAkhysaiFragment, false);
//                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right).add(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();
            }
        });

        sign_up.setOnClickListener(v -> {
            close_keyboard();
            Bundle bundle = new Bundle();
            bundle.putAll(getArguments());
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_signUpFragment, bundle);
        });

        google.setOnClickListener(v -> {
            this.v = v;
            signUpGoogle();
            close_keyboard();
        });

        facebook.setOnClickListener(v -> {
            this.v = v;
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
            close_keyboard();
        });

        phone.setOnClickListener(v -> {
            close_keyboard();
            Bundle bundle = new Bundle();
            bundle.putAll(getArguments());
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_phoneAuthFragment, bundle);
        });

        return view;
    }


    public void AkhysaiLoginRequest(String languageIso, String email, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", password);

        ApiClient.getINSTANCE().LoginRequest(languageIso,"specialist", jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.w("Login_Response", response.body() + "");
                if (response.isSuccessful()) {
                    Log.w("Login", "AkhysaiViewModel_LoginRequest_onResponse, Body: " + response.body() + " ,status: " + response.body().get("status").getAsString() + " ,Token: " + response.body().get("data").getAsString());

                    String loginStatus = response.body().get("status").getAsString();
                    String loginData = response.body().get("data").getAsString();

                    close_loading_dialog();
                    if (loginStatus != null && loginStatus.equals("success"))
                        getCurrentAkhysaiData(loginData);
                    else
                        Toast.makeText(context, loginData, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, "response is not successful", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (t.getMessage().contains("Unable to resolve host"))
                    Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                            .setAction(R.string.go_to_setting, v -> context.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)))
//                            .setActionTextColor(Color.WHITE)
                            .show();
            }
        });
    }

    private void getCurrentAkhysaiData(String token) {

        ApiClient.getINSTANCE().getSpecialistData("Bearer " + token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.w("specialistData", response.body().get("status").getAsString());

                    if (response.body().get("status").getAsString().equals("success")) {

                        currentAkhysai = new Gson().fromJson(response.body().get("data").getAsJsonObject().get("specialist").getAsJsonObject().toString(), Akhysai.class);
                        requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE)
                                .edit()
                                .putBoolean(logged_in, true)
                                .putBoolean(profileComplete,true)
                                .putBoolean(profileVerify,true)
                                .putString(userType, Type)
                                .putString(Token, token)
                                .apply();
                        Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                        updateNavDrawer(requireActivity());

                        if (goTo.isEmpty())
                            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_homeFragment);
                        else if (goTo.equalsIgnoreCase("oneAkhysaiFragmntWriteReview"))
                            Navigation.findNavController(v).popBackStack(R.id.oneAkhysaiFragment, false);
                        else if (goTo.equalsIgnoreCase("BookOneAkhysaiFragment"))
                            Navigation.findNavController(v).popBackStack(R.id.bookOneAkhysaiFragment, false);
                    } else if (response.body().get("status").getAsString().equals("warning")) {
                        if (response.body().get("data").getAsString().equals("profile_not_completed")) {
                            requireActivity().getSharedPreferences(shared_pref, MODE_PRIVATE)
                                    .edit()
                                    .putBoolean(logged_in, true)
                                    .putBoolean(profileComplete, false)
                                    .putString(userType, Type)
                                    .putString(Token, token)
                                    .apply();
                            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_completeProfileFragment);
                        } else if(response.body().get("data").getAsString().equalsIgnoreCase("waiting_to_verify")){
                            currentAkhysai = new Gson().fromJson(response.body().get("specialist").getAsJsonObject().get("specialist").getAsJsonObject().toString(), Akhysai.class);
                            requireActivity().getSharedPreferences(shared_pref, MODE_PRIVATE)
                                    .edit()
                                    .putBoolean(logged_in, true)
                                    .putBoolean(profileComplete, true)
                                    .putBoolean(profileVerify,false)
                                    .putString(userType, Type)
                                    .putString(Token, token)
                                    .apply();
                            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_verifyProfileFragment);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    public void PatientLoginRequest(String languageIso, String email, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", password);

        ApiClient.getINSTANCE().LoginRequest(languageIso,"user", jsonObject).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.w("Login_Response", response.body() + "");
                if (response.isSuccessful()) {
                    Log.w("LoginP", "LoginRequest_onResponse, Body: " + response.body() + " ,status: " + response.body().get("status").getAsString() + " ,Token: " + response.body().get("data").getAsString());

                    String loginStatus = response.body().get("status").getAsString();
                    String loginData = response.body().get("data").getAsString();

                    close_loading_dialog();
                    if (loginStatus != null && loginStatus.equals("success"))
                        getCurrentPatientData(loginData);
                    else
                        Toast.makeText(context, loginData, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, "response is not successful", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (t.getMessage().contains("Unable to resolve host"))
                    Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                            .setAction(R.string.go_to_setting, v -> context.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)))
//                            .setActionTextColor(Color.WHITE)
                            .show();
            }
        });
    }

    private void getCurrentPatientData(String token){
        requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(logged_in, true)
                .putString(userType, Type)
                .putString(Token, token)
                .apply();
        Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
        updateNavDrawer(requireActivity());

        if (goTo.isEmpty())
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_homeFragment);
        else if (goTo.equalsIgnoreCase("oneAkhysaiFragmntWriteReview"))
            Navigation.findNavController(v).popBackStack(R.id.oneAkhysaiFragment, false);
        else if (goTo.equalsIgnoreCase("BookOneAkhysaiFragment"))
            Navigation.findNavController(v).popBackStack(R.id.bookOneAkhysaiFragment, false);
    }

    private void initFacebook() {

        FacebookSdk.sdkInitialize(context.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(context, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    // google auth begin
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private void initGoogle() {
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    private void signUpGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCredential:success");
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        //TODO check if user new then goto sign up // else go to home
                        if (true == true && !false == !true) {  // logged in before ?

                            requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).edit().putBoolean(logged_in, true).apply();
                            updateNavDrawer(requireActivity());

                            //TODO if(profileComplete==false) nav to completeProfile fragment
                            if (goTo.isEmpty())
                                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_homeFragment);
                            else if (goTo.equalsIgnoreCase("oneAkhysaiFragmntWriteReview"))
                                Navigation.findNavController(v).popBackStack(R.id.oneAkhysaiFragment, false);
                            else if (goTo.equalsIgnoreCase("BookOneAkhysaiFragment"))
                                Navigation.findNavController(v).popBackStack(R.id.bookOneAkhysaiFragment, false);
                        } else {
                            // new user ?
                            Bundle bundle = new Bundle();
                            bundle.putString("sign_from", "google");
                            bundle.putString("goTo", goTo);
                            bundle.putString("name", currentUser.getDisplayName());
                            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_pickSignUpTypeFragment, bundle);
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                    }

                });
    }
    // [END auth_with_google]


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            close_keyboard();
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            //TODO check if user new then goto sign up // else go to home
                            if (true == true && !false == !true) {    // logged in before ?
                                requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).edit().putBoolean(logged_in, true).apply();
                                updateNavDrawer(requireActivity());

                                //TODO if(profileComplete==false) nav to completeProfile fragment
                                if (goTo.isEmpty())
                                    Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_homeFragment);
                                else if (goTo.equalsIgnoreCase("oneAkhysaiFragmntWriteReview"))
                                    Navigation.findNavController(v).popBackStack(R.id.oneAkhysaiFragment, false);
                                else if (goTo.equalsIgnoreCase("BookOneAkhysaiFragment"))
                                    Navigation.findNavController(v).popBackStack(R.id.bookOneAkhysaiFragment, false);
                            } else {
                                // new user ?
                                Bundle bundle = new Bundle();
                                bundle.putString("sign_from", "facebook");
                                bundle.putString("goTo", goTo);
                                bundle.putString("name", currentUser.getDisplayName());
                                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_pickSignUpTypeFragment, bundle);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);     // Context.INPUT_METHOD_SERVICE
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}