package com.example.icyfillup.newsapp;

import android.content.Context;
import android.net.sip.SipAudioCall;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by icyfillup on 6/27/2017.
 */

public class NewsApiAdapter extends RecyclerView.Adapter<NewsApiAdapter.NewsApiAdapterViewHolder> {

    private ArrayList<NewsItem> NewsArticles;
    private OpenUrlLinkToBrowser OnClickListener;

    public NewsApiAdapter(OpenUrlLinkToBrowser Listener) {
        OnClickListener = Listener;
    }

    public interface OpenUrlLinkToBrowser
    {
        void onItemClick();
    }

    @Override
    public NewsApiAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(R.layout.news_api, viewGroup, shouldAttachToParentImmediately);
        NewsApiAdapterViewHolder viewHolder = new NewsApiAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsApiAdapterViewHolder holder, int position) {
        holder.NewsTitle.setText(NewsArticles.get(position).getTitle());
        holder.NewsDescription.setText(NewsArticles.get(position).getDescription());
        holder.NewsTime.setText(NewsArticles.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        if(NewsArticles == null)
        {
            return 0;
        }
        return NewsArticles.size();
    }

    public void setNewsArticles(ArrayList<NewsItem> Articles)
    {
        NewsArticles = Articles;
        notifyDataSetChanged();
    }


    class NewsApiAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final public TextView NewsTitle;
        final public TextView NewsDescription;
        final public TextView NewsTime;


        public NewsApiAdapterViewHolder(View itemView) {
            super(itemView);

            NewsTitle = (TextView) itemView.findViewById(R.id.news_title);
            NewsDescription = (TextView) itemView.findViewById(R.id.news_description);
            NewsTime = (TextView) itemView.findViewById(R.id.news_time);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            OnClickListener.onItemClick();
        }
    }
}
