package com.ahmed.othman.akhysai.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;

import java.util.Locale;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (ConnectivityManager.CONNECTIVITY_ACTION.equalsIgnoreCase(intent.getAction())) {
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            Log.w("Network","NoConnectivity: "+noConnectivity);
            if (!noConnectivity) {
                if(LauncherActivity.Cities.isEmpty()){
                    LauncherActivity.akhysaiViewModel.getAllCities(context.getSharedPreferences(LauncherActivity.shared_pref, Context.MODE_PRIVATE).getString(LauncherActivity.LanguageIso, Locale.getDefault().getLanguage()));
                }
                if(LauncherActivity.Fields.isEmpty()){
                    LauncherActivity.akhysaiViewModel.getAllFields(context.getSharedPreferences(LauncherActivity.shared_pref, Context.MODE_PRIVATE).getString(LauncherActivity.LanguageIso, Locale.getDefault().getLanguage()));
                }
                if(LauncherActivity.BlogCategories.isEmpty()){
                    LauncherActivity.akhysaiViewModel.getAllBlogCategories(context.getSharedPreferences(LauncherActivity.shared_pref, Context.MODE_PRIVATE).getString(LauncherActivity.LanguageIso, Locale.getDefault().getLanguage()));
                }
                if(LauncherActivity.DirectoryCategories.isEmpty()){
                    LauncherActivity.akhysaiViewModel.getAllDirectoryCategories(context.getSharedPreferences(LauncherActivity.shared_pref, Context.MODE_PRIVATE).getString(LauncherActivity.LanguageIso, Locale.getDefault().getLanguage()));
                }
                if(LauncherActivity.Qualifications.isEmpty()){
                    LauncherActivity.akhysaiViewModel.getAllQualifications(context.getSharedPreferences(LauncherActivity.shared_pref, Context.MODE_PRIVATE).getString(LauncherActivity.LanguageIso, Locale.getDefault().getLanguage()));
                }
            }
        }
    }
}
