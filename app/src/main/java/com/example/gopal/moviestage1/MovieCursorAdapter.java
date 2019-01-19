package com.example.gopal.moviestage1;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.gopal.moviestage1.database.MovieContract.MovieEntry;

/**
 * Created by Gopal on 1/16/2019.
 */

public class MovieCursorAdapter extends CursorAdapter {


    public MovieCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView posterImage = view.findViewById(R.id.movie_poster);
        TextView titleText = view.findViewById(R.id.movie_title);
        if (cursor.moveToNext()) {
            int movieTitleColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_NAME);
            int moviePosterColumnIndex = cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_POSTER);

            String movieTitle = cursor.getString(movieTitleColumnIndex);
            String moviePoster = cursor.getString(moviePosterColumnIndex);

            titleText.setText(movieTitle);
            String imageUrl = "https://image.tmdb.org/t/p/original" + moviePoster;
            //Extracting image from Url by Glide library
            Glide.with(context).load(imageUrl)
                    .into(posterImage);

        }
    }
}
