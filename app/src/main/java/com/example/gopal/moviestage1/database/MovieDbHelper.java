package com.example.gopal.moviestage1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.gopal.moviestage1.database.MovieContract.MovieEntry;

/**
 * Created by Gopal on 1/15/2019.
 */

public class MovieDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "cinema.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_DATABASE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + "("
                + MovieEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MovieEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL,"
                + MovieEntry.COLUMN_MOVIE_POSTER + " TEXT NOT NULL,"
                + MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL )";
        sqLiteDatabase.execSQL(CREATE_DATABASE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
