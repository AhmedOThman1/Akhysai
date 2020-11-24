package com.ahmed.othman.akhysai.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.RecyclerViewTouchListener;
import com.ahmed.othman.akhysai.adapter.ArticleAdapter;
import com.ahmed.othman.akhysai.pojo.Article;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.navigation_view;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;


public class LibraryArticlesFragment extends Fragment {

    public LibraryArticlesFragment() {
        // Required empty public constructor
    }

    TextInputLayout search;
    Spinner search_category;
    RecyclerView library_articles_recycler;
    ArrayList<Article> homeArticles = new ArrayList<>();
    TextView akhysai_library_text;
    NestedScrollView nested_scroll;
    ConstraintLayout no_result, no_internet;

    List<String> Categories = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library_articles, container, false);

        getArticlesCategories();
        homeArticles = getAllArticles();

        search = view.findViewById(R.id.search);
        search_category = view.findViewById(R.id.search_category);

        library_articles_recycler = view.findViewById(R.id.library_articles_recycler);
        nested_scroll = view.findViewById(R.id.nested_scroll);
        akhysai_library_text = view.findViewById(R.id.akhysai_library_text);
        no_result = view.findViewById(R.id.no_result);
        no_internet = view.findViewById(R.id.no_internet);

        toolbar.setVisibility(View.VISIBLE);
        navigation_view.setCheckedItem(R.id.akhysai_library);

        ArticleAdapter articleAdapter = new ArticleAdapter(getContext());


        articleAdapter.setModels(homeArticles);
        library_articles_recycler.setAdapter(articleAdapter);
        library_articles_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        library_articles_recycler.setHasFixedSize(true);

        library_articles_recycler.addOnItemTouchListener(new RecyclerViewTouchListener(getContext(), library_articles_recycler, new RecyclerViewTouchListener.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("id", homeArticles.get(position).getArticle_id());
                bundle.putString("article", new Gson().toJson(homeArticles.get(position)));
                Navigation.findNavController(v).navigate(R.id.action_libraryArticlesFragment_to_oneArticleFragment,bundle);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        ArrayAdapter<String> field_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Categories);
        field_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        search_category.setAdapter(field_adapter);


        view.findViewById(R.id.library_search_button).setOnClickListener(v -> {
            if (search.getEditText().getText().toString().trim().isEmpty()) {
                search.setError("Can't be empty");
                search.requestFocus();
                open_keyboard(search.getEditText());
            } else if (search_category.getSelectedItemPosition() == 0) {
                search.setError(null);
                search.clearFocus();
                Toast.makeText(getContext(), "select category", Toast.LENGTH_SHORT).show();
                search_category.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner_error));
            } else {
                search.setError(null);
                search_category.setBackground(getActivity().getResources().getDrawable(R.drawable.background_spinner));
                nested_scroll.smoothScrollTo(0, akhysai_library_text.getTop());
                getArticlesByTitle(search.getEditText().getText().toString().trim(), Categories.get(search_category.getSelectedItemPosition()));
            }
        });


        return view;
    }

    private void getArticlesCategories() {
        Categories = Arrays.asList(getContext().getResources().getStringArray(R.array.category));
    }

    private ArrayList<Article> getAllArticles() {
        ArrayList<Article> tempArticles = new ArrayList<>();
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DAY_OF_MONTH, -10);
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DAY_OF_MONTH, -1);
        Calendar c3 = Calendar.getInstance();
        c3.add(Calendar.DAY_OF_MONTH, -142);

        tempArticles.add(new Article("", "مقالة عن التنمر", "الطب", c1.getTimeInMillis(), "تفاصيل طويلة عريضة", ""));
        tempArticles.add(new Article("", "مقالة عن التوحد", "الطب", c2.getTimeInMillis(), "تفاصيل طويلة عريضة تاني", ""));
        tempArticles.add(new Article("", "مقالة عن التخاطب", "ذووي الاحتياجات الخاصة", c3.getTimeInMillis(), "تفاصيل طويلة عريضة تالت انت لحقت تزهق", ""));
        return tempArticles;
    }

    private ArrayList<Article> getArticlesByTitle(String title, String category) {
        ArrayList<Article> tempArticles = new ArrayList<>();
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DAY_OF_MONTH, -10);
        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DAY_OF_MONTH, -1);
        Calendar c3 = Calendar.getInstance();
        c3.add(Calendar.DAY_OF_MONTH, -142);
        tempArticles.add(new Article("", "مقالة عن التخاطب", "ذووي الاحتياجات الخاصة", c1.getTimeInMillis(), "تفاصيل طويلة عريضة تالت انت لحقت تزهق", ""));
        tempArticles.add(new Article("", title, category, c2.getTimeInMillis(), "تفاصيل طويلة عريضة", ""));
        tempArticles.add(new Article("", "مقالة عن التوحد", "الطب", c3.getTimeInMillis(), "تفاصيل طويلة عريضة تاني", ""));
        return tempArticles;
    }

    private void open_keyboard(EditText textInputLayout) {
        textInputLayout.requestFocus();     // editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);     // Context.INPUT_METHOD_SERVICE
        assert imm != null;
        imm.showSoftInput(textInputLayout, InputMethodManager.SHOW_IMPLICIT); //    first param -> editText
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