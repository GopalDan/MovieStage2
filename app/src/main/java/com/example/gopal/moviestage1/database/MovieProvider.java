package com.example.gopal.moviestage1.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Gopal on 1/15/2019.
 */
import com.example.gopal.moviestage1.database.MovieContract.MovieEntry;

public class MovieProvider extends ContentProvider {
    private MovieDbHelper mMovieDbHelper;
    private final static int PATH_KEY = 23;
    private final static int SINGLE_PATH_KEY = 56;

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieEntry.TABLE_NAME,PATH_KEY);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieEntry.TABLE_NAME + "/#",SINGLE_PATH_KEY);
    }
    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return false;
    }


    @Override
    public Cursor query( Uri uri,  String[] projection,  String selection,  String[] selectionArgs,  String sortOrder) {
        SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        Cursor cursor;

        switch (uriMatcher.match(uri)){
            case PATH_KEY:
               cursor = db.query(MovieEntry.TABLE_NAME,projection, selection, selectionArgs, null,null,null);
               break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public Uri insert( Uri uri,  ContentValues values) {
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        long id;
        switch(uriMatcher.match(uri)){
            case PATH_KEY:
                id = db.insert(MovieEntry.TABLE_NAME, null,values );break;
                default:
                    throw new UnsupportedOperationException("Unknown uri " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete( Uri uri,  String s,  String[] strings) {
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int rowDeleted;
        switch (uriMatcher.match(uri)){
            case SINGLE_PATH_KEY:
                String selection = MovieEntry.COLUMN_ID + " = ?";
                String[] selectionArgs = {String.valueOf(ContentUris.parseId(uri))};
               rowDeleted = db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);break;
            case PATH_KEY:
               rowDeleted = db.delete(MovieEntry.TABLE_NAME,s, strings);break;
               default:
                   throw new UnsupportedOperationException("Unknown uri " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowDeleted;
    }

    @Override
    public int update( Uri uri,  ContentValues contentValues,  String s,  String[] strings) {
        throw new RuntimeException("We don't need update operation");
    }

    @Override
    public String getType( Uri uri) {
        throw new RuntimeException("We don't need this operation");
    }
}
