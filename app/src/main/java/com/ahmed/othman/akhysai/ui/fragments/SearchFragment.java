package com.ahmed.othman.akhysai.ui.fragments;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.adapter.AkhysaiSearchAdapter;
import com.ahmed.othman.akhysai.pojo.Akhysai;
import com.ahmed.othman.akhysai.pojo.Region;
import com.ahmed.othman.akhysai.pojo.Speciality;
import com.ahmed.othman.akhysai.ui.viewModels.AkhysaiViewModel;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.LanguageIso;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.navigation_view;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.CitiesString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Fields;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Regions;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.RegionsString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Cities;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.FieldsString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Specialties;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.SpecialtiesString;

public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }

    Spinner search_city, search_area, search_field, search_specialty;
    TextView search_button;
    RecyclerView searchRecycler;
    NestedScrollView nested_scroll;
    AkhysaiSearchAdapter akhysaiSearchAdapter;

    int temp1 = -1, temp2 = -1;
    AkhysaiViewModel akhysaiViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        search_city = view.findViewById(R.id.search_city);
        search_area = view.findViewById(R.id.search_area);
        search_field = view.findViewById(R.id.search_field);
        search_specialty = view.findViewById(R.id.search_specialty);
        search_button = view.findViewById(R.id.search_button);
        searchRecycler = view.findViewById(R.id.search_recyclerview);
        nested_scroll = view.findViewById(R.id.nested_scroll);

        akhysaiViewModel = ViewModelProviders.of(requireActivity()).get(AkhysaiViewModel.class);

        toolbar.setVisibility(View.VISIBLE);
        navigation_view.setCheckedItem(R.id.book_akhysai);
        akhysaiSearchAdapter = new AkhysaiSearchAdapter(requireContext());


        ArrayAdapter<String> city_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, CitiesString);
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        search_city.setAdapter(city_adapter);


        ArrayAdapter<String> area_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, RegionsString);
        area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        search_area.setAdapter(area_adapter);

        ArrayAdapter<String> field_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, FieldsString);
        field_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        search_field.setAdapter(field_adapter);

        ArrayAdapter<String> specialty_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, SpecialtiesString);
        specialty_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        search_specialty.setAdapter(specialty_adapter);


        search_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                search_area.setVisibility(position > 0 ? View.VISIBLE : View.GONE);
                if (position > 0) {
                    if (temp1 != -1) {
                        search_area.setSelection(temp1);
                        temp1 = -1;
                    } else if(!Cities.isEmpty()) akhysaiViewModel.getAllRegionsByCityId(requireActivity().getSharedPreferences(shared_pref,MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()),Cities.get(position-1).getCityId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search_field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                search_specialty.setVisibility(position > 0 ? View.VISIBLE : View.GONE);
                if (position > 0) {

                    if (temp2 != -1) {
                        search_specialty.setSelection(temp2);
                        temp2 = -1;
                    } else if(!Fields.isEmpty())
                        akhysaiViewModel.getAllSpecialitiesByFieldId(requireActivity().getSharedPreferences(shared_pref,MODE_PRIVATE).getString(LanguageIso,Locale.getDefault().getLanguage()),Fields.get(position-1).getFieldId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Bundle arg = getArguments();
        if (arg != null) {
            search_city.setSelection(arg.getInt("city", 0));
            temp1 = arg.getInt("area", 0);
            search_field.setSelection(arg.getInt("field", 0));
            temp2 = arg.getInt("specialty", 0);

            akhysaiSearchAdapter.setModels(
                    getAkhysaiSearchData(CitiesString.get(arg.getInt("city", 0)),
                            RegionsString.get(temp1),
                            FieldsString.get(arg.getInt("field", 0)),
                            SpecialtiesString.get(temp2)));
        }

        search_button.setOnClickListener(v -> {
            if (search_city.getSelectedItemPosition() == 0) {
                search_city.setBackgroundResource(R.drawable.background_spinner_error);
                search_area.setBackgroundResource(R.drawable.background_spinner);
                search_field.setBackgroundResource(R.drawable.background_spinner);
                search_specialty.setBackgroundResource(R.drawable.background_spinner);
                Toast.makeText(requireContext(), "Select city to start search", Toast.LENGTH_SHORT).show();
            } else if (search_area.getSelectedItemPosition() == 0) {
                search_city.setBackgroundResource(R.drawable.background_spinner);
                search_area.setBackgroundResource(R.drawable.background_spinner_error);
                search_field.setBackgroundResource(R.drawable.background_spinner);
                search_specialty.setBackgroundResource(R.drawable.background_spinner);
                Toast.makeText(requireContext(), "Select area to start search", Toast.LENGTH_SHORT).show();
            } else if (search_field.getSelectedItemPosition() == 0) {
                search_city.setBackgroundResource(R.drawable.background_spinner);
                search_area.setBackgroundResource(R.drawable.background_spinner);
                search_field.setBackgroundResource(R.drawable.background_spinner_error);
                search_specialty.setBackgroundResource(R.drawable.background_spinner);
                Toast.makeText(requireContext(), "Select field to start search", Toast.LENGTH_SHORT).show();
            } else if (search_specialty.getSelectedItemPosition() == 0) {
                search_city.setBackgroundResource(R.drawable.background_spinner);
                search_area.setBackgroundResource(R.drawable.background_spinner);
                search_field.setBackgroundResource(R.drawable.background_spinner);
                search_specialty.setBackgroundResource(R.drawable.background_spinner_error);
                Toast.makeText(requireContext(), "Select specialty to start search", Toast.LENGTH_SHORT).show();
            } else {

                nested_scroll.smoothScrollTo(0, searchRecycler.getTop());
            }
        });

        //Todo delete this
        akhysaiSearchAdapter.setModels(getAkhysaiSearchData("", "", "", ""));
        //End of TODO
        searchRecycler.setAdapter(akhysaiSearchAdapter);
        searchRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchRecycler.setHasFixedSize(true);

        view.findViewById(R.id.go_to_wifi_setting).setOnClickListener(v -> requireContext().startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)));
        nested_scroll.smoothScrollTo(0, 0);
        searchRecycler.setFocusable(false);
        view.findViewById(R.id.search_card).requestFocus();


        akhysaiViewModel.specialitiesMutableLiveData.observe(requireActivity(), specialities -> {
            Specialties = new ArrayList<>(specialities);
            SpecialtiesString.clear();
            SpecialtiesString.add(requireActivity().getResources().getString(R.string.choose_specialty));
            for (Speciality speciality : Specialties)
                SpecialtiesString.add(speciality.getSpecialityName());

            ArrayAdapter<String> specialty_adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, SpecialtiesString);
            specialty_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            search_specialty.setAdapter(specialty_adapter2);
        });


        akhysaiViewModel.regionsMutableLiveData.observe(requireActivity(), regions -> {
            Regions = new ArrayList<>(regions);
            RegionsString.clear();
            RegionsString.add(requireActivity().getResources().getString(R.string.choose_region));
            for (Region region : Regions)
                RegionsString.add(region.getRegionName());

            ArrayAdapter<String> area_adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, RegionsString);
            area_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            search_area.setAdapter(area_adapter2);
        });
        return view;
    }

    private ArrayList<Akhysai> getAkhysaiSearchData(String city, String area, String field, String specialty) {
        //TODO delete this
        ArrayList<Akhysai> akhysaiArrayList = new ArrayList<>();
//        akhysaiArrayList.add(new Akhysai("", "حسام اشرف", "طبيب انف و اذن و حنجرة و حاصل ع الزماله البريطانية", "محاضر معتمد من نقابة الاطباء\n دبلوم تجميل و ليزر و دبلومة مكافحة عدوي جودة", 2, (float) 3.5, 19, 380));
//        akhysaiArrayList.add(new Akhysai("", "زياد صقر", "طبيب جراح و حاصل ع الزماله البريطانية", "محاضر معتمد من نقابة الاطباء\n دبلوم تجميل و ليزر و دبلومة مكافحة عدوي جودة", 2, (float) 4, 29, 250));
//        akhysaiArrayList.add(new Akhysai("", "احمد عثمان", "طبيب جراح قلوب الناس اداويها", "محاضر معتمد من نقابة الاطباء\n دبلوم تجميل و ليزر و دبلومة مكافحة عدوي جودة", 6, (float) 4.5, 42, 620));
        //End of delete
        return akhysaiArrayList;
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