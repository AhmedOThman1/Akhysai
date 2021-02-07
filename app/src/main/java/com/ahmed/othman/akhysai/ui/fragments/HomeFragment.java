package com.ahmed.othman.akhysai.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.RecyclerViewTouchListener;
import com.ahmed.othman.akhysai.adapter.ArticleCategoryAdapter;
import com.ahmed.othman.akhysai.pojo.BlogCategories;
import com.ahmed.othman.akhysai.pojo.City;
import com.ahmed.othman.akhysai.pojo.DirectoryCategories;
import com.ahmed.othman.akhysai.pojo.Field;
import com.ahmed.othman.akhysai.pojo.Qualification;
import com.ahmed.othman.akhysai.pojo.Region;
import com.ahmed.othman.akhysai.pojo.Speciality;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.BlogCategoriesString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.DirectoryCategoriesString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.LanguageIso;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Qualifications;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.QualificationsString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Regions;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.akhysaiViewModel;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.logged_in;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.navigation_view;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.toolbar;

import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Fields;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.FieldsString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Specialties;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.SpecialtiesString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Cities;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.CitiesString;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.RegionsString;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    Spinner home_search_city, home_search_area, home_search_field, home_search_specialty;
    TextView home_search_button, home_first_title, get_started;
    RecyclerView akhysai_library;
    NestedScrollView nested_scroll;
    boolean isLoggedIn, check_ScrollingUp = false;

    ArticleCategoryAdapter articleCategoryAdapter;
    public static View view;

    ShimmerFrameLayout akhysai_library_shimmer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);

        home_search_city = view.findViewById(R.id.home_search_city);
        home_search_area = view.findViewById(R.id.home_search_area);
        home_search_field = view.findViewById(R.id.home_search_field);
        home_search_specialty = view.findViewById(R.id.home_search_specialty);
        home_search_button = view.findViewById(R.id.home_search_button);
        akhysai_library = view.findViewById(R.id.akhysai_library);
        akhysai_library_shimmer = view.findViewById(R.id.akhysai_library_shimmer);
        nested_scroll = view.findViewById(R.id.nested_scroll);
        home_first_title = view.findViewById(R.id.home_first_title);
        get_started = view.findViewById(R.id.get_started);

        toolbar.setVisibility(View.VISIBLE);
        navigation_view.setCheckedItem(R.id.home);

        //start shimmer
        akhysai_library_shimmer.setVisibility(View.VISIBLE);
        akhysai_library_shimmer.startShimmer();
        akhysai_library.setVisibility(View.GONE);
        ////

        isLoggedIn = requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).getBoolean(logged_in, false);
        if (isLoggedIn) {
            view.findViewById(R.id.home_sign_up_img).setVisibility(View.GONE);
            view.findViewById(R.id.home_sign_up_title).setVisibility(View.GONE);
            view.findViewById(R.id.home_sign_up_body).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.num1)).setText("1");
            ((TextView) view.findViewById(R.id.num2)).setText("2");
            ((TextView) view.findViewById(R.id.num3)).setText("3");
        }

        initSpinnerData();

        home_search_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                home_search_area.setVisibility(position > 0 ? View.VISIBLE : View.GONE);
                if (position > 0) {
                    akhysaiViewModel.getAllRegionsByCityId(requireActivity().getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()), Cities.get(position - 1).getCityId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        home_search_field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                home_search_specialty.setVisibility(position > 0 ? View.VISIBLE : View.GONE);
                if (position > 0) {
                    akhysaiViewModel.getAllSpecialitiesByFieldId(requireActivity().getSharedPreferences(shared_pref, MODE_PRIVATE).getString(LanguageIso, Locale.getDefault().getLanguage()), Fields.get(position - 1).getFieldId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        articleCategoryAdapter = new ArticleCategoryAdapter(requireContext());

        akhysai_library.setAdapter(articleCategoryAdapter);
        akhysai_library.setLayoutManager(new LinearLayoutManager(requireContext()));
        akhysai_library.setHasFixedSize(true);


        if(!LauncherActivity.BlogCategories.isEmpty()){
            view.findViewById(R.id.home_third_title).setVisibility(View.VISIBLE);
            articleCategoryAdapter.setBlogCategories(LauncherActivity.BlogCategories);
            articleCategoryAdapter.notifyDataSetChanged();

            akhysai_library_shimmer.stopShimmer();
            akhysai_library_shimmer.hideShimmer();
            akhysai_library_shimmer.setVisibility(View.GONE);
            akhysai_library.setVisibility(View.VISIBLE);
        }

        akhysai_library.addOnItemTouchListener(new RecyclerViewTouchListener(requireContext(), akhysai_library, new RecyclerViewTouchListener.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("articles", new Gson().toJson(LauncherActivity.BlogCategories.get(position)));
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_categoryArticlesFragment, bundle);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        view.findViewById(R.id.home_third_title).setVisibility(LauncherActivity.BlogCategories.isEmpty() ? View.GONE : View.VISIBLE);

        view.findViewById(R.id.home_sign_up_img).setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_loginFragment));
        view.findViewById(R.id.home_sign_up_title).setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_loginFragment));
        view.findViewById(R.id.home_sign_up_body).setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_loginFragment));


        view.findViewById(R.id.home_search_img).setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_searchFragment));
        view.findViewById(R.id.home_search_title).setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_searchFragment));
        view.findViewById(R.id.home_search_body).setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_searchFragment));

        home_search_button.setOnClickListener(v -> {
//            if (!CitiesString.isEmpty()&&home_search_city.getSelectedItemPosition() == 0) {
//                home_search_city.setBackgroundResource(R.drawable.background_spinner_error);
//                home_search_area.setBackgroundResource(R.drawable.background_spinner);
//                home_search_field.setBackgroundResource(R.drawable.background_spinner);
//                home_search_specialty.setBackgroundResource(R.drawable.background_spinner);
//                Toast.makeText(requireContext(), "Select city to start search", Toast.LENGTH_SHORT).show();
//            } else if (!RegionsString.isEmpty()&&home_search_area.getSelectedItemPosition() == 0) {
//                home_search_city.setBackgroundResource(R.drawable.background_spinner);
//                home_search_area.setBackgroundResource(R.drawable.background_spinner_error);
//                home_search_field.setBackgroundResource(R.drawable.background_spinner);
//                home_search_specialty.setBackgroundResource(R.drawable.background_spinner);
//                Toast.makeText(requireContext(), "Select area to start search", Toast.LENGTH_SHORT).show();
//            } else if (!FieldsString.isEmpty()&&home_search_field.getSelectedItemPosition() == 0) {
//                home_search_city.setBackgroundResource(R.drawable.background_spinner);
//                home_search_area.setBackgroundResource(R.drawable.background_spinner);
//                home_search_field.setBackgroundResource(R.drawable.background_spinner_error);
//                home_search_specialty.setBackgroundResource(R.drawable.background_spinner);
//                Toast.makeText(requireContext(), "Select field to start search", Toast.LENGTH_SHORT).show();
//            } else if (!SpecialtiesString.isEmpty()&&home_search_specialty.getSelectedItemPosition() == 0) {
//                home_search_city.setBackgroundResource(R.drawable.background_spinner);
//                home_search_area.setBackgroundResource(R.drawable.background_spinner);
//                home_search_field.setBackgroundResource(R.drawable.background_spinner);
//                home_search_specialty.setBackgroundResource(R.drawable.background_spinner_error);
//                Toast.makeText(requireContext(), "Select specialty to start search", Toast.LENGTH_SHORT).show();
//            } else if(!CitiesString.isEmpty()&&!RegionsString.isEmpty()&&!FieldsString.isEmpty()&&!SpecialtiesString.isEmpty()){
            Log.w("Bad", home_search_city.getSelectedItemPosition() + "\n" +
                    home_search_area.getSelectedItemPosition() + "\n" +
                    home_search_field.getSelectedItemPosition() + "\n" +
                    home_search_specialty.getSelectedItemPosition() + "\n");

            String city, region, field, specialty;
            if (home_search_city.getSelectedItemPosition() > 0 && home_search_city.getSelectedItemPosition() <= Cities.size())
                city = "" + Cities.get(home_search_city.getSelectedItemPosition() - 1).getCityId();
            else
                city = "all";

            if (home_search_area.getVisibility()==View.VISIBLE && home_search_area.getSelectedItemPosition() > 0 && home_search_area.getSelectedItemPosition() <= Regions.size())
                region = "" + Regions.get(home_search_area.getSelectedItemPosition() - 1).getRegionId();
            else
                region = "all";

            if (home_search_field.getSelectedItemPosition() > 0 && home_search_field.getSelectedItemPosition() <= Fields.size())
                field = "" + Fields.get(home_search_field.getSelectedItemPosition() - 1).getFieldId();
            else
                field = "all";

            if (home_search_specialty.getVisibility()==View.VISIBLE && home_search_specialty.getSelectedItemPosition() > 0 && home_search_specialty.getSelectedItemPosition() <= Specialties.size())
                specialty = "" + Specialties.get(home_search_specialty.getSelectedItemPosition() - 1).getSpecialityId();
            else
                specialty = "all";

            Bundle bundle = new Bundle();

            bundle.putInt("cityPosition", home_search_city.getSelectedItemPosition());
            bundle.putInt("regionPosition", home_search_area.getSelectedItemPosition());
            bundle.putInt("fieldPosition", home_search_field.getSelectedItemPosition());
            bundle.putInt("specialtyPosition", home_search_specialty.getSelectedItemPosition());
            bundle.putString("city", city);
            bundle.putString("region", region);
            bundle.putString("field", field);
            bundle.putString("specialty", specialty);
            Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_searchFragment, bundle);
//            }
        });

        get_started.setOnClickListener(v -> nested_scroll.smoothScrollTo(0, home_first_title.getTop()));
        view.findViewById(R.id.home_third_title).setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_libraryArticlesFragment));

//        nested_scroll.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
//            if (scrollY > oldScrollY + 2) {
//                // User scrolls down
//                if (!check_ScrollingUp) {
//                    Log.w("UpDown", "DDDDDDDD");
//                    toolbar.startAnimation(AnimationUtils
//                            .loadAnimation(requireContext(), R.anim.trans_upwards));
//                    new Handler().postDelayed(()->toolbar.setVisibility(View.GONE),500);
//                    check_ScrollingUp = true;
//                }
//
//            } else if (scrollY < oldScrollY - 2) {
//
//                // Scrolling up
//                if (check_ScrollingUp) {
//                    Log.w("UpDown", "UUUUUUUUUU");
//                    toolbar.setVisibility(View.VISIBLE);
//                    toolbar.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.trans_downwards));
//                    check_ScrollingUp = false;
//                }
//            }
//
//        });


        return view;
    }

    private void initSpinnerData() {

        ArrayAdapter<String> city_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, CitiesString);
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        home_search_city.setAdapter(city_adapter);

        ArrayAdapter<String> field_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, FieldsString);
        field_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        home_search_field.setAdapter(field_adapter);

        akhysaiViewModel.citiesMutableLiveData.observe(getViewLifecycleOwner(), cities -> {
            Cities = new ArrayList<>(cities);
            CitiesString.clear();
            CitiesString.add(getResources().getString(R.string.choose_city));
            for (City city : Cities)
                CitiesString.add(city.getCityName());

            ArrayAdapter<String> city_adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, CitiesString);
            city_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            home_search_city.setAdapter(city_adapter2);
        });

        akhysaiViewModel.regionsMutableLiveData.observe(getViewLifecycleOwner(), regions -> {
            Regions = new ArrayList<>(regions);
            RegionsString.clear();
            RegionsString.add(getResources().getString(R.string.choose_region));
            for (Region region : Regions)
                RegionsString.add(region.getRegionName());

            ArrayAdapter<String> area_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, RegionsString);
            area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            home_search_area.setAdapter(area_adapter);
        });

        akhysaiViewModel.fieldsMutableLiveData.observe(getViewLifecycleOwner(), fields -> {
            Fields = new ArrayList<>(fields);
            FieldsString.clear();
            FieldsString.add(getResources().getString(R.string.choose_field));
            for (Field field : Fields)
                FieldsString.add(field.getFieldName());

            ArrayAdapter<String> field_adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, FieldsString);
            field_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            home_search_field.setAdapter(field_adapter2);
        });

        akhysaiViewModel.specialitiesMutableLiveData.observe(getViewLifecycleOwner(), specialities -> {
            Specialties = new ArrayList<>(specialities);
            SpecialtiesString.clear();
            SpecialtiesString.add(getResources().getString(R.string.choose_specialty));
            for (Speciality speciality : Specialties)
                SpecialtiesString.add(speciality.getSpecialityName());

            ArrayAdapter<String> specialty_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, SpecialtiesString);
            specialty_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            home_search_specialty.setAdapter(specialty_adapter);
        });


        akhysaiViewModel.blogCategoriesMutableLiveData.observe(getViewLifecycleOwner(), blogCategories -> {
            LauncherActivity.BlogCategories = new ArrayList<>(blogCategories);
            BlogCategoriesString.clear();
            BlogCategoriesString.add(getResources().getString(R.string.choose_category));
            for (BlogCategories categories : LauncherActivity.BlogCategories)
                BlogCategoriesString.add(categories.getName());

            //To Do view Categories here *recycler view
            view.findViewById(R.id.home_third_title).setVisibility(LauncherActivity.BlogCategories.isEmpty() ? View.GONE : View.VISIBLE);
            articleCategoryAdapter.setBlogCategories(blogCategories);
            articleCategoryAdapter.notifyDataSetChanged();

            akhysai_library_shimmer.stopShimmer();
            akhysai_library_shimmer.hideShimmer();
            akhysai_library_shimmer.setVisibility(View.GONE);
            akhysai_library.setVisibility(View.VISIBLE);
        });


        akhysaiViewModel.directoryCategoriesMutableLiveData.observe(getViewLifecycleOwner(), directoryCategories -> {
            LauncherActivity.DirectoryCategories = new ArrayList<>(directoryCategories);
            DirectoryCategoriesString.clear();
            DirectoryCategoriesString.add(getResources().getString(R.string.choose_category));
            for (DirectoryCategories categories : LauncherActivity.DirectoryCategories)
                DirectoryCategoriesString.add(categories.getName());
        });


        akhysaiViewModel.qualificationsMutableLiveData.observe(getViewLifecycleOwner(), qualifications -> {
            Qualifications = new ArrayList<>(qualifications);
            QualificationsString.clear();
            QualificationsString.add(getResources().getString(R.string.choose_qualification));
            for (Qualification qualification : Qualifications)
                QualificationsString.add(qualification.getName());

        });

    }



    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event

                /* exit only if the user click the back button twice in the Main Activity**/
                if (doubleBackToExitPressedOnce) {
                    requireActivity().finish();
                    return;
                }
                doubleBackToExitPressedOnce = true;
                Toast.makeText(requireContext(), getResources().getString(R.string.please_click_back_again_to_exit), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

}