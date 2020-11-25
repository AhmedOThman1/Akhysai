package com.ahmed.othman.akhysai.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.adapter.BookingRequestsAdapter;
import com.ahmed.othman.akhysai.pojo.BookingRequest;

import java.util.ArrayList;
import java.util.Calendar;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;

public class BookingRequestsFragment extends Fragment {

    public BookingRequestsFragment() {
        // Required empty public constructor
    }

    ArrayList<BookingRequest> bookingRequests = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking_requests, container, false);

        toolbar.setVisibility(View.VISIBLE);
        bookingRequests = getAllBookingRequestsByAkhysaiID();
        RecyclerView booking_requests_recycler = view.findViewById(R.id.booking_requests_recycler);

        BookingRequestsAdapter bookingRequestsAdapter = new BookingRequestsAdapter(getContext());
        bookingRequestsAdapter.setModels(bookingRequests);
        booking_requests_recycler.setAdapter(bookingRequestsAdapter);
        booking_requests_recycler.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    private ArrayList<BookingRequest> getAllBookingRequestsByAkhysaiID() {

        Calendar c = Calendar.getInstance(), c2 = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c2.add(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.HOUR, 2);
        c2.add(Calendar.HOUR, 3);

        ArrayList<BookingRequest> tempBookingRequests = new ArrayList<>();
        tempBookingRequests.add(new BookingRequest("Ahmed Othman",
                "456612",
                "Ahmed Ali st.",
                "please hurry up!",
                "01097654241",
                true,
                System.currentTimeMillis(),
                c.getTimeInMillis(),
                c2.getTimeInMillis()));

        c.add(Calendar.DAY_OF_MONTH, 1);
        c2.add(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.HOUR, 4);
        c2.add(Calendar.HOUR, 5);
        c2.add(Calendar.MINUTE, 30);

        tempBookingRequests.add(new BookingRequest("Ziad Sakr",
                "456612",
                "Ahmed Ali st.",
                "please hurry up!",
                "01097654241",
                true,
                System.currentTimeMillis(),
                c.getTimeInMillis(),
                c2.getTimeInMillis()));

        c.add(Calendar.DAY_OF_MONTH, 1);
        c2.add(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.HOUR, 4);
        c.add(Calendar.MINUTE, 30);
        c2.add(Calendar.HOUR, 6);
        c2.add(Calendar.MINUTE, 30);

        tempBookingRequests.add(new BookingRequest("Rizq",
                "456612",
                "Ahmed Ali st.",
                "please hurry up!",
                "01097654241",
                true,
                System.currentTimeMillis(),
                c.getTimeInMillis(),
                c2.getTimeInMillis()));

        return tempBookingRequests;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment, true)
                        .setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left)
                        .setPopEnterAnim(R.anim.slide_in_left)
                        .setPopExitAnim(R.anim.slide_out_right)
                        .build();
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.homeFragment, null, navOptions);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}