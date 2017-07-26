package com.example.icyfillup.newsapp.utilities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.icyfillup.newsapp.NewsItem;
import com.example.icyfillup.newsapp.data.ArticleContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by icyfillup on 7/22/2017.
 */

public class DatabaseUtils
{
    public static Cursor getAllArticles(SQLiteDatabase db)
    {
        return db.query(ArticleContract.TABLE_NAME, null, null, null, null, null, null);
    }

    public static void bulkInsert(SQLiteDatabase db, ArrayList<NewsItem> articles)
    {
        db.beginTransaction();
        try
        {
            for(NewsItem story: articles)
            {
                ContentValues cv = new ContentValues();
                cv.put(ArticleContract.COLUMN_TITLE, story.getTitle());
                cv.put(ArticleContract.COLUMN_DESCRIPTION, story.getDescription());
                cv.put(ArticleContract.COLUMN_DATE, story.getDate());
                cv.put(ArticleContract.COLUMN_URL, story.getUrl().toString());
                cv.put(ArticleContract.COLUMN_THUMB_URL, story.getThumbUrl());
                db.insert(ArticleContract.TABLE_NAME, null, cv);
            }

            db.setTransactionSuccessful();
        }
        finally
        {
            db.endTransaction();
            db.close();
        }
    }
}
