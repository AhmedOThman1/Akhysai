package com.ahmed.othman.akhysai.adapter;

import androidx.annotation.NonNull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.Review;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Review> Models;

    public ReviewAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setModels(ArrayList<Review> models) {
        Models = models;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_rate_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Review current_model = Models.get(position);
        final ReviewViewHolder ViewHolder = (ReviewViewHolder) holder;

        ViewHolder.review_body.setText(current_model.getReviewBody());

        ViewHolder.rate.setRating(current_model.getRate());
        ViewHolder.rate.setIsIndicator(true);

        ViewHolder.review_writer_name.setText(current_model.getReviewWriterName());

        long days = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - current_model.getDate());

        String temp = days < 1 ? context.getResources().getString(R.string.today) :
                days == 1 ? context.getResources().getString(R.string.one_day_ago) :
                        days == 2 ? context.getResources().getString(R.string.two_days_ago) :
                                days < 11 ? context.getResources().getString(R.string.ago) + days + context.getResources().getString(R.string.days_ago) :
                                        context.getResources().getString(R.string.ago) + days + context.getResources().getString(R.string.days_ago2);
        ViewHolder.review_date.setText(temp);
    }

    @Override
    public int getItemCount() {
        return Models.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView review_body, review_date, review_writer_name;
        RatingBar rate;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            review_body = itemView.findViewById(R.id.review_body);
            rate = itemView.findViewById(R.id.rate);
            review_date = itemView.findViewById(R.id.review_date);
            review_writer_name = itemView.findViewById(R.id.review_writer_name);
        }
    }

}
