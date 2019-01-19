package com.example.gopal.moviestage1;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Gopal on 1/13/2019.
 */

public class CustomLoader extends AsyncTaskLoader<List<Movie>> {
    String LOG_TAG = CustomLoader.class.getSimpleName();
    private String mUrl;
    List<Movie> movies;
    public CustomLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        //  Log.e(LOG_TAG,"onStartLoading is called: ");
        if (movies != null) {
            // Use cached data
            deliverResult(movies);
        } else {
            // We have no data, so kick off loading it
            forceLoad();
        }
    }

    @Override
    public List<Movie> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
         Log.e(LOG_TAG,"loadInBackground is called: ");
        // Perform the network request, parse the response, and extract a list of movies.
        Log.v("CustomLoader", "URL for data fetching- " + mUrl);
         movies = QueryUtils.fetchTrainData(mUrl);
        return movies;
    }

    @Override
    public void deliverResult(List<Movie> data) {
        // We’ll save the data for later retrieval
        Log.v(LOG_TAG, "Caching  data is used- " + mUrl);

        movies = data;
        // We can do any pre-processing we want here
        // Just remember this is on the UI thread so nothing lengthy!
        super.deliverResult(data);
    }
}
