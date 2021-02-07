package com.ahmed.othman.akhysai.adapter;

import androidx.annotation.NonNull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.Article;
import com.ahmed.othman.akhysai.pojo.BlogCategories;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Article> Articles = new ArrayList<>();

    private List<BlogCategories> blogCategories = new ArrayList<>();

    public ArticleAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setArticles(ArrayList<Article> articles) {
        Articles = articles;
    }

    public ArrayList<Article> getArticles() {
        return Articles;
    }

    public void setBlogCategories(List<BlogCategories> blogCategories) {
        this.blogCategories = blogCategories;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_article_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (!Articles.isEmpty()) {
            Article current_model = Articles.get(position);

            final ArticleViewHolder ViewHolder = (ArticleViewHolder) holder;

            ViewHolder.article_item_title.setText(current_model.getTitle());

            ViewHolder.article_item_category.setText(context.getResources().getString(R.string.category) + getCategoryNameById(current_model.getCategoryId()));

            Glide.with(context)
                    .load(LauncherActivity.ImagesLink + current_model.getPicture())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.akhysai_logo)
                    .into(ViewHolder.article_item_image);

            if (Articles.get(position).isSelected())
                ViewHolder.constrain_select_background.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            else
                ViewHolder.constrain_select_background.setBackgroundColor(context.getResources().getColor(R.color.white));

        } else {

            BlogCategories category = blogCategories.get(position);

            final ArticleViewHolder ViewHolder = (ArticleViewHolder) holder;

            ViewHolder.article_item_title.setText(category.getName());

//            ViewHolder.article_item_category.setText(category.getcontext.getResources().getString(R.string.category) + getCategoryNameById(current_model.getCategoryId()));

            Glide.with(context)
                    .load(LauncherActivity.ImagesLink + category.getPhoto())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.akhysai_logo)
                    .into(ViewHolder.article_item_image);
        }
    }

    private String getCategoryNameById(String Id) {
        for (int i = 0; i < LauncherActivity.BlogCategories.size(); i++) {
            if (String.valueOf(LauncherActivity.BlogCategories.get(i).getId()).equalsIgnoreCase(Id))
                return LauncherActivity.BlogCategories.get(i).getName();
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return Articles.size();
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        // views
        CircleImageView article_item_image;
        TextView article_item_title, article_item_category;
        ConstraintLayout constrain_select_background;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            article_item_image = itemView.findViewById(R.id.article_item_image);
            article_item_title = itemView.findViewById(R.id.article_item_title);
            article_item_category = itemView.findViewById(R.id.article_item_category);
            constrain_select_background = itemView.findViewById(R.id.constrain_select_background);
        }
    }

}
