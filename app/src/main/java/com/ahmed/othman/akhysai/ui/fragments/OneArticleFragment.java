package com.ahmed.othman.akhysai.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.Akhysai;
import com.ahmed.othman.akhysai.pojo.Article;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Calendar;

import static com.ahmed.othman.akhysai.ui.activities.MainActivity.toolbar;


public class OneArticleFragment extends Fragment {


    public OneArticleFragment() {
        // Required empty public constructor
    }
    RoundedImageView article_item_image,article_writer_image;
    TextView article_item_title,article_item_category,article_item_time,article_item_body, article_writer_name;
    Article article = new Article();
    Context context;
    Akhysai Article_writer;
    boolean fromAkhysiaProfile = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one_article, container, false);
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
        if(args!=null){
            fromAkhysiaProfile = args.getBoolean("from_akhysai_profile",false);
            String json = args.getString("article","");
            Log.w("JSON_ARTICLE",json+"");
            if(!json.trim().isEmpty())
            {
                article = new Gson().fromJson(json, Article.class);
                Article_writer = getArticleWriterData(article.getArticle_writer_id());
            }
        }


        Glide.with(context)
                .load(article.getImage())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.akhysai_logo)
                .into(article_item_image);

        article_item_title.setText(article.getTitle());

        article_item_category.setText(article.getCategory().contains(context.getResources().getString(R.string.category)) ? article.getCategory() : context.getResources().getString(R.string.category) + article.getCategory());

        article_item_body.setText(article.getBody());

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(article.getDate());
        article_item_time.setText(c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH));

        Glide.with(context)
                .load(Article_writer.getPhoto())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.doctor_img2)
                .into(article_writer_image);

        article_writer_name.setText(String.valueOf(Article_writer.getName()));

        view.findViewById(R.id.writer_card).setOnClickListener(v->{
            if(fromAkhysiaProfile)
                requireActivity().onBackPressed();
            else{
                Bundle bundle = new Bundle();
                bundle.putString("akhysai", new Gson().toJson(Article_writer));
                Navigation.findNavController(v).navigate(R.id.action_oneArticleFragment_to_oneAkhysaiFragment,bundle);
            }
        });

        return view;
    }

    private Akhysai getArticleWriterData(String article_writer_id) {

        //TODO delete this
        return new Akhysai("", "احمد عثمان", "طبيب جراح قلوب الناس اداويها", "محاضر معتمد من نقابة الاطباء\n دبلوم تجميل و ليزر و دبلومة مكافحة عدوي جودة", 6, (float) 4.5, 42, 620);
    }
}