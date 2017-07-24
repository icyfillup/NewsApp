package com.example.icyfillup.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by icyfillup on 7/23/2017.
 */

public class ScheduleUtils {
    private static final int SCHEDULE_INTERVAL_SECOND = 5;
    private static final int FLEXTIME_SCHEDULE_INTERVAL_SECOND = 5;
    private static final String SCHEDULE_ARTICLES_TAG = "schedule_article_tag";
    private static boolean isInitialized;

    synchronized public static void scheduleRefresh(@NonNull final Context context)
    {
        // note: make sure that this task is init once.
        if(isInitialized)
        {
            return;
        }

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job constraintReminderJob = dispatcher.newJobBuilder()
                .setService(UpdateActiclesFirebaseJobService.class)
                .setTag(SCHEDULE_ARTICLES_TAG)
                .setConstraints(Constraint.DEVICE_CHARGING)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(SCHEDULE_INTERVAL_SECOND,
                        SCHEDULE_INTERVAL_SECOND + FLEXTIME_SCHEDULE_INTERVAL_SECOND))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(constraintReminderJob);
        isInitialized = true;
    }
}
