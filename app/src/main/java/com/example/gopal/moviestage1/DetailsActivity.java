package com.example.gopal.moviestage1;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gopal.moviestage1.database.MovieContract.MovieEntry;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<List<String>>> {

    public static final String LOG_TAG = DetailsActivity.class.getSimpleName();
    boolean mBookmarkState = false;
    private String mUrl;
    private TextView mTrailerTextView;
    private TextView mReviewText;
    private String mKey;
    private String movieTitle;
    private String moviePoster;
    private String movieId;
    private Uri receivedUri;
    private View mBookMarkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if(savedInstanceState!=null){
           mBookmarkState =savedInstanceState.getBoolean("bookMark-key");
           int color = savedInstanceState.getInt("color");
            Log.e(LOG_TAG,"Value of received Color: " +  color);
           mBookMarkButton.setBackgroundColor(color);
        }

        mTrailerTextView = findViewById(R.id.trailer);
        mReviewText = findViewById(R.id.movie_reviews);

        TextView title = findViewById(R.id.movie_title);
        TextView rating = findViewById(R.id.movie_rating);
        TextView releaseDate = findViewById(R.id.movie_release_date);
        TextView overview = findViewById(R.id.movie_overview);
        ImageView poster = findViewById(R.id.movie_poster);

        movieTitle = getIntent().getStringExtra(MainActivity.MOVIE_TITLE_KEY);
        moviePoster = getIntent().getStringExtra(MainActivity.MOVIE_POSTER_KEY);
        String movieRating = getIntent().getStringExtra(MainActivity.MOVIE_RATING_KEY);
        String movieReleaseDate = dateFormatter(getIntent().getStringExtra(MainActivity.MOVIE_RELEASE_DATE_KEY));
        String movieOverview = getIntent().getStringExtra(MainActivity.MOVIE_OVERVIEW_KEY);
        movieId = getIntent().getStringExtra(MainActivity.MOVIE_ID_KEY);

        title.setText(movieTitle);
        rating.setText(movieRating + "/10,");
        releaseDate.setText(movieReleaseDate);
        overview.setText(movieOverview);
        Log.e("DetailsActivity", movieReleaseDate);

        //Extracting image from Url by Picasso library
        String imageUrl = "https://image.tmdb.org/t/p/original" + moviePoster;
        Picasso.get().load(imageUrl)
                .into(poster);
      /*  Glide.with(this).load(imageUrl)
                .into(poster);
      */
        mUrl = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=7bf68dbc7833ff0567ea231ec0cef88f&append_to_response=videos%2Creviews";
        getLoaderManager().initLoader(1, null, this);

    }

    // 2018-12-07
    private String dateFormatter(String dateString) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = fmt.parse(dateString);
            SimpleDateFormat fmtOut = new SimpleDateFormat("d MMM yyyy");
            return fmtOut.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    public void showTrailer(View view) {
        String url = "https://www.youtube.com/watch?v=" + mKey;
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_activity_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorite:
                changeBookmarkButtonColor();
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(DetailsActivity.this);
                break;
            default:
                throw new UnsupportedOperationException("No such operation available");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<List<String>>> onCreateLoader(int i, Bundle bundle) {
        return new MovieDetailsLoader(this, mUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<List<String>>> loader, List<List<String>> movieDetails) {

        if (movieDetails == null) {
            Log.v(LOG_TAG, "Getting Nothing");
            return;
        }
        Log.v(LOG_TAG, "Length of MovieDetails is-" + movieDetails.size());

        List<String> trailers = movieDetails.get(0);
        List<String> reviews = movieDetails.get(1);
        Log.v(LOG_TAG, "Length of MovieDetails trailers -" + trailers.size());
        Log.v(LOG_TAG, "Length of MovieDetails reviews-" + reviews.size());

        for (int i = 0; i < trailers.size(); i++) {
            mKey = trailers.get(i);
            break;
            //Log.v(LOG_TAG,"Trailer key is " + key);
            // mTrailerTextView.append("Trailer times" +(i) +  "\n");
        }
        if (reviews.isEmpty()) {
            mReviewText.setText("No review found");
        } else {
            int j = 1;
            for (int i = 0; i < reviews.size(); i++) {
                String review = reviews.get(i);
                mReviewText.append(j + " " + review + "\n");
                j++;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<List<String>>> loader) {}

    public void changeBookmarkButtonColor() {
         mBookMarkButton = findViewById(R.id.favorite);

        if (!mBookmarkState) {
            int color = ContextCompat.getColor(this, R.color.colorAccent);
            mBookMarkButton.setBackgroundColor(color);
            bookMarkButtonIsClicked();
            mBookmarkState = true;
        } else {
            // remove background color
            int color = ContextCompat.getColor(this, R.color.colorPrimary);
            mBookMarkButton.setBackgroundColor(color);
            bookMarkButtonUnclicked();
            mBookmarkState = false;
        }
    }

    private void bookMarkButtonIsClicked() {
        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_MOVIE_NAME, movieTitle);
        values.put(MovieEntry.COLUMN_MOVIE_POSTER, moviePoster);
        values.put(MovieEntry.COLUMN_MOVIE_ID, movieId);

        receivedUri = getContentResolver().insert(MovieEntry.BASE_URI, values);
        long id = ContentUris.parseId(receivedUri);
        if (receivedUri == null) {
            Toast.makeText(this, "Bookmarked Unsuccessful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Added as your favorite movie!" + id, Toast.LENGTH_SHORT).show();
        }
    }

    private void bookMarkButtonUnclicked() {

        long rowDeleted = getContentResolver().delete(receivedUri, null, null);
        if (rowDeleted == 0) {
            Toast.makeText(this, "Bookmarked Unsuccessful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Removed from your favorite movie list", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle, PersistableBundle outPersistentState) {
        bundle.putBoolean("bookMark-key", mBookmarkState);
        ColorDrawable drawable = (ColorDrawable)mBookMarkButton.getBackground();
        int color = drawable.getColor();
        bundle.putInt("color", color);
        Log.e(LOG_TAG,"Value of saved Color: " +  color);
        super.onSaveInstanceState(bundle, outPersistentState);
    }
}
