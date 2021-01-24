package com.ahmed.othman.akhysai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.Article;
import com.ahmed.othman.akhysai.pojo.Clinic;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClinicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Clinic> Models;

    public ClinicAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setModels(ArrayList<Clinic> models) {
        Models = models;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_clinic_item, parent, false);
        return new ClinicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Clinic current_model = Models.get(position);

        final ClinicViewHolder ViewHolder = (ClinicViewHolder) holder;

        ViewHolder.clinic_item_name.setText(current_model.getName());

        ViewHolder.clinic_item_phone.setText(current_model.getPhone().contains(context.getResources().getString(R.string.phone)) ? current_model.getPhone() : context.getResources().getString(R.string.phone) + current_model.getPhone());

        ViewHolder.clinic_item_category.setText(current_model.getCategory().contains(context.getResources().getString(R.string.category)) ? current_model.getCategory() : context.getResources().getString(R.string.category) + current_model.getCategory());

        ViewHolder.clinic_item_website.setText(current_model.getWebsite().contains(context.getResources().getString(R.string.website)) ? current_model.getWebsite() : context.getResources().getString(R.string.website) + current_model.getWebsite());

        ViewHolder.clinic_item_company_name.setText(current_model.getCompany_name().contains(context.getResources().getString(R.string.company)) ? current_model.getCompany_name() : context.getResources().getString(R.string.company) + current_model.getCompany_name());

        Glide.with(context)
                .load(LauncherActivity.ImagesLink+current_model.getImage())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.akhysai_logo)
                .into(ViewHolder.clinic_item_image);
    }

    @Override
    public int getItemCount() {
        return Models.size();
    }

    static class ClinicViewHolder extends RecyclerView.ViewHolder {
        // views
        CircleImageView clinic_item_image;
        TextView clinic_item_name, clinic_item_category, clinic_item_phone, clinic_item_website, clinic_item_company_name;

        public ClinicViewHolder(@NonNull View itemView) {
            super(itemView);
            clinic_item_image = itemView.findViewById(R.id.clinic_item_image);
            clinic_item_name = itemView.findViewById(R.id.clinic_item_name);
            clinic_item_category = itemView.findViewById(R.id.clinic_item_category);
            clinic_item_phone = itemView.findViewById(R.id.clinic_item_phone);
            clinic_item_website = itemView.findViewById(R.id.clinic_item_website);
            clinic_item_company_name = itemView.findViewById(R.id.clinic_item_company_name);
        }
    }

}
