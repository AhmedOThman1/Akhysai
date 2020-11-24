package com.ahmed.othman.akhysai.adapter;

import androidx.annotation.NonNull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.BookingRequest;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class BookingRequestsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<BookingRequest> Models;
    private BookingRequest current_model;

    public BookingRequestsAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setModels(ArrayList<BookingRequest> models) {
        Models = models;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_booking_request_item, parent, false);
        return new BookingRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        current_model = Models.get(position);
        final BookingRequestViewHolder ViewHolder = (BookingRequestViewHolder) holder;

        ViewHolder.username.setText(current_model.getName());
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(current_model.getStart_time());
        Calendar end = Calendar.getInstance();
        start.setTimeInMillis(current_model.getEnd_time());
        String temp = context.getResources().getString(R.string.day) + getDayName(start.get(Calendar.DAY_OF_WEEK));
        ViewHolder.booking_day.setText(temp);
        temp = context.getResources().getString(R.string.from)
                + (start.get(Calendar.HOUR) == 0 ? "12" : start.get(Calendar.HOUR))
                + ":" + (start.get(Calendar.MINUTE) < 10 ? "0" + start.get(Calendar.MINUTE) : start.get(Calendar.MINUTE))
                + (start.get(Calendar.AM_PM) == Calendar.AM ?
                context.getResources().getString(R.string.am)
                : context.getResources().getString(R.string.pm))
                + context.getResources().getString(R.string.to)
                + (end.get(Calendar.HOUR) == 0 ? "12" : end.get(Calendar.HOUR))
                + ":" + (end.get(Calendar.MINUTE) < 10 ? "0" + end.get(Calendar.MINUTE) : end.get(Calendar.MINUTE))
                + (end.get(Calendar.AM_PM) == Calendar.AM ?
                context.getResources().getString(R.string.am)
                : context.getResources().getString(R.string.pm));
        ViewHolder.booking_hours.setText(temp);
    }

    private String getDayName(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SATURDAY:
                return context.getResources().getString(R.string.saturday);
            case Calendar.SUNDAY:
                return context.getResources().getString(R.string.sunday);
            case Calendar.MONDAY:
                return context.getResources().getString(R.string.monday);
            case Calendar.TUESDAY:
                return context.getResources().getString(R.string.tuesday);
            case Calendar.WEDNESDAY:
                return context.getResources().getString(R.string.wednesday);
            case Calendar.THURSDAY:
                return context.getResources().getString(R.string.thursday);
            case Calendar.FRIDAY:
                return context.getResources().getString(R.string.friday);
            default:
                return  "DayName";
        }

    }

    @Override
    public int getItemCount() {
        return Models.size();
    }

    class BookingRequestViewHolder extends RecyclerView.ViewHolder {
        // views
        CircleImageView user_image;
        TextView username, booking_day, booking_hours;

        public BookingRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            user_image = itemView.findViewById(R.id.user_image);
            username = itemView.findViewById(R.id.username);
            booking_day = itemView.findViewById(R.id.booking_day);
            booking_hours = itemView.findViewById(R.id.booking_hours);

        }
    }

}
