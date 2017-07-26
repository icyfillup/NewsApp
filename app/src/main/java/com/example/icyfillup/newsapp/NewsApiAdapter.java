package com.example.icyfillup.newsapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.icyfillup.newsapp.data.ArticleContract;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by icyfillup on 6/27/2017.
 */

public class NewsApiAdapter extends RecyclerView.Adapter<NewsApiAdapter.NewsApiAdapterViewHolder> {

    private OpenUrlLinkToBrowser OnClickListener;
    private Cursor cursor;
    private Context context;

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
        context = viewGroup.getContext();
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
        String thumbUrl = cursor.getString(cursor.getColumnIndex(ArticleContract.COLUMN_THUMB_URL));

        holder.NewsTitle.setText(title);
        holder.NewsDescription.setText(description);
        holder.NewsTime.setText(date);

        Log.d("onBindViewHolder:", thumbUrl);
        if(thumbUrl != null)
        {
            // display the thumbnail image in the view
            Picasso.with(context).load(thumbUrl).into(holder.NewsImg);
        }

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    class NewsApiAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView NewsTitle;
        TextView NewsDescription;
        TextView NewsTime;
        ImageView NewsImg;


        public NewsApiAdapterViewHolder(View itemView) {
            super(itemView);

            NewsTitle = (TextView) itemView.findViewById(R.id.news_title);
            NewsDescription = (TextView) itemView.findViewById(R.id.news_description);
            NewsTime = (TextView) itemView.findViewById(R.id.news_time);
            NewsImg = (ImageView) itemView.findViewById(R.id.news_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view)
        {
            // retrives the clicked article's url and sends it to the OnClickListener.onItemClick
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
