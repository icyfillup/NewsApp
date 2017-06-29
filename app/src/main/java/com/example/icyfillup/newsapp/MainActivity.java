package com.example.icyfillup.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.icyfillup.newsapp.utilities.NetworkUtils;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NewsApiAdapter.OpenUrlLinkToBrowser {

    private static final String TAG = MainActivity.class.getSimpleName();

    ProgressBar progressBar;

    private NewsApiAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_newsapi);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        adapter = new NewsApiAdapter(this);
        recyclerView.setAdapter(adapter);

        progressBar = (ProgressBar) findViewById(R.id.loading_indicator);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int ItemSelectedId = item.getItemId();
        if(ItemSelectedId == R.id.action_search)
        {
            adapter.setNewsArticles(null);
            new FetchNewsTask().execute("hello");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(URL UrlLink){
        Uri UriLink = Uri.parse(UrlLink.toString());

        Intent intent = new Intent(Intent.ACTION_VIEW, UriLink);

        if(intent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(intent);
        }
    }

    class FetchNewsTask extends AsyncTask<String, Void, ArrayList<NewsItem>>
    {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<NewsItem> doInBackground(String... strings) {
            String SearchQuery = strings[0];
            URL NewsApiUrl = NetworkUtils.buildUrl(SearchQuery);

            ArrayList<NewsItem> NewsArticles = null;

            String JsonNewsApiResponse = null;
            try
            {
                JsonNewsApiResponse = NetworkUtils.getResponseFromHttpUrl(NewsApiUrl);
                NewsArticles = NetworkUtils.getNewsItemsFromJson(JsonNewsApiResponse);
                Log.d(TAG, "doInBackground: " + JsonNewsApiResponse);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            };
            return NewsArticles;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsItem> NewsArticles) {
            progressBar.setVisibility(View.INVISIBLE);
            if(NewsArticles != null && !NewsArticles.isEmpty())
            {
                adapter.setNewsArticles(NewsArticles);
            }

            super.onPostExecute(NewsArticles);
        }
    }
}
