package com.example.icyfillup.newsapp.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.icyfillup.newsapp.NewsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static android.content.ContentValues.TAG;

/**
 * Created by icyfillup on 6/20/2017.
 */

public class NetworkUtils
{
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private final static String BASE_URL =
            "https://newsapi.org/v1/articles";

    private final static String PARAM_SOURCE = "source";
    private final static String source = "the-next-web";

    private final static String PARAM_SORT = "sortby";
    private final static String sort = "latest";

    private final static String PARAM_API_KEY = "apiKey";
    // NOTE: add your api key in the apiKey variable. For safety reason, i will not post my api key in here since the homework and, most importantly, my key would have been posted up on github
    private final static String apiKey = "Put your api key here";

    public static URL buildUrl()
    {
        // SearchQuery is not going to be used to build the link
        Uri uri = Uri.parse(BASE_URL).buildUpon().
                appendQueryParameter(PARAM_SOURCE, source).
                appendQueryParameter(PARAM_SORT, sort).
                appendQueryParameter(PARAM_API_KEY, apiKey).
                build();

        URL url = null;
        try
        {
            url = new URL(uri.toString());
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        Log.d(TAG, "buildUrl: " + uri.toString());
        //Log.d(TAG, "buildUrl: " + url.toString());
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static ArrayList<NewsItem> getNewsItemsFromJson(String NewsItemsFromString) throws JSONException {
        final String NIA_STATUS = "status";
        final String NIA_ERROR_CODE = "code";
        final String NIA_ARTICLES = "articles";
        final String NIA_TITLE = "title";
        final String NIA_DESCRIPTION = "description";
        final String NIA_TIME_AT = "publishedAt";
        final String NIA_THUMB_URL = "urlToImage";
        final String NIA_URL = "url";


        ArrayList<NewsItem> Result = new ArrayList<NewsItem>();

        // make string JSON into actual json
        JSONObject NewsItemsJson = new JSONObject(NewsItemsFromString);

        if(NewsItemsJson.has(NIA_STATUS))
        {
            String errorMessage = NewsItemsJson.getString(NIA_STATUS);
            switch(errorMessage)
            {
                case "ok":
                    break;
                case "error":
                    String errorCode = NewsItemsJson.getString(NIA_ERROR_CODE);
                    Log.d(TAG, "getNewsItemsFromJson: Error Code->" + errorCode);
                    return null;
                default :
                    return null;
            }
        }

        // get list of articles from the json file
        JSONArray ArticlesJSON = NewsItemsJson.getJSONArray(NIA_ARTICLES);
        int NumOfArticles = ArticlesJSON.length();

        //iterate through each article to get certain news information
        for(int i = 0; i < NumOfArticles; i++)
        {
            JSONObject Article = ArticlesJSON.getJSONObject(i);
            String title = Article.getString(NIA_TITLE);
            String description = Article.getString(NIA_DESCRIPTION);
            String date = Article.getString(NIA_TIME_AT);
            URL url = null;
            String thumbUrl = Article.getString(NIA_THUMB_URL);

            try
            {
                url = new URL(Article.getString(NIA_URL));
            }catch(MalformedURLException e)
            {
                e.printStackTrace();
            }

            Result.add(new NewsItem(title, description, date, url, thumbUrl));
        }
        return Result;
    }
}
