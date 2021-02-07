package com.ahmed.othman.akhysai.ui.fragments.akhysai;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.network.ApiClient;
import com.ahmed.othman.akhysai.pojo.AvailableDate;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.toolbar;
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

    ImageView
            saturday_background,
            sunday_background,
            monday_background,
            tuesday_background,
            wednesday_background,
            thursday_background,
            friday_background,
    ///
    delete_saturday,
            delete_sunday,
            delete_monday,
            delete_tuesday,
            delete_wednesday,
            delete_thursday,
            delete_friday,
    ///
    saturday_active,
            sunday_active,
            monday_active,
            tuesday_active,
            wednesday_active,
            thursday_active,
            friday_active;

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

        saturday_background = view.findViewById(R.id.saturday_background);
        sunday_background = view.findViewById(R.id.sunday_background);
        monday_background = view.findViewById(R.id.monday_background);
        tuesday_background = view.findViewById(R.id.tuesday_background);
        wednesday_background = view.findViewById(R.id.wednesday_background);
        thursday_background = view.findViewById(R.id.thursday_background);
        friday_background = view.findViewById(R.id.friday_background);

        saturday_active = view.findViewById(R.id.saturday_active);
        sunday_active = view.findViewById(R.id.sunday_active);
        monday_active = view.findViewById(R.id.monday_active);
        tuesday_active = view.findViewById(R.id.tuesday_active);
        wednesday_active = view.findViewById(R.id.wednesday_active);
        thursday_active = view.findViewById(R.id.thursday_active);
        friday_active = view.findViewById(R.id.friday_active);

        delete_saturday = view.findViewById(R.id.delete_saturday);
        delete_sunday = view.findViewById(R.id.delete_sunday);
        delete_monday = view.findViewById(R.id.delete_monday);
        delete_tuesday = view.findViewById(R.id.delete_tuesday);
        delete_wednesday = view.findViewById(R.id.delete_wednesday);
        delete_thursday = view.findViewById(R.id.delete_thursday);
        delete_friday = view.findViewById(R.id.delete_friday);

        getAvailableDatesByAkhysaiToken(LauncherActivity.currentAkhysai.getApiToken());

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
                    Log.w("++", "" + start_calendar.get(Calendar.DAY_OF_MONTH));
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
                start_time_text.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                choice_start_time();
            } else if (end_time_text.getEditText().getText().toString().trim().isEmpty()) {
                days_spinner.setBackgroundResource(R.drawable.background_spinner);
                start_time_text.setError(null);
                end_time_text.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
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

                String _days = String.valueOf(days_spinner.getSelectedItemPosition() - 1 == 0 ? 7 : days_spinner.getSelectedItemPosition() - 1),
                        _start = (start_calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + start_calendar.get(Calendar.HOUR_OF_DAY)
                                : start_calendar.get(Calendar.HOUR_OF_DAY))
                                + ":"
                                + (start_calendar.get(Calendar.MINUTE) < 10 ? "0" + start_calendar.get(Calendar.MINUTE)
                                : start_calendar.get(Calendar.MINUTE)),
                        _end = (end_calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + end_calendar.get(Calendar.HOUR_OF_DAY)
                                : end_calendar.get(Calendar.HOUR_OF_DAY))
                                + ":"
                                + (end_calendar.get(Calendar.MINUTE) < 10 ? "0" + end_calendar.get(Calendar.MINUTE)
                                : end_calendar.get(Calendar.MINUTE));
                AddNewTimeSlot(LauncherActivity.currentAkhysai.getApiToken(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), _days),
                        RequestBody.create(MediaType.parse("multipart/form-data"), _start),
                        RequestBody.create(MediaType.parse("multipart/form-data"), _end)
                );

                //way 1
//                JsonObject object = new JsonObject();
//                object.addProperty("days[]",new String[] {String.valueOf(days_spinner.getSelectedItemPosition()-1 == 0 ? 7 : days_spinner.getSelectedItemPosition()-1)});
//                object.addProperty("start_time",
//                        (start_calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + start_calendar.get(Calendar.HOUR_OF_DAY)
//                                : start_calendar.get(Calendar.HOUR_OF_DAY))
//                                + ":"
//                                + (start_calendar.get(Calendar.MINUTE) < 10 ? "0" + start_calendar.get(Calendar.MINUTE)
//                                : start_calendar.get(Calendar.MINUTE))
//                );
//                object.addProperty("end_time",
//                        (end_calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + end_calendar.get(Calendar.HOUR_OF_DAY)
//                                : end_calendar.get(Calendar.HOUR_OF_DAY))
//                                + ":"
//                                + (end_calendar.get(Calendar.MINUTE) < 10 ? "0" + end_calendar.get(Calendar.MINUTE)
//                                : end_calendar.get(Calendar.MINUTE))
//                );
//
//                AddNewTimeSlot(LauncherActivity.currentAkhysai.getApiToken(), object);
//
///// way2
//                JSONObject jsonObject = new JSONObject();
//                String[] daysArr = {String.valueOf(days_spinner.getSelectedItemPosition()-1 == 0 ? 7 : days_spinner.getSelectedItemPosition()-1)};
//
//                try {
//                    jsonObject.put("days[]", daysArr);
//                    jsonObject.put("start_time",
//                            (start_calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + start_calendar.get(Calendar.HOUR_OF_DAY)
//                                    : start_calendar.get(Calendar.HOUR_OF_DAY))
//                                    + ":"
//                                    + (start_calendar.get(Calendar.MINUTE) < 10 ? "0" + start_calendar.get(Calendar.MINUTE)
//                                    : start_calendar.get(Calendar.MINUTE))
//
//                    );
//                    jsonObject.put("end_time",
//                            (end_calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" + end_calendar.get(Calendar.HOUR_OF_DAY)
//                                    : end_calendar.get(Calendar.HOUR_OF_DAY))
//                                    + ":"
//                                    + (end_calendar.get(Calendar.MINUTE) < 10 ? "0" + end_calendar.get(Calendar.MINUTE)
//                                    : end_calendar.get(Calendar.MINUTE))
//
//                    );
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//                AddNewTimeSlot(LauncherActivity.currentAkhysai.getApiToken(), RequestBody.create(MediaType.parse("application/json"),jsonObject.toString()));


            }
        });

        delete_saturday.setOnClickListener(v -> deleteDay("7"));
        delete_sunday.setOnClickListener(v -> deleteDay("1"));
        delete_monday.setOnClickListener(v -> deleteDay("2"));
        delete_tuesday.setOnClickListener(v -> deleteDay("3"));
        delete_wednesday.setOnClickListener(v -> deleteDay("4"));
        delete_thursday.setOnClickListener(v -> deleteDay("5"));
        delete_friday.setOnClickListener(v -> deleteDay("6"));

        saturday_active.setOnClickListener(v -> toggleDay("7"));
        sunday_active.setOnClickListener(v -> toggleDay("1"));
        monday_active.setOnClickListener(v -> toggleDay("2"));
        tuesday_active.setOnClickListener(v -> toggleDay("3"));
        wednesday_active.setOnClickListener(v -> toggleDay("4"));
        thursday_active.setOnClickListener(v -> toggleDay("5"));
        friday_active.setOnClickListener(v -> toggleDay("6"));
        return view;
    }


    private void AddNewTimeSlot(String akhysaiToken, RequestBody days, RequestBody start_time, RequestBody end_time) {
        ApiClient.getINSTANCE().AddNewTimeSlots("Bearer " + akhysaiToken, days, start_time, end_time).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    if (response.body().get("status").getAsString().equalsIgnoreCase("success")
                            && response.body().get("data").getAsString().equalsIgnoreCase("added")) {
                        Toast.makeText(requireContext(), "Time added", Toast.LENGTH_SHORT).show();
                        getAvailableDatesByAkhysaiToken(akhysaiToken);
                        days_spinner.setSelection(0);
                        start_time_text.getEditText().setText("");
                        end_time_text.getEditText().setText("");
                        start_calendar = Calendar.getInstance();
                        end_calendar = Calendar.getInstance();
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
    }

    private void getAvailableDatesByAkhysaiToken(String akhysaiToken) {
        ApiClient.getINSTANCE().getAkhysaiTimetable("Bearer " + akhysaiToken).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body().get("status").getAsString().equals("success")) {
                    availableDates.clear();
                    chipsMap.clear();

                    if(response.body().get("data") instanceof JsonObject)
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

    AlertDialog dialog;

    private void deleteDay(String dayId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View deleteDialog = requireActivity().getLayoutInflater().inflate(R.layout.delete_dialog, null);

        deleteDialog.findViewById(R.id.delete_text).setOnClickListener(v2 -> {
            ApiClient.getINSTANCE().DeleteDay("Bearer " + LauncherActivity.currentAkhysai.getApiToken(), dayId).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        if (response.body().get("status").getAsString().equalsIgnoreCase("success")) {
                            getAvailableDatesByAkhysaiToken(LauncherActivity.currentAkhysai.getApiToken());
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
            dialog.dismiss();

        });

        deleteDialog.findViewById(R.id.cancel).setOnClickListener(v2 -> {
            dialog.dismiss();
        });

        builder.setView(deleteDialog);
        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void toggleDay(String dayId) {
        ApiClient.getINSTANCE().ToggleDay("Bearer " + LauncherActivity.currentAkhysai.getApiToken(), dayId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body().get("status").getAsString().equalsIgnoreCase("success")) {
                        getAvailableDatesByAkhysaiToken(LauncherActivity.currentAkhysai.getApiToken());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
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

        saturday_background.setVisibility(saturday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        sunday_background.setVisibility(sunday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        monday_background.setVisibility(monday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        tuesday_background.setVisibility(tuesday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        wednesday_background.setVisibility(wednesday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        thursday_background.setVisibility(thursday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        friday_background.setVisibility(friday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);

        saturday_active.setVisibility(saturday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        sunday_active.setVisibility(sunday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        monday_active.setVisibility(monday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        tuesday_active.setVisibility(tuesday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        wednesday_active.setVisibility(wednesday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        thursday_active.setVisibility(thursday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        friday_active.setVisibility(friday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);

        delete_saturday.setVisibility(saturday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        delete_sunday.setVisibility(sunday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        delete_monday.setVisibility(monday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        delete_tuesday.setVisibility(tuesday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        delete_wednesday.setVisibility(wednesday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        delete_thursday.setVisibility(thursday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);
        delete_friday.setVisibility(friday_chips.getChildCount() != 0 ? View.VISIBLE : View.GONE);

    }

    boolean undoClick = false;

    private void addChipToChipGroup(AvailableDate date, String title, ChipGroup chipGroup) {
        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.one_date_action_chip_item, chipGroup, false);
        chip.setText(title);
        chip.setChipBackgroundColorResource(date.getIsActive().equals("1") ? R.color.colorPrimary : R.color.unselected_chip_color);
        chip.setTextAppearanceResource(date.getIsActive().equals("1") ? R.style.chipTextSelected : R.style.chipTextUnselected);
        chip.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), v);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.toggle_date:
                        ApiClient.getINSTANCE().ToggleTimeSlot("Bearer " + LauncherActivity.currentAkhysai.getApiToken(), chipsMap.get(chip.getId()).getId()).enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().get("status").getAsString().equalsIgnoreCase("success")) {
                                        getAvailableDatesByAkhysaiToken(LauncherActivity.currentAkhysai.getApiToken());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {

                            }
                        });
                        return true;
                    case R.id.delete_date:
                        Snackbar.make(v, requireContext().getResources().getString(R.string.deleting_time_slot), Snackbar.LENGTH_LONG)
                                .setAction(requireContext().getResources().getString(R.string.undo), v1 -> {
                                    undoClick = true;
                                })
                                .show();
                        new Handler().postDelayed(() -> {
                            if (!undoClick) {
                                //TO DO delete slot here
                                ApiClient.getINSTANCE().DeleteTimeSlot("Bearer " + LauncherActivity.currentAkhysai.getApiToken(), chipsMap.get(chip.getId()).getId()).enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body().get("status").getAsString().equalsIgnoreCase("success")) {
                                                getAvailableDatesByAkhysaiToken(LauncherActivity.currentAkhysai.getApiToken());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {

                                    }
                                });
                            }
                            undoClick = false;
                        }, 3000);

                        return true;
                    default:
                        return false;
                }
            });
            popupMenu.inflate(R.menu.date_menu);
            popupMenu.getMenu().findItem(R.id.toggle_date).setTitle(chipsMap.get(chip.getId()).getIsActive().equals("1") ? requireActivity().getResources().getString(R.string.not_available) : requireActivity().getResources().getString(R.string.available));
            popupMenu.show();
        });

//        chip.setCloseIconTint(ColorStateList.valueOf(requireContext().getResources().getColor(R.color.colorPrimary)));
//        chip.setCloseIconVisible(true);
//        chip.setOnCloseIconClickListener(v -> {
//            Snackbar.make(v, "this date has been deleted successfully", Snackbar.LENGTH_LONG)
//                    .setAction("Undo", v1 -> {
//                        undoClick = true;
//                    })
//                    .show();
//            new Handler().postDelayed(() -> {
//                if (!undoClick) {
//                    //TO DO delete slot here
//                    ApiClient.getINSTANCE().DeleteTimeSlot(LauncherActivity.currentAkhysai.getApiToken(),chipsMap.get(chip.getId()).getId()).enqueue(new Callback<JsonObject>() {
//                        @Override
//                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                            if(response.isSuccessful()){
//                                if(response.body().get("status").getAsString().equalsIgnoreCase("success")){
//                                    getAvailableDatesByAkhysaiToken(LauncherActivity.currentAkhysai.getApiToken());
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<JsonObject> call, Throwable t) {
//
//                        }
//                    });
////                    availableDates.remove(chipsMap.get(chip.getId()));
////                    chipsMap.remove(chip.getId());
////                    chipGroup.removeView(chip);
//////                    updateAvailableDatesByAkhysaiId(availableDates);
//                }
//                undoClick = false;
//            }, 3000);
//        });
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

            // TO DO delete this test
//            String day;
//            switch (start_calendar.get(Calendar.DAY_OF_WEEK)) {
//                case Calendar.SATURDAY:
//                    day = requireContext().getResources().getString(R.string.saturday);
//                    break;
//                case Calendar.SUNDAY:
//                    day = requireContext().getResources().getString(R.string.sunday);
//                    break;
//                case Calendar.MONDAY:
//                    day = requireContext().getResources().getString(R.string.monday);
//                    break;
//                case Calendar.TUESDAY:
//                    day = requireContext().getResources().getString(R.string.tuesday);
//                    break;
//                case Calendar.WEDNESDAY:
//                    day = requireContext().getResources().getString(R.string.wednesday);
//                    break;
//                case Calendar.THURSDAY:
//                    day = requireContext().getResources().getString(R.string.thursday);
//                    break;
//                case Calendar.FRIDAY:
//                    day = requireContext().getResources().getString(R.string.friday);
//                    break;
//                default:
//                    day = "DayName";
//                    break;
//            }
//            Log.w("START_TIME_CHECK2", start_calendar.get(Calendar.YEAR) + "/" + (start_calendar.get(Calendar.MONTH) + 1) + "/" + start_calendar.get(Calendar.DAY_OF_MONTH) + " " + start_calendar.get(Calendar.HOUR) + ":" + start_calendar.get(Calendar.MINUTE) + " " + (start_calendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM") + ", " + day);
//            switch (end_calendar.get(Calendar.DAY_OF_WEEK)) {
//                case Calendar.SATURDAY:
//                    day = requireContext().getResources().getString(R.string.saturday);
//                    break;
//                case Calendar.SUNDAY:
//                    day = requireContext().getResources().getString(R.string.sunday);
//                    break;
//                case Calendar.MONDAY:
//                    day = requireContext().getResources().getString(R.string.monday);
//                    break;
//                case Calendar.TUESDAY:
//                    day = requireContext().getResources().getString(R.string.tuesday);
//                    break;
//                case Calendar.WEDNESDAY:
//                    day = requireContext().getResources().getString(R.string.wednesday);
//                    break;
//                case Calendar.THURSDAY:
//                    day = requireContext().getResources().getString(R.string.thursday);
//                    break;
//                case Calendar.FRIDAY:
//                    day = requireContext().getResources().getString(R.string.friday);
//                    break;
//                default:
//                    day = "DayName";
//                    break;
//            }
//            Log.w("END_TIME_CHECK2", end_calendar.get(Calendar.YEAR) + "/" + (end_calendar.get(Calendar.MONTH) + 1) + "/" + end_calendar.get(Calendar.DAY_OF_MONTH) + " " + end_calendar.get(Calendar.HOUR) + ":" + end_calendar.get(Calendar.MINUTE) + " " + (end_calendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM") + ", " + day);
//End OF TO DO

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

                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack(R.id.homeFragment, false);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }


}