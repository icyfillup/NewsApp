package com.example.icyfillup.newsapp.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    private final static String apiKey = "Input Api key here";

    public static URL buildUrl(String SearchQuery)
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
}
