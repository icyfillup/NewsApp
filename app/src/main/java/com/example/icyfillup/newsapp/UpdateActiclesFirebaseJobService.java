package com.example.icyfillup.newsapp;

import android.os.AsyncTask;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by icyfillup on 7/23/2017.
 */

public class UpdateActiclesFirebaseJobService extends JobService {
    private AsyncTask backgroundTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        backgroundTask = new AsyncTask() {
            @Override
            protected void onPreExecute() {
                Toast.makeText(UpdateActiclesFirebaseJobService.this, "News refreshed", Toast.LENGTH_SHORT).show();
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                RefreshArticles.refreshArticles(UpdateActiclesFirebaseJobService.this);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
                super.onPostExecute(o);
            }
        };

        backgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(final JobParameters job)
    {
        if(backgroundTask != null){backgroundTask.cancel(false);}
        return true;
    }
}