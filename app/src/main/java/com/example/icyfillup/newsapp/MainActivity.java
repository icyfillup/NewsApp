package com.example.icyfillup.newsapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.icyfillup.newsapp.utilities.NetworkUtils;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    TextView NewsApiTextView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NewsApiTextView = (TextView) findViewById(R.id.news_api);
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
            new FetchNewsTask().execute("hello");
        }
        return super.onOptionsItemSelected(item);
    }

    class FetchNewsTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String SearchQuery = strings[0];
            URL NewsApiUrl = NetworkUtils.buildUrl(SearchQuery);
            String JsonNewsApiResponse = null;
            try
            {
                JsonNewsApiResponse = NetworkUtils.getResponseFromHttpUrl(NewsApiUrl);
                Log.d(TAG, "doInBackground: " + JsonNewsApiResponse);
            }catch(IOException e)
            {
                e.printStackTrace();
            };
            return JsonNewsApiResponse;
        }

        @Override
        protected void onPostExecute(String string) {
            progressBar.setVisibility(View.INVISIBLE);
            if(string != null && !string.isEmpty())
            {
                NewsApiTextView.setText(string.toString());
            }

            super.onPostExecute(string);
        }
    }
}
