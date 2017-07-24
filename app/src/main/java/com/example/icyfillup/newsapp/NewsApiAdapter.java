package com.example.icyfillup.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.net.sip.SipAudioCall;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.icyfillup.newsapp.data.ArticleContract;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by icyfillup on 6/27/2017.
 */

public class NewsApiAdapter extends RecyclerView.Adapter<NewsApiAdapter.NewsApiAdapterViewHolder> {

    private OpenUrlLinkToBrowser OnClickListener;
    private Cursor cursor;

    public NewsApiAdapter(OpenUrlLinkToBrowser Listener, Cursor cursor) {
        OnClickListener = Listener;
        this.cursor = cursor;
    }

    public interface OpenUrlLinkToBrowser
    {
        void onItemClick(URL UriLink);
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
        if(!cursor.moveToPosition(position))
        {
            return;
        }

        String title = cursor.getString(cursor.getColumnIndex(ArticleContract.COLUMN_TITLE));
        String description = cursor.getString(cursor.getColumnIndex(ArticleContract.COLUMN_DESCRIPTION));
        String date = cursor.getString(cursor.getColumnIndex(ArticleContract.COLUMN_DATE));

        holder.NewsTitle.setText(title);
        holder.NewsDescription.setText(description);
        holder.NewsTime.setText(date);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (cursor != null) cursor.close();
        cursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
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
        public void onClick(View view)
        {
            int index = getAdapterPosition();
            cursor.moveToPosition(index);
            String articleUrl = cursor.getString(cursor.getColumnIndex(ArticleContract.COLUMN_URL));
            try {
                URL url = new URL(articleUrl);
                OnClickListener.onItemClick(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
    }
}
