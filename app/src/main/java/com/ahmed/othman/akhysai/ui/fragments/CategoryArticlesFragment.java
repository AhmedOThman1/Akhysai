package com.ahmed.othman.akhysai.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.RecyclerViewTouchListener;
import com.ahmed.othman.akhysai.adapter.ArticleAdapter;
import com.ahmed.othman.akhysai.pojo.Article;
import com.ahmed.othman.akhysai.pojo.BlogCategories;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.ahmed.othman.akhysai.ui.activities.LauncherActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.navigation_view;
import static com.ahmed.othman.akhysai.ui.activities.mainActivity.MainActivity.toolbar;


public class CategoryArticlesFragment extends Fragment {

    public CategoryArticlesFragment() {
        // Required empty public constructor
    }


    TextView category_title,category_description;
    RecyclerView category_articles_recycler;
    ShimmerFrameLayout category_articles_shimmer;
    ArrayList<Article> categoryArticles = new ArrayList<>();
    ArticleAdapter articleAdapter;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_category_articles, container, false);

        category_title = view.findViewById(R.id.category_title);
        category_description = view.findViewById(R.id.category_description);
        category_articles_recycler = view.findViewById(R.id.category_articles_recycler);
        category_articles_shimmer = view.findViewById(R.id.category_articles_shimmer);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE);
        category_articles_shimmer.startShimmer();
        category_articles_shimmer.setVisibility(View.VISIBLE);
        category_articles_recycler.setVisibility(View.GONE);


        toolbar.setVisibility(View.VISIBLE);
        navigation_view.setCheckedItem(R.id.nav_my_articles);

        articleAdapter = new ArticleAdapter(requireContext());

        Bundle arg = getArguments();
        if (arg != null) {
            String json = arg.getString("articles", "");
            Log.w("JSON_ARTICLES", json + "");
            if (!json.trim().isEmpty()) {
                BlogCategories category = new Gson().fromJson(json, BlogCategories.class);
                category_title.setText(category.getName().trim());
                category_description.setText(category.getDescription().trim());
                categoryArticles = new ArrayList<>(category.getArticles());
                articleAdapter.setArticles(categoryArticles);
                articleAdapter.notifyDataSetChanged();
                category_articles_recycler.setVisibility(View.VISIBLE);
                category_articles_shimmer.stopShimmer();
                category_articles_shimmer.hideShimmer();
                category_articles_shimmer.setVisibility(View.GONE);
            }
        }

        category_articles_recycler.setAdapter(articleAdapter);
        category_articles_recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        category_articles_recycler.setHasFixedSize(true);

        category_articles_recycler.addOnItemTouchListener(new RecyclerViewTouchListener(requireContext(), category_articles_recycler, new RecyclerViewTouchListener.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("article", new Gson().toJson(categoryArticles.get(position)));
                Navigation.findNavController(v).navigate(R.id.action_categoryArticlesFragment_to_oneArticleFragment, bundle);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return view;
    }


}