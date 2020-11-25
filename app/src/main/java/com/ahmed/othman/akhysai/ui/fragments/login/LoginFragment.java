package com.ahmed.othman.akhysai.ui.fragments.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.logged_in;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.updateNavDrawer;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

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

        login.setOnClickListener(v -> {
            if (username.getEditText().getText().toString().trim().isEmpty()) {
                password.setError(null);
                username.setError("Can't be empty");
                username.requestFocus();
                open_keyboard(username.getEditText());
            } else if (!Patterns.EMAIL_ADDRESS.matcher(username.getEditText().getText().toString().trim()).matches()) {
                password.setError(null);
                username.setError("Enter valid email");
                username.requestFocus();
                open_keyboard(username.getEditText());
            } else if (password.getEditText().getText().toString().trim().isEmpty()) {
                username.setError(null);
                password.setError("Can't be empty");
                password.requestFocus();
                open_keyboard(password.getEditText());
            } else {
                username.setError(null);
                password.setError(null);
                close_keyboard();
                requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).edit().putBoolean(logged_in, true).apply();
                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                updateNavDrawer(requireActivity());
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_homeFragment);
//                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right).add(R.id.fragment_container, new HomeFragment()).addToBackStack(null).commit();
            }
        });

        sign_up.setOnClickListener(v -> {
            close_keyboard();
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_pickSignUpTypeFragment);
//            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right).add(R.id.fragment_container, new PickSignUpTypeFragment()).addToBackStack(null).commit();
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
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_phoneAuthFragment);
        });
        return view;
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
                        if (true == true && !false == !true)     // logged in before ?
                            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_homeFragment);
                        else {
                            // new user ?
                            Bundle bundle = new Bundle();
                            bundle.putString("sign_from", "google");
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
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            //TODO check if user new then goto sign up // else go to home
                            if (true == true && !false == !true)     // logged in before ?
                                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_homeFragment);
                            else {
                                // new user ?
                                Bundle bundle = new Bundle();
                                bundle.putString("sign_from", "facebook");
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