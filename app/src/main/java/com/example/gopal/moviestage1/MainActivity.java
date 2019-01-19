package com.example.gopal.moviestage1;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<List<Movie>> {
    String LOG_TAG = MainActivity.class.getSimpleName();

    public static final String MOVIE_ID_KEY = "movieId";
    public static final String MOVIE_TITLE_KEY = "movieTitle";
    public static final String MOVIE_POSTER_KEY = "moviePoster";
    public static final String MOVIE_RATING_KEY = "movieRating";
    public static final String MOVIE_RELEASE_DATE_KEY = "movieReleaseDate";
    public static final String MOVIE_OVERVIEW_KEY = "movieOverview";
    private final int ID_MAIN_LOADER = 0;
    private final int ID_POPULAR_MOVIE_LOADER = 1;
    private final int ID_TOP_RATED_MOVIE_LOADER = 2;
    CustomAdapter mCustomAdapter;
    ProgressBar mProgressbar;
    private TextView noNetworkTextView;
    private TextView emptyViewText;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressbar = findViewById(R.id.progress_bar);
        Log.v("MainActivity", "onCreate ");
        setTitle("Movies");

        emptyViewText = findViewById(R.id.empty_view);

        final GridView gridView = findViewById(R.id.gridView);
        mCustomAdapter = new CustomAdapter(this, new ArrayList<Movie>());
        gridView.setAdapter(mCustomAdapter);
        // As initially gridview is null during networking it's showing empty textview
        // but don't need at this time so use in case of gridview null after getting data
       // gridView.setEmptyView(emptyViewText);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        if (isConnected) {
            getLoaderManager().initLoader(ID_MAIN_LOADER, null, this);
        } else {
            mProgressbar.setVisibility(View.GONE);
            noNetworkTextView = findViewById(R.id.no_network);
            noNetworkTextView.setText("Oops! looks like no Network" + "\n See Your Bookmarked movie!");
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Movie currentMovie = mCustomAdapter.getItem(position);
                String movieId = currentMovie.getMovieId();
                String movieTitle = currentMovie.getMovieTitle();
                String moviePoster = currentMovie.getMoviePoster();
                String movieRating = currentMovie.getMovieRating();
                String movieReleaseDate = currentMovie.getMovieReleaseDate();
                String movieOverview = currentMovie.getMovieOverview();

                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(MOVIE_ID_KEY, movieId);
                intent.putExtra(MOVIE_TITLE_KEY, movieTitle);
                intent.putExtra(MOVIE_POSTER_KEY, moviePoster);
                intent.putExtra(MOVIE_RATING_KEY, movieRating);
                intent.putExtra(MOVIE_RELEASE_DATE_KEY, movieReleaseDate);
                intent.putExtra(MOVIE_OVERVIEW_KEY, movieOverview);
                startActivity(intent);
            }
        });
    }

    @Override
    public android.content.Loader<List<Movie>> onCreateLoader(int id, Bundle bundle) {
        if (id == ID_POPULAR_MOVIE_LOADER) {
            mUrl = "https://api.themoviedb.org/3/movie/popular?api_key=7bf68dbc7833ff0567ea231ec0cef88f";
        } else if (id == ID_TOP_RATED_MOVIE_LOADER) {
            mUrl = "https://api.themoviedb.org/3/movie/top_rated?api_key=7bf68dbc7833ff0567ea231ec0cef88f";
        } else {
            mUrl = "https://api.themoviedb.org/3/movie/now_playing?api_key=7bf68dbc7833ff0567ea231ec0cef88f";
        }
        return new CustomLoader(MainActivity.this, mUrl);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Movie>> loader, List<Movie> movies) {

        mCustomAdapter.clear();
        mProgressbar.setVisibility(View.GONE);
        if (movies == null) {
            Log.e("MainActivity", "No movie found");
            emptyViewText.setText("Oops! No movie found ");
            return;
        }
        mCustomAdapter.addAll(movies);
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Movie>> loader) {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_popularity:
                getLoaderManager().restartLoader(1, null, this);
                break;
            case R.id.sort_by_rating:
                getLoaderManager().restartLoader(2, null, this);
                break;

            case R.id.bookmark:
                startActivity(new Intent(this, BookmarkActivity.class));

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(LOG_TAG,"ONSTART");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(LOG_TAG,"ONRESUME ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(LOG_TAG,"ONPAUSE");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(LOG_TAG,"ONSTOP ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(LOG_TAG,"ONDESTROY");
    }
}
