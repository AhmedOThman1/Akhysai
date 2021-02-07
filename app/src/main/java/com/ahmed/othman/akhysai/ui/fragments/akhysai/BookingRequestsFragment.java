package com.ahmed.othman.akhysai.ui.fragments.akhysai;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.adapter.BookingRequestsAdapter;
import com.ahmed.othman.akhysai.network.ApiClient;
import com.ahmed.othman.akhysai.pojo.BookingRequest;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.LanguageIso;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.toolbar;

public class BookingRequestsFragment extends Fragment {

    public BookingRequestsFragment() {
        // Required empty public constructor
    }

    ArrayList<BookingRequest> bookingRequests = new ArrayList<>();
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_booking_requests, container, false);

        toolbar.setVisibility(View.VISIBLE);
        getAllBookingRequestsByAkhysaiID();
        RecyclerView booking_requests_recycler = view.findViewById(R.id.booking_requests_recycler);

        BookingRequestsAdapter bookingRequestsAdapter = new BookingRequestsAdapter(getContext());
        bookingRequestsAdapter.setModels(bookingRequests);
        booking_requests_recycler.setAdapter(bookingRequestsAdapter);
        booking_requests_recycler.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    private void getAllBookingRequestsByAkhysaiID() {

        ApiClient.getINSTANCE().getAkhysaiBookings("Bearer "+ LauncherActivity.currentAkhysai.getApiToken(),
                requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage())).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                //Stop shimmer
                if (response.isSuccessful()) {

                    if(response.body().get("status").getAsString().equalsIgnoreCase("success")){
                        //show recycler
//                        response.body().get("data").getAsJsonArray()
                    }

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (t.getMessage().contains("Unable to resolve host"))
                    Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                            .setAction(R.string.go_to_setting, v -> requireContext().startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)))
                            .show();
            }
        });
//        Calendar c = Calendar.getInstance(), c2 = Calendar.getInstance();
//        c.add(Calendar.DAY_OF_MONTH, 1);
//        c2.add(Calendar.DAY_OF_MONTH, 1);
//        c.add(Calendar.HOUR, 2);
//        c2.add(Calendar.HOUR, 3);
//
//        ArrayList<BookingRequest> tempBookingRequests = new ArrayList<>();
//        tempBookingRequests.add(new BookingRequest("Ahmed Othman",
//                "456612",
//                "Ahmed Ali st.",
//                "please hurry up!",
//                "01097654241",
//                true,
//                System.currentTimeMillis(),
//                c.getTimeInMillis(),
//                c2.getTimeInMillis()));
//
//        c.add(Calendar.DAY_OF_MONTH, 1);
//        c2.add(Calendar.DAY_OF_MONTH, 1);
//        c.add(Calendar.HOUR, 4);
//        c2.add(Calendar.HOUR, 5);
//        c2.add(Calendar.MINUTE, 30);
//
//        tempBookingRequests.add(new BookingRequest("Ziad Sakr",
//                "456612",
//                "Ahmed Ali st.",
//                "please hurry up!",
//                "01097654241",
//                true,
//                System.currentTimeMillis(),
//                c.getTimeInMillis(),
//                c2.getTimeInMillis()));
//
//        c.add(Calendar.DAY_OF_MONTH, 1);
//        c2.add(Calendar.DAY_OF_MONTH, 1);
//        c.add(Calendar.HOUR, 4);
//        c.add(Calendar.MINUTE, 30);
//        c2.add(Calendar.HOUR, 6);
//        c2.add(Calendar.MINUTE, 30);
//
//        tempBookingRequests.add(new BookingRequest("Rizq",
//                "456612",
//                "Ahmed Ali st.",
//                "please hurry up!",
//                "01097654241",
//                true,
//                System.currentTimeMillis(),
//                c.getTimeInMillis(),
//                c2.getTimeInMillis()));
//
//        return tempBookingRequests;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

//                NavOptions navOptions = new NavOptions.Builder()
//                        .setPopUpTo(R.id.homeFragment, true)
//                        .setEnterAnim(R.anim.slide_in_right)
//                        .setExitAnim(R.anim.slide_out_left)
//                        .setPopEnterAnim(R.anim.slide_in_left)
//                        .setPopExitAnim(R.anim.slide_out_right)
//                        .build();
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack(R.id.homeFragment, false);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}