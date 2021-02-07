package com.ahmed.othman.akhysai.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.network.ApiClient;
import com.ahmed.othman.akhysai.pojo.Akhysai;
import com.ahmed.othman.akhysai.pojo.AvailableDate;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;
import com.willy.ratingbar.ScaleRatingBar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.PATIENT;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.logged_in;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.toolbar;


public class BookOneAkhysaiFragment extends Fragment {


    public BookOneAkhysaiFragment() {
        // Required empty public constructor
    }

    Context context;
    Akhysai currentAkhysai = new Akhysai();
    ScrollView nested;
//    boolean check_ScrollingUp = false;

    ChipGroup saturday_chips,
            sunday_chips,
            monday_chips,
            tuesday_chips,
            wednesday_chips,
            thursday_chips,
            friday_chips;
    View view;

    ArrayList<AvailableDate> availableDates = new ArrayList<>();

    Map<Integer, AvailableDate> chipsMap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_book_one_akhysai, container, false);

        toolbar.setVisibility(View.VISIBLE);
        context = requireContext();

        CircleImageView akhysai_image = view.findViewById(R.id.akhysai_image);
        TextView akhysai_name = view.findViewById(R.id.akhysai_name),
                akhysai_years_of_experience = view.findViewById(R.id.akhysai_years_of_experience),
                visitors_rate_num = view.findViewById(R.id.visitors_rate_num),
                akhysai_price = view.findViewById(R.id.akhysai_price);
//        ScaleRatingBar akhysai_rating = view.findViewById(R.id.akhysai_rating);
        nested = view.findViewById(R.id.nested_scroll);

        saturday_chips = view.findViewById(R.id.saturday_chips);
        sunday_chips = view.findViewById(R.id.sunday_chips);
        monday_chips = view.findViewById(R.id.monday_chips);
        tuesday_chips = view.findViewById(R.id.tuesday_chips);
        wednesday_chips = view.findViewById(R.id.wednesday_chips);
        thursday_chips = view.findViewById(R.id.thursday_chips);
        friday_chips = view.findViewById(R.id.friday_chips);

        Bundle args = getArguments();
        if (args != null) {
            String json = args.getString("akhysai", "");
            if (!json.trim().isEmpty()) {
                currentAkhysai = new Gson().fromJson(json, Akhysai.class);
                getAvailableDatesByAkhysaiToken(currentAkhysai.getApiToken());
            }
        }

        Glide.with(context)
                .load(LauncherActivity.ImagesLink+currentAkhysai.getProfile_picture())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.doctor_img2)
                .into(akhysai_image);

        akhysai_name.setText(currentAkhysai.getName());

        String temp = context.getResources().getString(R.string.years_of_experience2) + ": " + currentAkhysai.getExperienceYears() + context.getResources().getString(R.string.years);
        akhysai_years_of_experience.setText(temp);

//        akhysai_rating.setRating(currentAkhysai.getRate());
//        akhysai_rating.setIsIndicator(true);

//        temp = context.getResources().getString(R.string.this_rate_from) + currentAkhysai.getVisitor_num() + context.getResources().getString(R.string.visitor);
//        visitors_rate_num.setText(temp);
//
//        temp = context.getResources().getString(R.string.session_price) + currentAkhysai.getPrice() + context.getResources().getString(R.string.egp);
//        akhysai_price.setText(temp);


        saturday_chips.setOnCheckedChangeListener((group, checkedId) -> confirmDate(0, group, checkedId));
        sunday_chips.setOnCheckedChangeListener((group, checkedId) -> confirmDate(1, group, checkedId));
        monday_chips.setOnCheckedChangeListener((group, checkedId) -> confirmDate(2, group, checkedId));
        tuesday_chips.setOnCheckedChangeListener((group, checkedId) -> confirmDate(3, group, checkedId));
        wednesday_chips.setOnCheckedChangeListener((group, checkedId) -> confirmDate(4, group, checkedId));
        thursday_chips.setOnCheckedChangeListener((group, checkedId) -> confirmDate(5, group, checkedId));
        friday_chips.setOnCheckedChangeListener((group, checkedId) -> confirmDate(6, group, checkedId));

//        nested.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
//            if (scrollY > oldScrollY + 2) {
//                // User scrolls down
//                if (!check_ScrollingUp) {
//                    Log.w("UpDown", "DDDDDDDD");
//                    toolbar.startAnimation(AnimationUtils
//                            .loadAnimation(requireContext(), R.anim.trans_upwards));
//                    check_ScrollingUp = true;
//                }
//
//            } else if (scrollY < oldScrollY - 2) {
//
//                // Scrolling up
//                if (check_ScrollingUp) {
//                    Log.w("UpDown", "UUUUUUUUUU");
//                    toolbar.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.trans_downwards));
//                    check_ScrollingUp = false;
//                }
//            }
//
//        });

        return view;
    }

    private void getAvailableDatesByAkhysaiToken(String akhysaiToken) {
        ApiClient.getINSTANCE().getAkhysaiTimetable("Bearer " + akhysaiToken).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body().get("status").getAsString().equals("success")) {
                    availableDates.clear();
                    for (String key : response.body().get("data").getAsJsonObject().keySet()) {
                        Type datesListType = new TypeToken<ArrayList<AvailableDate>>() {
                        }.getType();
                        ArrayList<AvailableDate> dates = new Gson().fromJson(response.body().get("data").getAsJsonObject().get(key).getAsJsonArray().toString(),
                                datesListType);
                        availableDates.addAll(dates);
                    }
                    Log.w("SIZE", "" + availableDates.size());
                    ChipsInit(availableDates);
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
    }

    private void ChipsInit(ArrayList<AvailableDate> availableDates) {

        saturday_chips.removeAllViews();
        sunday_chips.removeAllViews();
        monday_chips.removeAllViews();
        tuesday_chips.removeAllViews();
        wednesday_chips.removeAllViews();
        thursday_chips.removeAllViews();
        friday_chips.removeAllViews();


        for (AvailableDate date : availableDates) {

            int start_hour = Integer.parseInt(date.getStartTime().substring(0,2));
            if(start_hour>12) start_hour-=12;
            String start = String.valueOf(start_hour)+date.getStartTime().substring(2,5);

            int end_hour = Integer.parseInt(date.getEndTime().substring(0,2));
            if(end_hour>12) end_hour-=12;
            String end = String.valueOf(end_hour)+date.getEndTime().substring(2,5);

            String title = start
                    + ((date.getStartTime().charAt(0) == '0' || (date.getStartTime().charAt(0) == '1' && date.getStartTime().charAt(1) < '2')) ? "AM" : "PM")
                    + " - "
                    + end
                    + ((date.getEndTime().charAt(0) == '0' || (date.getEndTime().charAt(0) == '1' && date.getEndTime().charAt(1) < '2')) ? "AM" : "PM");


            switch (Integer.parseInt(date.getDay())) {

                case Calendar.SATURDAY:
                    addChipToChipGroup(date, title, saturday_chips);
                    break;

                case Calendar.SUNDAY:
                    addChipToChipGroup(date, title, sunday_chips);
                    break;

                case Calendar.MONDAY:
                    addChipToChipGroup(date, title, monday_chips);
                    break;

                case Calendar.TUESDAY:
                    addChipToChipGroup(date, title, tuesday_chips);
                    break;

                case Calendar.WEDNESDAY:
                    addChipToChipGroup(date, title, wednesday_chips);
                    break;

                case Calendar.THURSDAY:
                    addChipToChipGroup(date, title, thursday_chips);
                    break;

                case Calendar.FRIDAY:
                    addChipToChipGroup(date, title, friday_chips);
                    break;
            }
        }
        view.findViewById(R.id.saturday_not_available).setVisibility(saturday_chips.getChildCount() == 0 ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.sunday_not_available).setVisibility(sunday_chips.getChildCount() == 0 ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.monday_not_available).setVisibility(monday_chips.getChildCount() == 0 ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.tuesday_not_available).setVisibility(tuesday_chips.getChildCount() == 0 ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.wednesday_not_available).setVisibility(wednesday_chips.getChildCount() == 0 ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.thursday_not_available).setVisibility(thursday_chips.getChildCount() == 0 ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.friday_not_available).setVisibility(friday_chips.getChildCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private void confirmDate(int index, ChipGroup group, int checkedId) {
        String day;
        switch (index) {
            case 0:
                day = requireContext().getResources().getString(R.string.saturday);
                break;
            case 1:
                day = requireContext().getResources().getString(R.string.sunday);
                break;
            case 2:
                day = requireContext().getResources().getString(R.string.monday);
                break;
            case 3:
                day = requireContext().getResources().getString(R.string.tuesday);
                break;
            case 4:
                day = requireContext().getResources().getString(R.string.wednesday);
                break;
            case 5:
                day = requireContext().getResources().getString(R.string.thursday);
                break;
            case 6:
                day = requireContext().getResources().getString(R.string.friday);
                break;
            default:
                day = "DayName";
                break;
        }

        if (requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).getBoolean(logged_in, false)) {
            Bundle bundle = new Bundle();
            bundle.putString("name", currentAkhysai.getName());
            bundle.putString("day", day);
            bundle.putString("date", new Gson().toJson(chipsMap.get(checkedId)));
            Navigation.findNavController(view).navigate(R.id.action_bookOneAkhysaiFragment_to_confirmDateFragment, bundle);

        } else {
            Bundle bundle = new Bundle();
            bundle.putString("goTo", "BookOneAkhysaiFragment");
            bundle.putString("akhysai",new Gson().toJson(currentAkhysai));
            bundle.putString("Type", PATIENT);
            Navigation.findNavController(view).navigate(R.id.action_bookOneAkhysaiFragment_to_loginFragment, bundle);
        }
    }

    private void addChipToChipGroup(AvailableDate date, String title, ChipGroup chipGroup) {
        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.one_date_choice_chip_item, chipGroup, false);
        chip.setText(title);
        chipGroup.addView(chip);
        chipsMap.put(chip.getId(), date);
    }
}