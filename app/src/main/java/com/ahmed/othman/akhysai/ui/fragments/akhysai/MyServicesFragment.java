package com.ahmed.othman.akhysai.ui.fragments.akhysai;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.adapter.ServiceAdapter;
import com.ahmed.othman.akhysai.network.ApiClient;
import com.ahmed.othman.akhysai.pojo.Service;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.toolbar;

public class MyServicesFragment extends Fragment {

    public MyServicesFragment() {
        // Required empty public constructor
    }


    TextInputLayout service_name_in_arabic, service_name_in_english, price;
    RecyclerView my_services_recycler;
    ShimmerFrameLayout my_services_shimmer;
    View view;
    ServiceAdapter serviceAdapter;
    ArrayList<Service> Services = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_services, container, false);

        toolbar.setVisibility(View.VISIBLE);
        service_name_in_arabic = view.findViewById(R.id.service_name_in_arabic);
        service_name_in_english = view.findViewById(R.id.service_name_in_english);
        price = view.findViewById(R.id.price);

        my_services_recycler = view.findViewById(R.id.my_services_recycler);
        my_services_shimmer = view.findViewById(R.id.my_services_shimmer);

        //shimmer
        my_services_shimmer.startShimmer();
        my_services_shimmer.setVisibility(View.VISIBLE);
        my_services_recycler.setVisibility(View.GONE);
        ////

        serviceAdapter = new ServiceAdapter(requireContext(), "Bearer " + LauncherActivity.currentAkhysai.getApiToken());
        my_services_recycler.setAdapter(serviceAdapter);
        my_services_recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        //get services
        getServicesByAkhysaiToken(LauncherActivity.currentAkhysai.getApiToken());

        view.findViewById(R.id.add_service).setOnClickListener(v -> {

            if (service_name_in_arabic.getEditText().getText().toString().trim().isEmpty()) {
                service_name_in_arabic.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                service_name_in_arabic.requestFocus();
                open_keyboard(service_name_in_arabic.getEditText());
            } else if (service_name_in_english.getEditText().getText().toString().trim().isEmpty()) {
                service_name_in_arabic.setError(null);
                service_name_in_english.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                service_name_in_english.requestFocus();
                open_keyboard(service_name_in_english.getEditText());
            } else if (price.getEditText().getText().toString().trim().isEmpty()) {
                service_name_in_arabic.setError(null);
                service_name_in_english.setError(null);
                price.setError(requireActivity().getResources().getString(R.string.can_not_be_empty));
                price.requestFocus();
                open_keyboard(price.getEditText());
            } else {
                service_name_in_arabic.setError(null);
                service_name_in_english.setError(null);
                price.setError(null);

                AddNewService(LauncherActivity.currentAkhysai.getApiToken(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), service_name_in_arabic.getEditText().getText().toString().trim()),
                        RequestBody.create(MediaType.parse("multipart/form-data"), service_name_in_english.getEditText().getText().toString().trim()),
                        RequestBody.create(MediaType.parse("multipart/form-data"), price.getEditText().getText().toString().trim()));

//                String json = "{\"name\":[\"ar\":\"arbic\",\"en\":\"english\"],\"price\":\"125\"}";
//                try{
//                    JSONObject object = new JSONObject(json);
//                    JsonParser jsonParser = new JsonParser();
//                    JsonObject jsonObject = (JsonObject) jsonParser.parse(object.toString());
//                    AddNewService(LauncherActivity.currentAkhysai.getApiToken(), jsonObject);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("name[ar]", service_name_in_arabic.getEditText().getText().toString().trim());
//                jsonObject.addProperty("name[en]", service_name_in_english.getEditText().getText().toString().trim());
//                jsonObject.addProperty("price", price.getEditText().getText().toString().trim());
//                AddNewService(LauncherActivity.currentAkhysai.getApiToken(), jsonObject);
            }
        });


        return view;
    }

    private void AddNewService(String akhysaiToken, RequestBody nameAr, RequestBody nameEn, RequestBody priceNum) {
        ApiClient.getINSTANCE().AddNewService("Bearer " + akhysaiToken, nameAr,nameEn,priceNum).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body().get("status").getAsString().equalsIgnoreCase("success")) {
                        Service service = new Gson().fromJson(response.body().get("data").getAsJsonObject().toString(), Service.class);
                        Services.add(service);
                        serviceAdapter.setModels(Services);
                        serviceAdapter.notifyItemInserted(Services.size()-1);

                        service_name_in_arabic.getEditText().setText("");
                        service_name_in_english.getEditText().setText("");
                        price.getEditText().setText("");
                    }

                } else {
                    Toast.makeText(requireContext(), "Faild, try again.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (t.getMessage().contains("Unable to resolve host"))
                    Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                            .setAction(R.string.go_to_setting, v -> requireContext().startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)))
                            .show();
            }
        });
    }

    private void getServicesByAkhysaiToken(String akhysaiToken) {
        ApiClient.getINSTANCE().getAkhysaiServices("Bearer " + akhysaiToken).enqueue(new Callback<ArrayList<Service>>() {
            @Override
            public void onResponse(Call<ArrayList<Service>> call, Response<ArrayList<Service>> response) {
                if (response.isSuccessful()) {
                    if (response.body().isEmpty()) {
                        my_services_recycler.setVisibility(View.GONE);
                        my_services_shimmer.stopShimmer();
                        my_services_shimmer.hideShimmer();
                        my_services_shimmer.setVisibility(View.GONE);
                        view.findViewById(R.id.my_services_card).setVisibility(View.GONE);
                    } else {
                        Services = new ArrayList<>(response.body());
                        serviceAdapter.setModels(Services);
                        serviceAdapter.notifyDataSetChanged();
                        my_services_recycler.setVisibility(View.VISIBLE);
                        my_services_shimmer.stopShimmer();
                        my_services_shimmer.hideShimmer();
                        my_services_shimmer.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Service>> call, Throwable t) {
                if (t.getMessage().contains("Unable to resolve host"))
                    Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                            .setAction(R.string.go_to_setting, v -> requireContext().startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)))
                            .show();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack(R.id.homeFragment, false);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }


    private void open_keyboard(EditText editText) {
        editText.requestFocus();     // editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);     // Context.INPUT_METHOD_SERVICE
        assert imm != null;
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT); //    first param -> editText
    }


}