package com.ahmed.othman.akhysai.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.RecyclerViewTouchListener;
import com.ahmed.othman.akhysai.adapter.ArticleAdapter;
import com.ahmed.othman.akhysai.network.ApiClient;
import com.ahmed.othman.akhysai.pojo.Article;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.Token;
import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.navigation_view;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;


public class MyArticlesFragment extends Fragment {

    public MyArticlesFragment() {
        // Required empty public constructor
    }


    RecyclerView my_articles_recycler;
    ShimmerFrameLayout my_articles_shimmer;
    FloatingActionButton create_article;
    ArrayList<Article> myArticles = new ArrayList<>();
    ArticleAdapter articleAdapter;
    boolean selectMode = false;
    String UserToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_articles, container, false);

        my_articles_recycler = view.findViewById(R.id.my_articles_recycler);
        my_articles_shimmer = view.findViewById(R.id.my_articles_shimmer);
        create_article = view.findViewById(R.id.create_article);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE);
        UserToken = sharedPreferences.getString(Token, "");
        LauncherActivity.akhysaiViewModel.getAkhysaiArticles("Bearer " + UserToken);
        my_articles_shimmer.startShimmer();
        my_articles_shimmer.setVisibility(View.VISIBLE);
        my_articles_recycler.setVisibility(View.GONE);


        toolbar.setVisibility(View.VISIBLE);
        navigation_view.setCheckedItem(R.id.nav_my_articles);

        articleAdapter = new ArticleAdapter(requireContext());


        my_articles_recycler.setAdapter(articleAdapter);
        my_articles_recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        my_articles_recycler.setHasFixedSize(true);

        my_articles_recycler.addOnItemTouchListener(new RecyclerViewTouchListener(requireContext(), my_articles_recycler, new RecyclerViewTouchListener.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (!selectMode) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", myArticles.get(position).getArticleId());
                    bundle.putString("article", new Gson().toJson(myArticles.get(position)));
                    Navigation.findNavController(v).navigate(R.id.action_myArticlesFragment_to_oneArticleFragment, bundle);
                } else {
                    articleAdapter.getArticles().get(position).setSelected(!articleAdapter.getArticles().get(position).isSelected());
                    articleAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                selectMode = true;
                articleAdapter.getArticles().get(position).setSelected(!articleAdapter.getArticles().get(position).isSelected());
                articleAdapter.notifyItemChanged(position);
                create_article.setImageResource(R.drawable.ic_delete);
                create_article.setBackgroundTintList(ColorStateList.valueOf(requireActivity().getResources().getColor(R.color.error_red)));
            }
        }));


        create_article.setOnClickListener(v -> {
            if (selectMode) {
                selectMode = false;
                myArticles = articleAdapter.getArticles();
                for (int i = 0; i < myArticles.size(); i++) {
                    final int FinalI = i;
                    if (myArticles.get(i).isSelected()) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("article_id", myArticles.get(i).getArticleId());
                        ApiClient.getINSTANCE().DeleteArticle("Bearer " + UserToken, jsonObject).enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().get("status").getAsString().equals("success")) {
                                        Toast.makeText(requireContext(), "Article deleted successfully", Toast.LENGTH_SHORT).show();
                                        myArticles.remove(FinalI);
                                        articleAdapter.notifyItemRemoved(FinalI);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                if (t.getMessage().contains("Unable to resolve host"))
                                    Snackbar.make(view, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
                                            .setAction(R.string.go_to_setting, v -> requireContext().startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)))
//                            .setActionTextColor(Color.WHITE)
                                            .show();
                                else
                                    Toast.makeText(requireContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            } else
                Navigation.findNavController(v).navigate(R.id.action_myArticlesFragment_to_createNewArticleFragment);
        });

        LauncherActivity.akhysaiViewModel.akhysaiArticlesMutableLiveData.observe(requireActivity(), articles -> {
            if (articles != null) {
                myArticles = new ArrayList<>(articles);
                articleAdapter.setArticles(myArticles);
                articleAdapter.notifyDataSetChanged();
                my_articles_recycler.setVisibility(View.VISIBLE);
                my_articles_shimmer.stopShimmer();
                my_articles_shimmer.hideShimmer();
                my_articles_shimmer.setVisibility(View.GONE);
            }
        });

        return view;
    }

//    private ArrayList<Article> getCurrentAkhysaiArticles() {
//
//
////        ArrayList<Article> myTempArticles = new ArrayList<>();
////        Calendar c1 = Calendar.getInstance();
////        c1.add(Calendar.DAY_OF_MONTH,-10);
////        Calendar c2 = Calendar.getInstance();
////        c2.add(Calendar.DAY_OF_MONTH,-1);
////        Calendar c3 = Calendar.getInstance();
////        c3.add(Calendar.DAY_OF_MONTH,-142);
////
////        myTempArticles.add(new Article("", "مقالة عن التنمر", "الطب",c1.getTimeInMillis(),"تفاصيل طويلة عريضة",""));
////        myTempArticles.add(new Article("", "مقالة عن التوحد", "الطب",c2.getTimeInMillis(),"تفاصيل طويلة عريضة تاني",""));
////        myTempArticles.add(new Article("", "مقالة عن التخاطب", "ذووي الاحتياجات الخاصة",c3.getTimeInMillis(),"تفاصيل طويلة عريضة تالت انت لحقت تزهق",""));
////
//        return myTempArticles;
//    }

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

                if (selectMode) {
                    selectMode = false;
                    create_article.setImageResource(R.drawable.ic_create_article);
                    create_article.setBackgroundTintList(ColorStateList.valueOf(requireActivity().getResources().getColor(R.color.colorAccent)));
                    for (int i = 0; i < myArticles.size(); i++) {
                        myArticles.get(i).setSelected(false);
                    }
                    articleAdapter.setArticles(myArticles);
                    articleAdapter.notifyDataSetChanged();
                } else
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).popBackStack(R.id.homeFragment, false);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

}