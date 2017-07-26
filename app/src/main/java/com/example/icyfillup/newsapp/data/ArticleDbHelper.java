package com.example.icyfillup.newsapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by icyfillup on 7/22/2017.
 */

public class ArticleDbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "Articles.db";
    private static final int DATABASE_VERSION = 2;

    public ArticleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_ARTICLES_TABLE = "CREATE TABLE " + ArticleContract.TABLE_NAME + "("
                + ArticleContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ArticleContract.COLUMN_TITLE + " TEXT NOT NULL,"
                + ArticleContract.COLUMN_DESCRIPTION + " TEXT NOT NULL,"
                + ArticleContract.COLUMN_DATE + " TEXT NOT NULL,"
                + ArticleContract.COLUMN_URL + " TEXT NOT NULL,"
                + ArticleContract.COLUMN_THUMB_URL + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_ARTICLES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ArticleContract.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
