package com.example.icyfillup.newsapp.data;

import android.provider.BaseColumns;

/**
 * Created by icyfillup on 7/22/2017.
 */

public class ArticleContract implements BaseColumns
{
    public static final String TABLE_NAME = "article";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_THUMB_URL = "thumburl";
}
