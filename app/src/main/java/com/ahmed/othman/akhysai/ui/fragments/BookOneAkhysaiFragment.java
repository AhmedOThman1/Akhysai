package com.ahmed.othman.akhysai.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.Akhysai;
import com.ahmed.othman.akhysai.pojo.AvailableDate;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.logged_in;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;


public class BookOneAkhysaiFragment extends Fragment {


    public BookOneAkhysaiFragment() {
        // Required empty public constructor
    }

    Context context;
    Akhysai currentAkhysai = new Akhysai();
    NestedScrollView nested;
    boolean check_ScrollingUp = false;

    ChipGroup saturday_chips,
            sunday_chips,
            monday_chips,
            tuesday_chips,
            wednesday_chips,
            thursday_chips,
            friday_chips;
    View view;

    Map<Integer,AvailableDate> chipsMap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_book_one_akhysai, container, false);

        toolbar.setVisibility(View.VISIBLE);
        context = getContext();

        RoundedImageView akhysai_image = view.findViewById(R.id.akhysai_image);
        TextView akhysai_name = view.findViewById(R.id.akhysai_name),
                akhysai_years_of_experience = view.findViewById(R.id.akhysai_years_of_experience),
                visitors_rate_num = view.findViewById(R.id.visitors_rate_num),
                akhysai_price = view.findViewById(R.id.akhysai_price);
        RatingBar akhysai_rating = view.findViewById(R.id.akhysai_rating);
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
                ChipsInit(getAvailableDatesByAkhysaiID(currentAkhysai.getAkhysai_id()));
            }
        }

        Glide.with(context)
                .load(currentAkhysai.getPhoto())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.doctor_img2)
                .into(akhysai_image);

        akhysai_name.setText(currentAkhysai.getName());

        String temp = context.getResources().getString(R.string.years_of_experience2) + ": " + currentAkhysai.getExperience_years() + context.getResources().getString(R.string.years);
        akhysai_years_of_experience.setText(temp);

        akhysai_rating.setRating(currentAkhysai.getRate());
        akhysai_rating.setIsIndicator(true);

        temp = context.getResources().getString(R.string.this_rate_from) + currentAkhysai.getVisitor_num() + context.getResources().getString(R.string.visitor);
        visitors_rate_num.setText(temp);

        temp = context.getResources().getString(R.string.session_price) + currentAkhysai.getPrice() + context.getResources().getString(R.string.egp);
        akhysai_price.setText(temp);


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

    private ArrayList<AvailableDate> getAvailableDatesByAkhysaiID(String akhysai_id) {
        ArrayList<AvailableDate> tempAvailableDates = new ArrayList<>();

        Calendar c = Calendar.getInstance(),c2 = Calendar.getInstance();
        c2.add(Calendar.HOUR,1);
        c2.add(Calendar.MINUTE,30);
        tempAvailableDates.add(new AvailableDate(c.getTimeInMillis(),c2.getTimeInMillis()));
        c.add(Calendar.HOUR,4);
        c2.add(Calendar.HOUR,4);
        c2.add(Calendar.MINUTE,30);
        tempAvailableDates.add(new AvailableDate(c.getTimeInMillis(),c2.getTimeInMillis()));

        c.add(Calendar.DAY_OF_MONTH,1);
        c2.add(Calendar.DAY_OF_MONTH,1);
        c.add(Calendar.HOUR,1);
        c2.add(Calendar.HOUR,1);
        tempAvailableDates.add(new AvailableDate(c.getTimeInMillis(),c2.getTimeInMillis()));

        c.add(Calendar.DAY_OF_MONTH,1);
        c2.add(Calendar.DAY_OF_MONTH,1);
        c.add(Calendar.HOUR,1);
        c2.add(Calendar.HOUR,1);
        tempAvailableDates.add(new AvailableDate(c.getTimeInMillis(),c2.getTimeInMillis()));
        c.add(Calendar.HOUR,1);
        c2.add(Calendar.HOUR,1);
        tempAvailableDates.add(new AvailableDate(c.getTimeInMillis(),c2.getTimeInMillis()));
        c.add(Calendar.HOUR,1);
        c2.add(Calendar.HOUR,1);
        tempAvailableDates.add(new AvailableDate(c.getTimeInMillis(),c2.getTimeInMillis()));

        c.add(Calendar.DAY_OF_MONTH,1);
        c2.add(Calendar.DAY_OF_MONTH,1);
        c.add(Calendar.HOUR,2);
        c2.add(Calendar.HOUR,2);
        tempAvailableDates.add(new AvailableDate(c.getTimeInMillis(),c2.getTimeInMillis()));
        c.add(Calendar.HOUR,1);
        c2.add(Calendar.HOUR,2);
        tempAvailableDates.add(new AvailableDate(c.getTimeInMillis(),c2.getTimeInMillis()));

        return tempAvailableDates;
    }

    private void ChipsInit(ArrayList<AvailableDate> availableDates) {
        for (AvailableDate date : availableDates) {
            Calendar start_time = Calendar.getInstance(), end_time = Calendar.getInstance();
            start_time.setTimeInMillis(date.getStart_time());
            end_time.setTimeInMillis(date.getEnd_time());

            String title = (start_time.get(Calendar.HOUR)==0?"12":start_time.get(Calendar.HOUR))
                    + ":" + (start_time.get(Calendar.MINUTE) < 10 ? "0" + start_time.get(Calendar.MINUTE) : start_time.get(Calendar.MINUTE))
                    + (start_time.get(Calendar.AM_PM) == Calendar.AM ? " AM" : " PM")
                    + " - "
                    + (end_time.get(Calendar.HOUR)==0?"12":end_time.get(Calendar.HOUR))
                    + ":" + (end_time.get(Calendar.MINUTE) < 10 ? "0" + end_time.get(Calendar.MINUTE) : end_time.get(Calendar.MINUTE))
                    + (end_time.get(Calendar.AM_PM) == Calendar.AM ? " AM" : " PM");

            switch (start_time.get(Calendar.DAY_OF_WEEK)) {

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
                day = getContext().getResources().getString(R.string.saturday);
                break;
            case 1:
                day = getContext().getResources().getString(R.string.sunday);
                break;
            case 2:
                day = getContext().getResources().getString(R.string.monday);
                break;
            case 3:
                day = getContext().getResources().getString(R.string.tuesday);
                break;
            case 4:
                day = getContext().getResources().getString(R.string.wednesday);
                break;
            case 5:
                day = getContext().getResources().getString(R.string.thursday);
                break;
            case 6:
                day = getContext().getResources().getString(R.string.friday);
                break;
            default:
                day = "DayName";
                break;
        }

        if (requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).getBoolean(logged_in, false)) {
            Bundle bundle = new Bundle();
            bundle.putString("name", currentAkhysai.getName());
            bundle.putString("day", day);
            bundle.putString("date",new Gson().toJson(chipsMap.get(checkedId)));
            Navigation.findNavController(view).navigate(R.id.action_bookOneAkhysaiFragment_to_confirmDateFragment, bundle);

        } else
            Navigation.findNavController(view).navigate(R.id.action_bookOneAkhysaiFragment_to_loginFragment);
    }

    private void addChipToChipGroup(AvailableDate date, String title, ChipGroup chipGroup) {
        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.one_date_chip_item, chipGroup, false);
        chip.setText(title);
        chipGroup.addView(chip);
        chipsMap.put(chip.getId(),date);
    }
}