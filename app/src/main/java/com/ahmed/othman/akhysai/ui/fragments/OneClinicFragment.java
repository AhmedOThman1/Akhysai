package com.ahmed.othman.akhysai.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.Article;
import com.ahmed.othman.akhysai.pojo.Clinic;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Calendar;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;


public class OneClinicFragment extends Fragment {


    public OneClinicFragment() {
        // Required empty public constructor
    }

    RoundedImageView clinic_item_image;
    TextView clinic_item_name, clinic_item_category, clinic_item_company_name, clinic_item_phone, clinic_item_website, clinic_item_body;
    Clinic currentClinic = new Clinic();
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one_clinic, container, false);
        context = getContext();

        clinic_item_image = view.findViewById(R.id.clinic_item_image);
        clinic_item_name = view.findViewById(R.id.clinic_item_name);
        clinic_item_category = view.findViewById(R.id.clinic_item_category);
        clinic_item_phone = view.findViewById(R.id.clinic_item_phone);
        clinic_item_website = view.findViewById(R.id.clinic_item_website);
        clinic_item_company_name = view.findViewById(R.id.clinic_item_company_name);
        clinic_item_body = view.findViewById(R.id.clinic_item_body);

        toolbar.setVisibility(View.VISIBLE);

        Bundle args = getArguments();
        if (args != null) {
            String json = args.getString("clinic", "");
            if (!json.trim().isEmpty()) {
                currentClinic = new Gson().fromJson(json, Clinic.class);
            }

        }


        Glide.with(context)
                .load(LauncherActivity.ImagesLink+currentClinic.getImage())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.akhysai_logo)
                .into(clinic_item_image);


        clinic_item_name.setText(currentClinic.getName());

        clinic_item_phone.setText(currentClinic.getPhone().contains(context.getResources().getString(R.string.phone)) ? currentClinic.getPhone() : context.getResources().getString(R.string.phone) + currentClinic.getPhone());

        clinic_item_category.setText(currentClinic.getCategory().contains(context.getResources().getString(R.string.category)) ? currentClinic.getCategory() : context.getResources().getString(R.string.category) + currentClinic.getCategory());

        clinic_item_website.setText(currentClinic.getWebsite().contains(context.getResources().getString(R.string.website)) ? currentClinic.getWebsite() : context.getResources().getString(R.string.website) + currentClinic.getWebsite());

        clinic_item_company_name.setText(currentClinic.getCompany_name().contains(context.getResources().getString(R.string.company)) ? currentClinic.getCompany_name() : context.getResources().getString(R.string.company) + currentClinic.getCompany_name());

        clinic_item_body.setText(currentClinic.getDetails());

        return view;
    }

}