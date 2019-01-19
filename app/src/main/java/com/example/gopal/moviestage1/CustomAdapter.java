package com.example.gopal.moviestage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Gopal on 1/13/2019.
 */

public class CustomAdapter extends ArrayAdapter<Movie> {

    public CustomAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Movie} object located at this position in the list
        Movie currentMovie = getItem(position);

        TextView movieTitleTextView = (TextView) listItemView.findViewById(R.id.movie_title);
        movieTitleTextView.setText(currentMovie.getMovieTitle());

        ImageView moviePoster = listItemView.findViewById(R.id.movie_poster);
        //Extracting image from Url by Picasso library
        String imageUrl = "https://image.tmdb.org/t/p/original" + currentMovie.getMoviePoster();
        /*Picasso.get().load(imageUrl)
                .into(moviePoster);*/

        //Extracting image from Url by Glide library
        Glide.with(getContext()).load(imageUrl)
                .into(moviePoster);

        return listItemView;
    }
}