package com.ahmed.othman.akhysai.adapter;

import androidx.annotation.NonNull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmed.othman.akhysai.R;
import com.ahmed.othman.akhysai.pojo.Article;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Article> Models;
    private Article current_model;

    public ArticleAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setModels(ArrayList<Article> models) {
        Models = models;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_article_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        current_model = Models.get(position);

        final ArticleViewHolder ViewHolder = (ArticleViewHolder) holder;

        ViewHolder.article_item_title.setText(current_model.getTitle());

        ViewHolder.article_item_category.setText(current_model.getCategory().contains(context.getResources().getString(R.string.category)) ? current_model.getCategory() : context.getResources().getString(R.string.category) + current_model.getCategory());

        Glide.with(context)
                .load(current_model.getImage())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.akhysai_logo)
                .into(ViewHolder.article_item_image);
    }

    @Override
    public int getItemCount() {
        return Models.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        // views
        CircleImageView article_item_image;
        TextView article_item_title, article_item_category;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            article_item_image = itemView.findViewById(R.id.article_item_image);
            article_item_title = itemView.findViewById(R.id.article_item_title);
            article_item_category = itemView.findViewById(R.id.article_item_category);
        }
    }

}
