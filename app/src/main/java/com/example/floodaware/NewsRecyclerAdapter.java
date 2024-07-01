package com.example.floodaware;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder>{

    List<Article> articleList;

    public NewsRecyclerAdapter(List<Article> articleList) {
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_row,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.titleTV.setText(article.getTitle());
        holder.sourceTV.setText(article.getSource().getName());
        Picasso.get().load(article.getUrlToImage()).error(R.drawable.no_image).placeholder(R.drawable.no_image).into(holder.newsIV);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(),NewsScreenActivity.class);
            intent.putExtra("url",article.getUrl());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public void updateData(List<Article> data){
        articleList.clear();
        articleList.addAll(data);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{

        TextView titleTV,sourceTV;
        ImageView newsIV;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.article_title);
            sourceTV = itemView.findViewById(R.id.article_source);
            newsIV = itemView.findViewById(R.id.newsIV);
        }
    }
}
