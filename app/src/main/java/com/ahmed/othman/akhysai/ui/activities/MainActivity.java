package com.ahmed.othman.akhysai.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ahmed.othman.akhysai.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.firebase.auth.FirebaseAuth;

import static com.ahmed.othman.akhysai.ui.fragments.HomeFragment.view;

public class MainActivity extends AppCompatActivity {

    public final static String shared_pref = "shared_pref";
    public final static String full_name = "full_name";
    public final static String logged_in = "logged_in";
    public final static int CODE1_PERMISSION = 10001;
    public final static int CODE2_PERMISSION = 10002;
    public final static int PDF_CODE = 10003;
    public final static int GAL_CODE = 10004;
    public final static int GAL_CODE2 = 10005;

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


        navigation_view.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.home && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.homeFragment) {
                NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build();
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.homeFragment, null, navOptions);
            } else if (id == R.id.nav_book_requests && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.bookingRequestsFragment) {
                NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build();
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.bookingRequestsFragment, null, navOptions);
            } else if (id == R.id.nav_edit_akhysai_profile && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.editAkhysaiDataFragment) {
                NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build();
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.editAkhysaiDataFragment, null, navOptions);
            } else if (id == R.id.nav_my_specialty && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.mySpecialtiesFragment) {
                NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build();
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.mySpecialtiesFragment, null, navOptions);
            } else if (id == R.id.nav_my_available_dates && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.myAvailableDatesFragment) {
                NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build();
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.myAvailableDatesFragment, null, navOptions);
            } else if (id == R.id.nav_create_article && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.createNewArticleFragment) {
                NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build();
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.createNewArticleFragment, null, navOptions);
            } else if (id == R.id.nav_my_articles && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.myArticlesFragment) {
                NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build();
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.myArticlesFragment, null, navOptions);
            } else if (id == R.id.book_akhysai && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.searchFragment) {
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
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.loginFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            } else if (id == R.id.share) {
                Intent share_app_intent = new Intent(Intent.ACTION_SEND);
                share_app_intent.setType("text/plain");
                share_app_intent.putExtra(Intent.EXTRA_SUBJECT, "Sharing app");
                share_app_intent.putExtra(Intent.EXTRA_TEXT, "Www.akhysai.com/");
                startActivity(Intent.createChooser(share_app_intent, "Sharing Akhysai app"));
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            } else if (id == R.id.contact_us && Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.callUsFragment) {
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.callUsFragment);
            } else if (id == R.id.logout) {
                FirebaseAuth.getInstance().signOut();
                getSharedPreferences(shared_pref, MODE_PRIVATE).edit().putBoolean(logged_in, false).apply();
                if (Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId() != R.id.homeFragment)
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.homeFragment);
                updateNavDrawer(this);
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

    }

    public static void updateNavDrawer(Activity activity) {
        boolean isLoggedIn = activity.getSharedPreferences(shared_pref, MODE_PRIVATE).getBoolean(logged_in, false);
        String userType = "Akysia";
        navigation_view.getMenu().findItem(R.id.logout).setVisible(isLoggedIn);
        navigation_view.getMenu().findItem(R.id.login).setVisible(!isLoggedIn);
        navigation_view.getMenu().findItem(R.id.join_us).setVisible(!isLoggedIn);
        navigation_view.getMenu().findItem(R.id.nav_book_requests).setVisible(isLoggedIn && (userType.equals("Akysia")));
        navigation_view.getMenu().findItem(R.id.nav_edit_akhysai_profile).setVisible(isLoggedIn && (userType.equals("Akysia")));
        navigation_view.getMenu().findItem(R.id.nav_my_specialty).setVisible(isLoggedIn && (userType.equals("Akysia")));
        navigation_view.getMenu().findItem(R.id.nav_my_available_dates).setVisible(isLoggedIn && (userType.equals("Akysia")));
        navigation_view.getMenu().findItem(R.id.nav_create_article).setVisible(isLoggedIn && (userType.equals("Akysia")));
        navigation_view.getMenu().findItem(R.id.nav_my_articles).setVisible(isLoggedIn && (userType.equals("Akysia")));
        navigation_view.getMenu().findItem(R.id.book_akhysai).setVisible(!isLoggedIn || (!userType.equals("Akysia")));
        navigation_view.getMenu().findItem(R.id.centers_and_clinics).setVisible(!isLoggedIn || (!userType.equals("Akysia")));

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}