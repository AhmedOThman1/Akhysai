package com.ahmed.othman.akhysai.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.network.ApiClient;
import com.ahmed.othman.akhysai.pojo.Akhysai;
import com.ahmed.othman.akhysai.pojo.Article;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.toolbar;


public class OneArticleFragment extends Fragment {


    public OneArticleFragment() {
        // Required empty public constructor
    }

    RoundedImageView article_item_image;
    CircleImageView article_writer_image;
    TextView article_item_title, article_item_category, article_item_time, article_item_body, article_writer_name;
    Article article = new Article();
    Context context;
    Akhysai Article_writer = new Akhysai();
    boolean fromAkhysaiProfile = false;
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_one_article, container, false);
        context = getContext();

        article_item_image = view.findViewById(R.id.article_item_image);
        article_item_title = view.findViewById(R.id.article_item_title);
        article_item_category = view.findViewById(R.id.article_item_category);
        article_item_time = view.findViewById(R.id.article_item_time);
        article_item_body = view.findViewById(R.id.article_item_body);
        article_writer_image = view.findViewById(R.id.article_writer_image);
        article_writer_name = view.findViewById(R.id.article_writer_name);

        toolbar.setVisibility(View.VISIBLE);

        Bundle args = getArguments();
        if (args != null) {
            fromAkhysaiProfile = args.getBoolean("from_akhysai_profile", false);
            String json = args.getString("article", "");
            Log.w("JSON_ARTICLE", json + "");
            if (!json.trim().isEmpty()) {
                article = new Gson().fromJson(json, Article.class);
                getArticleWriterData(article.getSpecialistId());
            }
        }


        ///// Article
        Glide.with(context)
                .load(LauncherActivity.ImagesLink + article.getPicture())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.akhysai_logo)
                .into(article_item_image);

        article_item_title.setText(article.getTitle());

        article_item_category.setText(context.getResources().getString(R.string.category) + getCategoryNameById(article.getCategoryId()));

        article_item_body.setText(Html.fromHtml(article.getBody()));

        article_item_time.setText(article.getCreatedAt());

        ////// Article Writer
        Glide.with(context)
                .load(LauncherActivity.ImagesLink + Article_writer.getProfile_picture())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.doctor_img2)
                .into(article_writer_image);

        article_writer_name.setText(String.valueOf(Article_writer.getName()));

        view.findViewById(R.id.writer_card).setOnClickListener(v -> {
            if (fromAkhysaiProfile)
                requireActivity().onBackPressed();
            else {
                Bundle bundle = new Bundle();
                bundle.putString("akhysai", new Gson().toJson(Article_writer));
                Navigation.findNavController(v).navigate(R.id.action_oneArticleFragment_to_oneAkhysaiFragment, bundle);
            }
        });

        return view;
    }

    private String getCategoryNameById(String Id) {
        for (int i = 0; i < LauncherActivity.BlogCategories.size(); i++) {
            if (String.valueOf(LauncherActivity.BlogCategories.get(i).getId()).equalsIgnoreCase(Id))
                return LauncherActivity.BlogCategories.get(i).getName();
        }
        return "";
    }

    private void getArticleWriterData(String articleWriterId) {

        ApiClient.getINSTANCE().getAkhysaiById(articleWriterId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body().get("status").getAsString().equalsIgnoreCase("success")) {
                        Article_writer = new Gson().fromJson(response.body().get("data").getAsJsonObject().get("specialist").getAsJsonObject().toString(), Akhysai.class);

                        if (Article_writer.getProfile_picture() != null)
                            Glide.with(context)
                                    .load(LauncherActivity.ImagesLink + Article_writer.getProfile_picture())
                                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                                    .placeholder(R.drawable.doctor_img2)
                                    .into(article_writer_image);

                        if (Article_writer.getEn() != null && Article_writer.getEn().getName() != null && Article_writer.getAr() != null && Article_writer.getAr().getName() != null)
                            article_writer_name.setText((LauncherActivity.AppLanguage.equalsIgnoreCase("ar") ? Article_writer.getAr().getName() : Article_writer.getEn().getName()));

                    } else{
                        Toast.makeText(context, "Failed load Article writer", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (t.getMessage().contains("Unable to resolve host"))
                    Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                            .setAction(R.string.go_to_setting, v -> context.startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)))
                            .show();
                else
                    Toast.makeText(context, "Failed load Article writer", Toast.LENGTH_SHORT).show();

            }
        });

    }
}