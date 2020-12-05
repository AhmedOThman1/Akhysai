package com.ahmed.othman.akhysai.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.adapter.SpecialtiesAdapter;

import java.util.ArrayList;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;
import static com.ahmed.othman.akhysai.ui.fragments.HomeFragment.Fields;

public class MySpecialtiesFragment extends Fragment {

    public MySpecialtiesFragment() {
        // Required empty public constructor
    }

    Spinner specialty, field;
    RecyclerView my_specialties_recyclerview;
    ArrayList<String> mySpecialtiesTitles = new ArrayList<>();
    SpecialtiesAdapter specialtiesAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_specialties, container, false);

        toolbar.setVisibility(View.VISIBLE);
        mySpecialtiesTitles = getAllSpecialtiesByAkhysaiID();

        field = view.findViewById(R.id.field);
        specialty = view.findViewById(R.id.specialty);
        my_specialties_recyclerview = view.findViewById(R.id.my_specialties_recyclerview);


        ArrayAdapter<String> field_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, Fields);
        field_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        field.setAdapter(field_adapter);

        field.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                specialty.setVisibility(position > 0 ? View.VISIBLE : View.GONE);
                if (position > 0) {
                    ArrayAdapter<String> specialty_adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item,
                            position == 1 ? requireContext().getResources().getStringArray(R.array.specialties_medical) :
                                    requireContext().getResources().getStringArray(R.array.specialties_with_special_needs));
                    specialty_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    specialty.setAdapter(specialty_adapter);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        view.findViewById(R.id.add_available_date).setOnClickListener(v -> {
            if (field.getSelectedItemPosition() == 0) {
                field.setBackgroundResource(R.drawable.background_spinner_error);
                Toast.makeText(requireContext(), "Select field first", Toast.LENGTH_SHORT).show();
            } else if (specialty.getSelectedItemPosition() == 0) {
                field.setBackgroundResource(R.drawable.background_spinner);
                specialty.setBackgroundResource(R.drawable.background_spinner_error);
                Toast.makeText(requireContext(), "Select specialty first", Toast.LENGTH_SHORT).show();
            } else {
                field.setBackgroundResource(R.drawable.background_spinner);
                specialty.setBackgroundResource(R.drawable.background_spinner);
                postAddNewSpecialty("", specialty.getSelectedItemPosition()+"");
                view.findViewById(R.id.my_specialties_card).setVisibility(mySpecialtiesTitles.isEmpty()?View.GONE:View.VISIBLE);
            }
        });


        view.findViewById(R.id.my_specialties_card).setVisibility(mySpecialtiesTitles.isEmpty()?View.GONE:View.VISIBLE);
        specialtiesAdapter = new SpecialtiesAdapter(requireContext());
        specialtiesAdapter.setModels(mySpecialtiesTitles);
        my_specialties_recyclerview.setAdapter(specialtiesAdapter);
        my_specialties_recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                mySpecialtiesTitles.remove(position);
                specialtiesAdapter.setModels(mySpecialtiesTitles);
                specialtiesAdapter.notifyItemRemoved(position);
                view.findViewById(R.id.my_specialties_card).setVisibility(mySpecialtiesTitles.isEmpty()?View.GONE:View.VISIBLE);
            }
        }).attachToRecyclerView(my_specialties_recyclerview);

        return view;
    }

    private ArrayList<String> getAllSpecialtiesByAkhysaiID() {
        ArrayList<String> tempSpecialties = new ArrayList<>();
        tempSpecialties.add("التوحد");
        tempSpecialties.add("أطفال");
        tempSpecialties.add("قلب و اوعية دموية");
        tempSpecialties.add("نطق وتخاطب");
        tempSpecialties.add("انف واذن وحنجرة");

        return tempSpecialties;
    }

    private void postAddNewSpecialty(String field, String specialty) {

        mySpecialtiesTitles.add(specialty);
        specialtiesAdapter.setModels(mySpecialtiesTitles);
        specialtiesAdapter.notifyItemInserted(mySpecialtiesTitles.size()-1);
        Toast.makeText(requireContext(), "the new specialty added successfully", Toast.LENGTH_SHORT).show();
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