package com.ahmed.othman.akhysai.adapter;

import androidx.annotation.NonNull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.othman.akhysai.R;

import java.util.ArrayList;

public class SpecialtiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<String> Models;
    private String current_model;

    public SpecialtiesAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setModels(ArrayList<String> models) {
        Models = models;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_specialty_item, parent, false);
        return new SpecialtyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        current_model = Models.get(position);
        final SpecialtyViewHolder ViewHolder = (SpecialtyViewHolder) holder;

        ViewHolder.specialty_title.setText(current_model);

        ViewHolder.specialty_delete_background.setOnClickListener(v -> {
            Models.remove(position);
            this.setModels(Models);
            this.notifyItemRemoved(position);
        });
        ViewHolder.specialty_delete_icon.setOnClickListener(v -> {
            Models.remove(position);
            this.setModels(Models);
            this.notifyItemRemoved(position);
        });

    }

    @Override
    public int getItemCount() {
        return Models.size();
    }

    class SpecialtyViewHolder extends RecyclerView.ViewHolder {
        // views
        TextView specialty_title, specialty_delete_background;
        ImageView specialty_delete_icon;

        public SpecialtyViewHolder(@NonNull View itemView) {
            super(itemView);

            specialty_title = itemView.findViewById(R.id.specialty_title);
            specialty_delete_background = itemView.findViewById(R.id.specialty_delete_background);
            specialty_delete_icon = itemView.findViewById(R.id.specialty_delete_icon);
        }
    }

}
