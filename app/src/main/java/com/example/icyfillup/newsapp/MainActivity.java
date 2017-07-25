package com.example.icyfillup.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.icyfillup.newsapp.data.ArticleDbHelper;
import com.example.icyfillup.newsapp.utilities.DatabaseUtils;

import java.net.URL;


public class MainActivity extends AppCompatActivity implements NewsApiAdapter.OpenUrlLinkToBrowser, LoaderManager.LoaderCallbacks<Void> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int NEWS_APP_LOADER_ID = 22;

    ProgressBar progressBar;

    private NewsApiAdapter adapter;
    private RecyclerView recyclerView;

    private Cursor cursor;
    private SQLiteDatabase articleDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.loading_indicator);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_newsapi);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirst = prefs.getBoolean("isfirst", true);
        if(isFirst)
        {
            load();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isfirst", false);
            editor.commit();
        }

        ScheduleUtils.scheduleRefresh(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        articleDB = new ArticleDbHelper(this).getReadableDatabase();
        cursor = DatabaseUtils.getAllArticles(articleDB);

        adapter = new NewsApiAdapter(this, cursor);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        articleDB.close();
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int ItemSelectedId = item.getItemId();
        if (ItemSelectedId == R.id.action_search) {

            //NOTE: this placeHolderBundle variable has no purpose for the program.
            //      this is use to make sure that the loaderManager does not pass in a null bundle set by the programmer
            //      the usage is to distinguish programmer set bundle and framework set bundle
            load();
        }
        return super.onOptionsItemSelected(item);
    }

    // NOTE: Open the url link on the browner
    @Override
    public void onItemClick(URL UrlLink) {
        Uri UriLink = Uri.parse(UrlLink.toString());

        Intent intent = new Intent(Intent.ACTION_VIEW, UriLink);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public Loader<Void> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Void>(this) {
            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public Void loadInBackground() {
                RefreshArticles.refreshArticles(MainActivity.this);
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Void> loader, Void __) {
        progressBar.setVisibility(View.INVISIBLE);
        articleDB = new ArticleDbHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAllArticles(articleDB);

        adapter = new NewsApiAdapter(this, cursor);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    public void load()
    {
        Bundle placeHolderBundle = new Bundle();
        LoaderManager loaderManager = getSupportLoaderManager();

        loaderManager.restartLoader(NEWS_APP_LOADER_ID, placeHolderBundle, this).forceLoad();
    }
}
