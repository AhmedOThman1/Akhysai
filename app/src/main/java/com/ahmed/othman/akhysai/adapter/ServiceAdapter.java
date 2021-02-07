package com.ahmed.othman.akhysai.adapter;

import androidx.annotation.NonNull;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.network.ApiClient;
import com.ahmed.othman.akhysai.pojo.Service;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private String auth;
    private ArrayList<Service> Models;
    private Service current_model;
    private ServiceAdapter adapter;
    private AlertDialog dialog;

    public ServiceAdapter(@NonNull Context context, @NonNull String auth) {
        this.context = context;
        this.auth = auth;
        adapter = this;
    }

    public void setModels(ArrayList<Service> models) {
        Models = models;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_service_item, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        current_model = Models.get(position);
        final ServiceViewHolder ViewHolder = (ServiceViewHolder) holder;
        ViewHolder.service_name.setText(LauncherActivity.AppLanguage.equalsIgnoreCase("ar") ? current_model.getAr().getName() : current_model.getEn().getName());
        ViewHolder.service_price.setText(current_model.getPrice());
        ViewHolder.delete_services.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View deleteDialog = ((FragmentActivity) context).getLayoutInflater().inflate(R.layout.delete_dialog, null);

            deleteDialog.findViewById(R.id.delete_text).setOnClickListener(v2 -> {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("service_id", Models.get(position).getId());
                ApiClient.getINSTANCE().DeleteService(auth, jsonObject).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {

                            if (response.body().get("status").getAsString().equalsIgnoreCase("success") &&
                                    response.body().get("data").getAsString().equalsIgnoreCase("deleted")) {
                                Models.remove(position);
                                adapter.setModels(Models);
                                adapter.notifyDataSetChanged();
                            }

                        } else
                            Toast.makeText(context, "Failed, try again.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                        if (t.getMessage().contains("Unable to resolve host"))
                            Snackbar.make(ViewHolder.itemView, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.go_to_setting, v -> context.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)))
                                    .show();
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
        });
        ViewHolder.service_active.setOnClickListener(v -> {
            //TODO Call api edit active here
            if (Models.get(position).getIs_active().equalsIgnoreCase("1")) {
                Toast.makeText(context, "NotActive", Toast.LENGTH_SHORT).show();
                Models.get(position).setIs_active("0");
                ViewHolder.service_active.setColorFilter(ContextCompat.getColor(context,R.color.black2));
                this.notifyItemChanged(position);
            } else {
                Toast.makeText(context, "Active", Toast.LENGTH_SHORT).show();
                Models.get(position).setIs_active("1");
                ViewHolder.service_active.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary));
                this.notifyItemChanged(position);
            }
        });
        if (Models.get(position).getIs_active().equalsIgnoreCase("1")) {
            Log.w("Hererer",": 1 :");
            ViewHolder.service_active.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary));
        } else {
            Log.w("Hererer",": 1 :");
            ViewHolder.service_active.setColorFilter(ContextCompat.getColor(context,R.color.black2));
        }
    }

    @Override
    public int getItemCount() {
        return Models.size();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        // views
        TextView service_name, service_price;
        ImageView delete_services, service_active;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            // find view by id
            service_name = itemView.findViewById(R.id.service_name);
            service_price = itemView.findViewById(R.id.service_price);
            delete_services = itemView.findViewById(R.id.delete_services);
            service_active = itemView.findViewById(R.id.service_active);

        }
    }

}
