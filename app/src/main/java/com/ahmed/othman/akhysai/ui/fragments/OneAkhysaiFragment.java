package com.ahmed.othman.akhysai.ui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.RecyclerViewTouchListener;
import com.ahmed.othman.akhysai.adapter.ArticleAdapter;
import com.ahmed.othman.akhysai.adapter.ReviewAdapter;
import com.ahmed.othman.akhysai.pojo.Akhysai;
import com.ahmed.othman.akhysai.pojo.Article;
import com.ahmed.othman.akhysai.pojo.Review;
import com.ahmed.othman.akhysai.ui.activities.MainActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Calendar;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.logged_in;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.shared_pref;
import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;

public class OneAkhysaiFragment extends Fragment {

    public OneAkhysaiFragment() {
        // Required empty public constructor
    }

    Akhysai currentAkhysai = new Akhysai();
    Context context;
    ReviewAdapter reviewAdapter;
    boolean dialogHidden = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one_akhysai, container, false);

        context = requireContext();

        RoundedImageView akhysai_image = view.findViewById(R.id.akhysai_image);
        TextView akhysai_name = view.findViewById(R.id.akhysai_name),
                akhysai_description = view.findViewById(R.id.akhysai_description),
                akhysai_years_of_experience = view.findViewById(R.id.akhysai_years_of_experience),
                visitors_rate_num = view.findViewById(R.id.visitors_rate_num),
                about_akhysai_body = view.findViewById(R.id.about_akhysai_body),
                akhysai_price = view.findViewById(R.id.akhysai_price);
        RatingBar akhysai_rating = view.findViewById(R.id.akhysai_rating);
        RecyclerView rates_recyclerview = view.findViewById(R.id.rates_recyclerview);
        RecyclerView akhysai_articles_recyclerview = view.findViewById(R.id.akhysai_articles_recyclerview);

        toolbar.setVisibility(View.VISIBLE);

        Bundle args = getArguments();
        if (args != null) {
            String json = args.getString("akhysai", "");
            if (!json.trim().isEmpty()) {
                currentAkhysai = new Gson().fromJson(json, Akhysai.class);
                currentAkhysai.setReviews(getReviewsByAkhysaiID(currentAkhysai.getAkhysai_id()));
                currentAkhysai.setArticles(getArticlesByAkhysaiID(currentAkhysai.getAkhysai_id()));
            }
            if(args.getBoolean("OpenWriteReviewDialog",false)){
                open_write_review_dialog();
                dialogHidden = false;
            }
        }


        Glide.with(context)
                .load(currentAkhysai.getPhoto())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.doctor_img2)
                .into(akhysai_image);

        akhysai_name.setText(currentAkhysai.getName());

        akhysai_description.setText(currentAkhysai.getDescription());

        about_akhysai_body.setText(currentAkhysai.getAbout_akhysai());


        if (currentAkhysai.getReviews().isEmpty()) {
            //TODO show image no reviews found
        }

        if (currentAkhysai.getArticles().isEmpty()) {
            //TODO show image no articles found
        } else {
            //TODO hide image no articles found

            ArticleAdapter articleAdapter = new ArticleAdapter(requireContext());

            articleAdapter.setModels(currentAkhysai.getArticles());
            akhysai_articles_recyclerview.setAdapter(articleAdapter);
            akhysai_articles_recyclerview.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
            akhysai_articles_recyclerview.setHasFixedSize(true);

            akhysai_articles_recyclerview.addOnItemTouchListener(new RecyclerViewTouchListener(requireContext(), akhysai_articles_recyclerview, new RecyclerViewTouchListener.RecyclerViewClickListener() {
                @Override
                public void onClick(View v, int position) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", currentAkhysai.getArticles().get(position).getArticle_id());
                    bundle.putBoolean("from_akhysai_profile",true);
                    bundle.putString("article", new Gson().toJson(currentAkhysai.getArticles().get(position)));
                    Navigation.findNavController(v).navigate(R.id.action_oneAkhysaiFragment_to_oneArticleFragment,bundle);
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

        }


        view.findViewById(R.id.profile_second_card).setVisibility(currentAkhysai.getAbout_akhysai().trim().isEmpty() ? View.GONE : View.VISIBLE);


        String temp = context.getResources().getString(R.string.years_of_experience2) + ": " + currentAkhysai.getExperience_years() + context.getResources().getString(R.string.years);
        akhysai_years_of_experience.setText(temp);

        akhysai_rating.setRating(currentAkhysai.getRate());
        akhysai_rating.setIsIndicator(true);

        temp = context.getResources().getString(R.string.this_rate_from) + currentAkhysai.getVisitor_num() + context.getResources().getString(R.string.visitor);
        visitors_rate_num.setText(temp);

        temp = context.getResources().getString(R.string.session_price) + currentAkhysai.getPrice() + context.getResources().getString(R.string.egp);
        akhysai_price.setText(temp);

        view.findViewById(R.id.book_akhysai).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", currentAkhysai.getAkhysai_id());
            bundle.putString("akhysai", new Gson().toJson(currentAkhysai));
            Navigation.findNavController(v).navigate(R.id.action_oneAkhysaiFragment_to_bookOneAkhysaiFragment, bundle);
        });

        view.findViewById(R.id.write_a_review).setOnClickListener(v -> {
            if (requireActivity().getSharedPreferences(shared_pref, Context.MODE_PRIVATE).getBoolean(logged_in, false) && dialogHidden) {
                open_write_review_dialog();
                dialogHidden = false;
            } else if (dialogHidden)
            {
                Bundle bundle = new Bundle();
                bundle.putString("goTo","oneAkhysaiFragmntWriteReview");
                bundle.putString("akhysai",new Gson().toJson(currentAkhysai));
                Navigation.findNavController(v).navigate(R.id.action_oneAkhysaiFragment_to_loginFragment,bundle);
            }

        });

        reviewAdapter = new ReviewAdapter(context);
        reviewAdapter.setModels(currentAkhysai.getReviews());
        rates_recyclerview.setAdapter(reviewAdapter);
        rates_recyclerview.setLayoutManager(new LinearLayoutManager(context));

        return view;
    }

    private ArrayList<Review> getReviewsByAkhysaiID(String akhysai_id) {
        //TODO delete this
        ArrayList<Review> tempReviews = new ArrayList<>();
        Calendar tempCal1 = Calendar.getInstance();
        Calendar tempCal2 = Calendar.getInstance();
        tempCal2.add(Calendar.DAY_OF_MONTH, -1);
        Calendar tempCal3 = Calendar.getInstance();
        tempCal3.add(Calendar.DAY_OF_MONTH, -2);
        Calendar tempCal4 = Calendar.getInstance();
        tempCal4.add(Calendar.DAY_OF_MONTH, -7);
        Calendar tempCal5 = Calendar.getInstance();
        tempCal5.add(Calendar.DAY_OF_MONTH, -16);
        tempReviews.add(new Review("دكتور ممتاز جداا انصح به", 5, tempCal1.getTimeInMillis(), "Ahmed Othman"));
        tempReviews.add(new Review("خلوق و محترم و فاهم بيعمل اي", 5, tempCal2.getTimeInMillis(), "Asma Adel"));
        tempReviews.add(new Review("دكتور ممتاز جدا ساعدني بمنتهي البراعة و اساليب غير تقليدية متحمس جدا للجلسات القادمة باذن الله", (float) 4.5, tempCal3.getTimeInMillis(), "Ziad Sakr"));
        tempReviews.add(new Review("دكتور رائع و ذو علم و معرفة و خلوق و مستمع جيد باختصار ع ظ م ه", (float) 3.5, tempCal4.getTimeInMillis(), "Abdel allah"));
        tempReviews.add(new Review("جزاه الله كل خير فهو مستمع جيد جدا جدا بس برضو هعطيه نجمة واحده عشان التيست", 1, tempCal5.getTimeInMillis(), "Rizk"));
        return tempReviews;
        // End of TODO
    }

    private ArrayList<Article> getArticlesByAkhysaiID(String akhysai_id) {
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

    AlertDialog dialog;

    private void open_write_review_dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View write_review_dialog = getLayoutInflater().inflate(R.layout.write_review_dialog, null);
        TextInputLayout review = write_review_dialog.findViewById(R.id.review);
        RatingBar akhysai_rating = write_review_dialog.findViewById(R.id.akhysai_rating);

        review.requestFocus();
        open_keyboard(review.getEditText());

        write_review_dialog.findViewById(R.id.cancel_review).setOnClickListener(v -> {
            dialog.dismiss();
            dialogHidden = true;
        });
        write_review_dialog.findViewById(R.id.publish_review).setOnClickListener(v -> {
            if (akhysai_rating.getRating() < 0.5) {
                Toast.makeText(context, "Choose rate first", Toast.LENGTH_SHORT).show();
            } else if (review.getEditText().getText().toString().trim().isEmpty()) {
                review.setError("Can't be empty");
                review.requestFocus();
                open_keyboard(review.getEditText());
            } else {
                review.setError(null);
                review.clearFocus();
                Review newReview = new Review(review.getEditText().getText().toString().trim(),
                        akhysai_rating.getRating(),
                        System.currentTimeMillis(),
                        context.getSharedPreferences(MainActivity.shared_pref, Context.MODE_PRIVATE).getString(MainActivity.full_name, ""));
                currentAkhysai.getReviews().add(newReview);

                postReview(newReview);
                reviewAdapter.notifyItemInserted(currentAkhysai.getReviews().size() - 1);
                dialog.dismiss();
                dialogHidden = true;
            }
        });
        builder.setView(write_review_dialog).setCancelable(false);
        dialog = builder.create();
        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void postReview(Review newReview) {
        Toast.makeText(context, "Review added successfully", Toast.LENGTH_SHORT).show();
    }


    private void open_keyboard(EditText textInputLayout) {
        textInputLayout.requestFocus();     // editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);     // Context.INPUT_METHOD_SERVICE
        assert imm != null;
        imm.showSoftInput(textInputLayout, InputMethodManager.SHOW_IMPLICIT); //    first param -> editText

    }

}