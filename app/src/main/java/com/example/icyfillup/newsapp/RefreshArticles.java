package com.example.icyfillup.newsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.icyfillup.newsapp.data.ArticleContract;
import com.example.icyfillup.newsapp.data.ArticleDbHelper;
import com.example.icyfillup.newsapp.utilities.DatabaseUtils;
import com.example.icyfillup.newsapp.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by icyfillup on 7/22/2017.
 */

public class RefreshArticles
{
    private static final String TAG = RefreshArticles.class.getSimpleName();

    public static void refreshArticles(Context context)
    {
        SQLiteDatabase articleDB = new ArticleDbHelper(context).getWritableDatabase();
        articleDB.delete(ArticleContract.TABLE_NAME, null, null);

        ArrayList<NewsItem> result = null;
        String JsonNewsApiResponse = null;

        URL NewsApiUrl = NetworkUtils.buildUrl();
        try
        {
            JsonNewsApiResponse = NetworkUtils.getResponseFromHttpUrl(NewsApiUrl);
            result = NetworkUtils.getNewsItemsFromJson(JsonNewsApiResponse);
            DatabaseUtils.bulkInsert(articleDB, result);
            Log.d(TAG, JsonNewsApiResponse);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
    }
}
