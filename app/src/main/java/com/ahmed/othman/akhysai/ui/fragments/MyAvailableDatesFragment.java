package com.ahmed.othman.akhysai.ui.fragments;

import android.app.TimePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.AvailableDate;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;
import static java.lang.Math.abs;

public class MyAvailableDatesFragment extends Fragment {

    public MyAvailableDatesFragment() {
        // Required empty public constructor
    }

    Spinner days_spinner;
    TextInputLayout start_time_text, end_time_text;
    ChipGroup saturday_chips,
            sunday_chips,
            monday_chips,
            tuesday_chips,
            wednesday_chips,
            thursday_chips,
            friday_chips;
    View view;

    Map<Integer, AvailableDate> chipsMap = new HashMap<>();

    Calendar start_calendar = Calendar.getInstance(),
            end_calendar = Calendar.getInstance();

    ArrayList<AvailableDate> availableDates = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_available_dates, container, false);

        toolbar.setVisibility(View.VISIBLE);
        days_spinner = view.findViewById(R.id.days_spinner);
        start_time_text = view.findViewById(R.id.start_time_text);
        end_time_text = view.findViewById(R.id.end_time_text);

        saturday_chips = view.findViewById(R.id.saturday_chips);
        sunday_chips = view.findViewById(R.id.sunday_chips);
        monday_chips = view.findViewById(R.id.monday_chips);
        tuesday_chips = view.findViewById(R.id.tuesday_chips);
        wednesday_chips = view.findViewById(R.id.wednesday_chips);
        thursday_chips = view.findViewById(R.id.thursday_chips);
        friday_chips = view.findViewById(R.id.friday_chips);

        availableDates = getAvailableDatesByAkhysaiID("TODO");

        ArrayAdapter<String> days = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, requireContext().getResources().getStringArray(R.array.days));
        days.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        days_spinner.setAdapter(days);
        days_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                start_calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
                start_calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
                start_calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                end_calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
                end_calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
                end_calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

                while (start_calendar.get(Calendar.DAY_OF_WEEK) != getDayOfWeek(position)) {
                    start_calendar.add(Calendar.DAY_OF_MONTH, 1);
                    end_calendar.add(Calendar.DAY_OF_MONTH, 1);
                    Log.w("++",""+start_calendar.get(Calendar.DAY_OF_MONTH));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        view.findViewById(R.id.pick_start_time).setOnClickListener(v -> choice_start_time());

        view.findViewById(R.id.pick_end_time).setOnClickListener(v -> choice_end_time());


        view.findViewById(R.id.add_available_date).setOnClickListener(v -> {

            if (start_calendar.get(Calendar.AM_PM) == Calendar.PM && end_calendar.get(Calendar.AM_PM) == Calendar.AM)
                end_calendar.add(Calendar.DAY_OF_MONTH, 1);


            if (days_spinner.getSelectedItemPosition() == 0) {
                days_spinner.setBackgroundResource(R.drawable.background_spinner_error);
                Toast.makeText(requireContext(), "Choose day first", Toast.LENGTH_SHORT).show();
            } else if (start_time_text.getEditText().getText().toString().trim().isEmpty()) {
                days_spinner.setBackgroundResource(R.drawable.background_spinner);
                start_time_text.setError("Can't be empty");
                choice_start_time();
            } else if (end_time_text.getEditText().getText().toString().trim().isEmpty()) {
                days_spinner.setBackgroundResource(R.drawable.background_spinner);
                start_time_text.setError(null);
                end_time_text.setError("Can't be empty");
                choice_end_time();
            } else if (end_calendar.before(start_calendar)) {
                days_spinner.setBackgroundResource(R.drawable.background_spinner);
                start_time_text.setError(null);
                end_time_text.setError(null);
                Toast.makeText(requireContext(), "End time can't be before Start time, change one of them", Toast.LENGTH_SHORT).show();
            } else {
                days_spinner.setBackgroundResource(R.drawable.background_spinner);
                start_time_text.setError(null);
                end_time_text.setError(null);
                AvailableDate newAvailableDate = new AvailableDate(start_calendar.getTimeInMillis(), end_calendar.getTimeInMillis());
                availableDates.add(newAvailableDate);
                updateAvailableDatesByAkhysaiId(availableDates);
                days_spinner.setSelection(0);
                start_time_text.getEditText().setText("");
                end_time_text.getEditText().setText("");
                start_calendar = Calendar.getInstance();
                end_calendar = Calendar.getInstance();
            }
        });

        ChipsInit(availableDates);


        return view;
    }

    private int getDayOfWeek(int index) {
        switch (index) {
            case 1:
                return Calendar.SATURDAY;
            case 2:
                return Calendar.SUNDAY;
            case 3:
                return Calendar.MONDAY;
            case 4:
                return Calendar.TUESDAY;
            case 5:
                return Calendar.WEDNESDAY;
            case 6:
                return Calendar.THURSDAY;
            case 7:
                return Calendar.FRIDAY;
            default:
                return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        }
    }


    private void updateAvailableDatesByAkhysaiId(ArrayList<AvailableDate> myNewAvailableDates) {
        ChipsInit(myNewAvailableDates);
        Toast.makeText(requireContext(), "Dates updated successfully", Toast.LENGTH_SHORT).show();
    }

    private ArrayList<AvailableDate> getAvailableDatesByAkhysaiID(String akhysai_id) {
        ArrayList<AvailableDate> tempAvailableDates = new ArrayList<>();

        Calendar c = Calendar.getInstance(), c2 = Calendar.getInstance();
        c2.add(Calendar.HOUR, 1);
        c2.add(Calendar.MINUTE, 30);
        tempAvailableDates.add(new AvailableDate(c.getTimeInMillis(), c2.getTimeInMillis()));
        c.add(Calendar.HOUR, 4);
        c2.add(Calendar.HOUR, 4);
        c2.add(Calendar.MINUTE, 30);
        tempAvailableDates.add(new AvailableDate(c.getTimeInMillis(), c2.getTimeInMillis()));

        c.add(Calendar.DAY_OF_MONTH, 1);
        c2.add(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.HOUR, 1);
        c2.add(Calendar.HOUR, 1);
        tempAvailableDates.add(new AvailableDate(c.getTimeInMillis(), c2.getTimeInMillis()));

        c.add(Calendar.DAY_OF_MONTH, 1);
        c2.add(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.HOUR, 1);
        c2.add(Calendar.HOUR, 1);
        tempAvailableDates.add(new AvailableDate(c.getTimeInMillis(), c2.getTimeInMillis()));
        c.add(Calendar.HOUR, 1);
        c2.add(Calendar.HOUR, 1);
        tempAvailableDates.add(new AvailableDate(c.getTimeInMillis(), c2.getTimeInMillis()));
        c.add(Calendar.HOUR, 1);
        c2.add(Calendar.HOUR, 1);
        tempAvailableDates.add(new AvailableDate(c.getTimeInMillis(), c2.getTimeInMillis()));

        c.add(Calendar.DAY_OF_MONTH, 1);
        c2.add(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.HOUR, 2);
        c2.add(Calendar.HOUR, 2);
        tempAvailableDates.add(new AvailableDate(c.getTimeInMillis(), c2.getTimeInMillis()));
        c.add(Calendar.HOUR, 1);
        c2.add(Calendar.HOUR, 2);
        tempAvailableDates.add(new AvailableDate(c.getTimeInMillis(), c2.getTimeInMillis()));

        return tempAvailableDates;
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
            Calendar start_time = Calendar.getInstance(), end_time = Calendar.getInstance();
            start_time.setTimeInMillis(date.getStart_time());
            end_time.setTimeInMillis(date.getEnd_time());

            String title = (start_time.get(Calendar.HOUR) == 0 ? "12" : start_time.get(Calendar.HOUR))
                    + ":" + (start_time.get(Calendar.MINUTE) < 10 ? "0" + start_time.get(Calendar.MINUTE) : start_time.get(Calendar.MINUTE))
                    + (start_time.get(Calendar.AM_PM) == Calendar.AM ? " AM" : " PM")
                    + " - "
                    + (end_time.get(Calendar.HOUR) == 0 ? "12" : end_time.get(Calendar.HOUR))
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

    boolean undoClick = false;

    private void addChipToChipGroup(AvailableDate date, String title, ChipGroup chipGroup) {
        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.one_date_chip_item, chipGroup, false);
        chip.setText(title);
        chip.setCloseIconTint(ColorStateList.valueOf(requireContext().getResources().getColor(R.color.colorPrimary)));
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> {
            Snackbar.make(v, "this date has been deleted successfully", Snackbar.LENGTH_LONG)
                    .setAction("Undo", v1 -> {
                        undoClick = true;
                    })
                    .show();

            new Handler().postDelayed(() -> {
                if (!undoClick) {
                    availableDates.remove(chipsMap.get(chip.getId()));
                    chipsMap.remove(chip.getId());
                    chipGroup.removeView(chip);
                    updateAvailableDatesByAkhysaiId(availableDates);
                }
                undoClick = false;
            }, 3000);
        });
        chipGroup.addView(chip);
        chipsMap.put(chip.getId(), date);
    }


    private void choice_start_time() {
        choice_time("start");
    }

    private void choice_end_time() {
        choice_time("end");
    }

    private void choice_time(final String time) {

        int Chour = time.equals("start") ? start_calendar.get(Calendar.HOUR) : end_calendar.get(Calendar.HOUR);
        int Cminute = time.equals("start") ? start_calendar.get(Calendar.MINUTE) : end_calendar.get(Calendar.MINUTE);
        if ((time.equals("start") && start_calendar.get(Calendar.AM_PM) == Calendar.PM) || (time.equals("end") && end_calendar.get(Calendar.AM_PM) == Calendar.PM))
            Chour += 12;
        TimePickerDialog pickerDialog = new TimePickerDialog(requireContext(), (view, hourOfDay, minute) -> {
            if (time.equals("start")) {
                start_calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                start_calendar.set(Calendar.MINUTE, minute);
                String temp_start_time = (start_calendar.get(Calendar.HOUR) == 0 ? "12" : start_calendar.get(Calendar.HOUR))
                        + ":"
                        + (start_calendar.get(Calendar.MINUTE) < 10 ? "0" + start_calendar.get(Calendar.MINUTE) : start_calendar.get(Calendar.MINUTE))
                        + (start_calendar.get(Calendar.AM_PM) == Calendar.AM ?
                        requireContext().getResources().getString(R.string.am)
                        : requireContext().getResources().getString(R.string.pm));

                start_time_text.getEditText().setText(temp_start_time);

            } else if (time.equals("end")) {
                end_calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                end_calendar.set(Calendar.MINUTE, minute);
                String temp_end_time = (end_calendar.get(Calendar.HOUR) == 0 ? "12" : end_calendar.get(Calendar.HOUR))
                        + ":"
                        + (end_calendar.get(Calendar.MINUTE) < 10 ? "0" + end_calendar.get(Calendar.MINUTE) : end_calendar.get(Calendar.MINUTE))
                        + (end_calendar.get(Calendar.AM_PM) == Calendar.AM ?
                        requireContext().getResources().getString(R.string.am)
                        : requireContext().getResources().getString(R.string.pm));

                end_time_text.getEditText().setText(temp_end_time);
            }

            // TODO delete this test
            String day;
            switch (start_calendar.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.SATURDAY:
                    day = requireContext().getResources().getString(R.string.saturday);
                    break;
                case Calendar.SUNDAY:
                    day = requireContext().getResources().getString(R.string.sunday);
                    break;
                case Calendar.MONDAY:
                    day = requireContext().getResources().getString(R.string.monday);
                    break;
                case Calendar.TUESDAY:
                    day = requireContext().getResources().getString(R.string.tuesday);
                    break;
                case Calendar.WEDNESDAY:
                    day = requireContext().getResources().getString(R.string.wednesday);
                    break;
                case Calendar.THURSDAY:
                    day = requireContext().getResources().getString(R.string.thursday);
                    break;
                case Calendar.FRIDAY:
                    day = requireContext().getResources().getString(R.string.friday);
                    break;
                default:
                    day = "DayName";
                    break;
            }
            Log.w("START_TIME_CHECK2", start_calendar.get(Calendar.YEAR) + "/" + (start_calendar.get(Calendar.MONTH) + 1) + "/" + start_calendar.get(Calendar.DAY_OF_MONTH) + " " + start_calendar.get(Calendar.HOUR) + ":" + start_calendar.get(Calendar.MINUTE) + " " + (start_calendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM") + ", " + day);
            switch (end_calendar.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.SATURDAY:
                    day = requireContext().getResources().getString(R.string.saturday);
                    break;
                case Calendar.SUNDAY:
                    day = requireContext().getResources().getString(R.string.sunday);
                    break;
                case Calendar.MONDAY:
                    day = requireContext().getResources().getString(R.string.monday);
                    break;
                case Calendar.TUESDAY:
                    day = requireContext().getResources().getString(R.string.tuesday);
                    break;
                case Calendar.WEDNESDAY:
                    day = requireContext().getResources().getString(R.string.wednesday);
                    break;
                case Calendar.THURSDAY:
                    day = requireContext().getResources().getString(R.string.thursday);
                    break;
                case Calendar.FRIDAY:
                    day = requireContext().getResources().getString(R.string.friday);
                    break;
                default:
                    day = "DayName";
                    break;
            }
            Log.w("END_TIME_CHECK2", end_calendar.get(Calendar.YEAR) + "/" + (end_calendar.get(Calendar.MONTH) + 1) + "/" + end_calendar.get(Calendar.DAY_OF_MONTH) + " " + end_calendar.get(Calendar.HOUR) + ":" + end_calendar.get(Calendar.MINUTE) + " " + (end_calendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM") + ", " + day);
//End OF TODO

        }, Chour, Cminute, false);
        pickerDialog.show();
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
//                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.homeFragment, null, navOptions);

                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack(R.id.homeFragment, false);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }
}