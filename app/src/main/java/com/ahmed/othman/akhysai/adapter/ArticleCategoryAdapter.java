package com.ahmed.othman.akhysai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.Article;
import com.ahmed.othman.akhysai.pojo.BlogCategories;
import com.ahmed.othman.akhysai.ui.activities.LauncherActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArticleCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<BlogCategories> BlogCategories = new ArrayList<>();

    public ArticleCategoryAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setBlogCategories(List<BlogCategories> blogCategories) {
        BlogCategories = blogCategories;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_article_category_item, parent, false);
        return new ArticleCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            final ArticleCategoryViewHolder ViewHolder = (ArticleCategoryViewHolder) holder;

            Glide.with(context)
                    .load(LauncherActivity.ImagesLink + BlogCategories.get(position).getPhoto())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.background_shimmer)
                    .into(ViewHolder.article_category_item_image);

        ViewHolder.article_category_articles_count.setText(BlogCategories.get(position).getArticles_count() + " "+context.getResources().getString(R.string.article));

        ViewHolder.article_category_item_title.setText(BlogCategories.get(position).getName().trim());

        ViewHolder.article_category_item_description.setText(BlogCategories.get(position).getDescription().trim());
    }


    @Override
    public int getItemCount() {
        return BlogCategories.size();
    }

    static class ArticleCategoryViewHolder extends RecyclerView.ViewHolder {
        // views
        RoundedImageView article_category_item_image;
        TextView article_category_articles_count,article_category_item_title, article_category_item_description;

        public ArticleCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            article_category_item_image = itemView.findViewById(R.id.article_category_item_image);
            article_category_articles_count = itemView.findViewById(R.id.article_category_articles_count);
            article_category_item_title = itemView.findViewById(R.id.article_category_item_title);
            article_category_item_description = itemView.findViewById(R.id.article_category_item_description);
        }
    }

}
