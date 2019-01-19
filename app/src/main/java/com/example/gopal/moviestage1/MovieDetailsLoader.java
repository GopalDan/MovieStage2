package com.example.gopal.moviestage1;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Gopal on 1/15/2019.
 */

public class MovieDetailsLoader extends AsyncTaskLoader<List<List<String>>> {
    private String mUrl;

    public MovieDetailsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        //  Log.e(LOG_TAG,"onStartLoading is called: ");
        forceLoad();
    }

    @Override
    public List<List<String>> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<List<String>> movieDetails = QueryUtils.fetchMovieDetails(mUrl);
        return movieDetails;
    }
}
