package com.example.gerardus.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsItemViewHolder>{

    Context context_holder;

    public ArrayList<NewsItem> news;

    public NewsAdapter(ArrayList<NewsItem> news, newsListener listen) {
//        this.context_holder = context_holder;
        this.news = news;
        this.onClickListener = listen;
    }
    public interface newsListener{
        void onListItemClick(int clickedItemIndex);
    }
    final private newsListener onClickListener;

    public ArrayList<NewsItem> getNews() {
        return news;
    }

    public void setNews(ArrayList<NewsItem> news) {
        this.news = news;
    }



    @NonNull
    @Override
    public NewsAdapter.NewsItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.news_item, viewGroup, shouldAttachToParentImmediately);
        return new NewsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsItemViewHolder newsViewHolder, int i) {
        newsViewHolder.title.setText("Title" + news.get(i).getTitle());
        newsViewHolder.description.setText("Description" + news.get(i).getDescription());
        newsViewHolder.date.setText("Date" + news.get(i).getPublishedAt());
//        newsViewHolder.itemView.setOnClickListener(newsViewHolder);

                                                    // TODO this ?
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public class NewsItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        TextView title,description,date;



        public  NewsItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            date = (TextView) itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int clickedPostition = getAdapterPosition();
          onClickListener.onListItemClick(clickedPostition);
        }
    }

}
