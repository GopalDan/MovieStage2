package com.example.gopal.moviestage1.database;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URL;

/**
 * Created by Gopal on 1/15/2019.
 */

public final class MovieContract {

    public static String CONTENT_AUTHORITY = "com.example.gopal.moviestage1";
    public static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static class MovieEntry implements BaseColumns{

        public static String TABLE_NAME = "movie";
        public static String COLUMN_ID = BaseColumns._ID;
        public static String COLUMN_MOVIE_NAME = "movieName";
        public static String COLUMN_MOVIE_POSTER = "moviePoster";
        public static String COLUMN_MOVIE_ID = "movieId";

        public static Uri BASE_URI = Uri.withAppendedPath(BASE_CONTENT_URI, TABLE_NAME);

    }
}
