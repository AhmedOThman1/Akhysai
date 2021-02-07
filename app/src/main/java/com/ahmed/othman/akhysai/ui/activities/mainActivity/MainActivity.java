package com.ahmed.othman.akhysai.ui.activities.mainActivity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.Akhysai;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Locale;

import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.BlogCategories;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Cities;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.DirectoryCategories;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Fields;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.LanguageIso;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.PATIENT;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Qualifications;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Token;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.akhysaiViewModel;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.currentAkhysai;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.networkChangeReceiver;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.profileComplete;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.profileVerify;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.userType;

public class MainActivity extends AppCompatActivity implements DrawerLocker {


    DrawerLayout drawerLayout;

    public static Toolbar toolbar;
    public static NavigationView navigation_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigation_view = findViewById(R.id.navigation_view);

        updateNavDrawer(this);

        initSpinnerData();

        navigation_view.setNavigationItemSelectedListener(item -> {
            close_keyboard();
            int id = item.getItemId();

            if (id == R.id.home && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.homeFragment) {
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.homeFragment);
            }
            // clinic
            //TO DO move to settings
//            else if (id == R.id.nav_edit_clinic_profile && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.editClinicFragment) {
//                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.editClinicFragment);
//            }

            //akhsai
            else if (id == R.id.nav_book_requests && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.bookingRequestsFragment) {
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.bookingRequestsFragment);
            }
            //TO DO move to settings
//            else if (id == R.id.nav_edit_akhysai_profile && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.editAkhysaiDataFragment) {
//                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.editAkhysaiDataFragment);
//            }
//            else if (id == R.id.nav_my_specialty && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.mySpecialtiesFragment) {
//                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.mySpecialtiesFragment);
//            }
            else if (id == R.id.nav_my_available_dates && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.myAvailableDatesFragment) {
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.myAvailableDatesFragment);
            } else if (id == R.id.nav_my_services && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.myServicesFragment) {
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.myServicesFragment);
            } else if (id == R.id.nav_my_articles && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.myArticlesFragment) {
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.myArticlesFragment);
            }
            //patient
            else if (id == R.id.book_akhysai && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.searchFragment) {
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.searchFragment);
            } else if (id == R.id.nav_settings && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.settingsFragment) {
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.settingsFragment);
            } else if (id == R.id.join_us && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.pickSignUpTypeFragment) {
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.pickSignUpTypeFragment);
            } else if (id == R.id.akhysai_library && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.libraryArticlesFragment) {
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.libraryArticlesFragment);
            } else if (id == R.id.centers_and_clinics && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.centersAndClinicsFragment) {
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.centersAndClinicsFragment);
            } else if (id == R.id.login && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.loginFragment) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("Login", true);
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.pickSignUpTypeFragment, bundle);
//                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.loginFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            } else if (id == R.id.share) {
                Intent share_app_intent = new Intent(Intent.ACTION_SEND);
                share_app_intent.setType("text/plain");
                share_app_intent.putExtra(Intent.EXTRA_SUBJECT, "Sharing app");
                share_app_intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_text));
                startActivity(Intent.createChooser(share_app_intent, "Sharing Akhysai app"));
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            } else if (id == R.id.contact_us && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.callUsFragment) {
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.callUsFragment);
            } else if (id == R.id.logout) {
                FirebaseAuth.getInstance().signOut();
                getSharedPreferences(LauncherActivity.shared_pref, MODE_PRIVATE).edit()
                        .putBoolean(LauncherActivity.logged_in, false)
                        .putBoolean(profileComplete, false)
                        .putBoolean(profileVerify, false)
                        .putString(Token, "")
                        .putString(userType, "")
                        .apply();

                currentAkhysai = new Akhysai();
                // Restart the app

                Intent restartIntent = getBaseContext()
                        .getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                assert restartIntent != null;
                restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                restartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(restartIntent);
                finish();

                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        MaterialShapeDrawable materialShapeDrawable = ((MaterialShapeDrawable) navigation_view.getBackground());
        materialShapeDrawable.setShapeAppearanceModel(
                materialShapeDrawable.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopRightCorner(CornerFamily.ROUNDED, getResources().getDimension(R.dimen.eight_dp))
                        .setTopLeftCorner(CornerFamily.ROUNDED, getResources().getDimension(R.dimen.eight_dp))
                        .build()
        );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.main_nav_graph);

        SharedPreferences sharedPreferences = getSharedPreferences(LauncherActivity.shared_pref, MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(LauncherActivity.logged_in, false) || sharedPreferences.getString(userType,"").equalsIgnoreCase(PATIENT))
            navGraph.setStartDestination(R.id.homeFragment);
        else if (!sharedPreferences.getBoolean(LauncherActivity.profileComplete, false))
            navGraph.setStartDestination(R.id.completeProfileFragment);
        else if (!sharedPreferences.getBoolean(LauncherActivity.profileVerify, false))
            navGraph.setStartDestination(R.id.verifyProfileFragment);
        else navGraph.setStartDestination(R.id.homeFragment);

        navController.setGraph(navGraph);
    }

    public static void updateNavDrawer(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(LauncherActivity.shared_pref, MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(LauncherActivity.logged_in, false);
        String userType = sharedPreferences.getString(LauncherActivity.userType, PATIENT);
        navigation_view.getMenu().findItem(R.id.logout).setVisible(isLoggedIn);
        navigation_view.getMenu().findItem(R.id.login).setVisible(!isLoggedIn);
        navigation_view.getMenu().findItem(R.id.join_us).setVisible(!isLoggedIn);
        navigation_view.getMenu().findItem(R.id.nav_book_requests).setVisible(isLoggedIn && (userType.equalsIgnoreCase(LauncherActivity.AKHYSAI)));
//        navigation_view.getMenu().findItem(R.id.nav_edit_akhysai_profile).setVisible(isLoggedIn && (userType.equalsIgnoreCase(LauncherActivity.AKHYSAI)));
//        navigation_view.getMenu().findItem(R.id.nav_my_specialty).setVisible(isLoggedIn && (userType.equalsIgnoreCase(LauncherActivity.AKHYSAI)));
        navigation_view.getMenu().findItem(R.id.nav_my_available_dates).setVisible(isLoggedIn && (userType.equalsIgnoreCase(LauncherActivity.AKHYSAI)));
        navigation_view.getMenu().findItem(R.id.nav_my_services).setVisible(isLoggedIn && (userType.equalsIgnoreCase(LauncherActivity.AKHYSAI)));
        navigation_view.getMenu().findItem(R.id.nav_my_articles).setVisible(isLoggedIn && (userType.equalsIgnoreCase(LauncherActivity.AKHYSAI)));
        navigation_view.getMenu().findItem(R.id.book_akhysai).setVisible(!isLoggedIn || (!userType.equalsIgnoreCase(LauncherActivity.AKHYSAI)));
        navigation_view.getMenu().findItem(R.id.centers_and_clinics).setVisible(!isLoggedIn || (!userType.equalsIgnoreCase(LauncherActivity.AKHYSAI)));
//        navigation_view.getMenu().findItem(R.id.nav_edit_clinic_profile).setVisible(userType.equalsIgnoreCase("Clinic"));
    }

    @Override
    public void setDrawerLocked(boolean enabled){
        if(enabled)
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        else
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }


    public static void setAppLocale(Resources res, String language_code) {
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            conf.setLocale(new Locale(language_code.toLowerCase())); // API 17+ only.
        else
            conf.locale = new Locale(language_code.toLowerCase());

        res.updateConfiguration(conf, dm);
    }

    private void initSpinnerData() {
        if (Cities.isEmpty())
            akhysaiViewModel.getAllCities(getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()));
        if (Fields.isEmpty())
            akhysaiViewModel.getAllFields(getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()));
        if (BlogCategories.isEmpty())
            akhysaiViewModel.getAllBlogCategories(getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()));
        if (DirectoryCategories.isEmpty())
            akhysaiViewModel.getAllDirectoryCategories(getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()));
        if (Qualifications.isEmpty())
            akhysaiViewModel.getAllQualifications(getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()));
    }

    private void close_keyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);     // Context.INPUT_METHOD_SERVICE
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(networkChangeReceiver);
        } catch (Exception e) {

        }
    }



}