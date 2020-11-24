package com.ahmed.othman.akhysai.adapter;

import androidx.annotation.NonNull;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.Akhysai;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;

public class AkhysaiSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Akhysai> Models;

    public AkhysaiSearchAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setModels(ArrayList<Akhysai> models) {
        Models = models;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_search_item, parent, false);
        return new AkhysaiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Akhysai current_model = Models.get(position);
        final AkhysaiViewHolder ViewHolder = (AkhysaiViewHolder) holder;


        Glide.with(context)
                .load(current_model.getPhoto())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.doctor_img2)
                .into(ViewHolder.akhysai_image);

        ViewHolder.akhysai_name.setText(current_model.getName());

        ViewHolder.akhysai_description.setText(current_model.getDescription());

        String temp = context.getResources().getString(R.string.years_of_experience2)+": " + current_model.getExperience_years() + context.getResources().getString(R.string.years);
        ViewHolder.akhysai_years_of_experience.setText(temp);

        ViewHolder.akhysai_rating.setRating(current_model.getRate());
        ViewHolder.akhysai_rating.setIsIndicator(true);

        temp = context.getResources().getString(R.string.this_rate_from) + current_model.getVisitor_num() + context.getResources().getString(R.string.visitor);
        ViewHolder.visitors_rate_num.setText(temp);

        temp = context.getResources().getString(R.string.session_price)+ current_model.getPrice()+context.getResources().getString(R.string.egp);
        ViewHolder.akhysai_price.setText(temp);

        ViewHolder.open_profile.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putString("id",Models.get(position).getAkhysai_id());
            bundle.putString("akhysai", new Gson().toJson(Models.get(position)));
            Navigation.findNavController(v).navigate(R.id.action_searchFragment_to_oneAkhysaiFragment,bundle);
        });

        ViewHolder.book_akhysai.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putString("id",Models.get(position).getAkhysai_id());
            bundle.putString("akhysai", new Gson().toJson(Models.get(position)));
            Navigation.findNavController(v).navigate(R.id.action_searchFragment_to_bookOneAkhysaiFragment,bundle);
        });

        ViewHolder.itemView.setOnClickListener(v->{
            Bundle bundle = new Bundle();
            bundle.putString("id",Models.get(position).getAkhysai_id());
            bundle.putString("akhysai", new Gson().toJson(Models.get(position)));
            Navigation.findNavController(v).navigate(R.id.action_searchFragment_to_oneAkhysaiFragment,bundle);
        });

    }

    @Override
    public int getItemCount() {
        return Models.size();
    }

    class AkhysaiViewHolder extends RecyclerView.ViewHolder {
        // views
        ImageView akhysai_image;
        TextView akhysai_name, akhysai_description, akhysai_years_of_experience, visitors_rate_num, akhysai_price, open_profile, book_akhysai;
        RatingBar akhysai_rating;

        public AkhysaiViewHolder(@NonNull View itemView) {
            super(itemView);
            akhysai_image = itemView.findViewById(R.id.akhysai_image);
            akhysai_name = itemView.findViewById(R.id.akhysai_name);
            akhysai_description = itemView.findViewById(R.id.akhysai_description);
            akhysai_years_of_experience = itemView.findViewById(R.id.akhysai_years_of_experience);
            akhysai_rating = itemView.findViewById(R.id.akhysai_rating);
            visitors_rate_num = itemView.findViewById(R.id.visitors_rate_num);
            akhysai_price = itemView.findViewById(R.id.akhysai_price);
            open_profile = itemView.findViewById(R.id.open_profile);
            book_akhysai = itemView.findViewById(R.id.book_akhysai);

            // findviewbyid
        }
    }

}
