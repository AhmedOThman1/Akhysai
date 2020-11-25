package com.ahmed.othman.akhysai.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.RecyclerViewTouchListener;
import com.ahmed.othman.akhysai.adapter.ArticleAdapter;
import com.ahmed.othman.akhysai.pojo.Article;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.navigation_view;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;


public class MyArticlesFragment extends Fragment {

    public MyArticlesFragment() {
        // Required empty public constructor
    }

    
    RecyclerView my_articles_recycler;
    ArrayList<Article> myArticles = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_articles, container, false);

        myArticles = getArticlesByAkhysaiID();

        my_articles_recycler = view.findViewById(R.id.my_articles_recycler);

        toolbar.setVisibility(View.VISIBLE);
        navigation_view.setCheckedItem(R.id.nav_my_articles);

        ArticleAdapter articleAdapter = new ArticleAdapter(getContext());

        articleAdapter.setModels(myArticles);
        my_articles_recycler.setAdapter(articleAdapter);
        my_articles_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        my_articles_recycler.setHasFixedSize(true);

        my_articles_recycler.addOnItemTouchListener(new RecyclerViewTouchListener(getContext(), my_articles_recycler, new RecyclerViewTouchListener.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("id", myArticles.get(position).getArticle_id());
                bundle.putString("article", new Gson().toJson(myArticles.get(position)));
                Navigation.findNavController(v).navigate(R.id.action_myArticlesFragment_to_oneArticleFragment,bundle);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        view.findViewById(R.id.create_article).setOnClickListener(v->Navigation.findNavController(v).navigate(R.id.action_myArticlesFragment_to_createNewArticleFragment));

        return view;
    }

    private ArrayList<Article> getArticlesByAkhysaiID() {
        ArrayList<Article> myTempArticles = new ArrayList<>();
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DAY_OF_MONTH,-10);
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DAY_OF_MONTH,-1);
        Calendar c3 = Calendar.getInstance();
        c3.add(Calendar.DAY_OF_MONTH,-142);

        myTempArticles.add(new Article("", "مقالة عن التنمر", "الطب",c1.getTimeInMillis(),"تفاصيل طويلة عريضة",""));
        myTempArticles.add(new Article("", "مقالة عن التوحد", "الطب",c2.getTimeInMillis(),"تفاصيل طويلة عريضة تاني",""));
        myTempArticles.add(new Article("", "مقالة عن التخاطب", "ذووي الاحتياجات الخاصة",c3.getTimeInMillis(),"تفاصيل طويلة عريضة تالت انت لحقت تزهق",""));
        return myTempArticles;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment, true)
                        .setEnterAnim(R.anim.slide_in_right)
                        .setExitAnim(R.anim.slide_out_left)
                        .setPopEnterAnim(R.anim.slide_in_left)
                        .setPopExitAnim(R.anim.slide_out_right)
                        .build();
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.homeFragment, null, navOptions);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

}