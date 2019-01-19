package com.example.gopal.moviestage1;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import com.example.gopal.moviestage1.database.MovieContract;
import com.example.gopal.moviestage1.database.MovieContract.MovieEntry;

import org.w3c.dom.Text;

public class BookmarkActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private MovieCursorAdapter mCursorAdapter;
    private TextView emptyViewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        emptyViewText = findViewById(R.id.empty_view);

        GridView gridView = findViewById(R.id.gridView);
        mCursorAdapter = new MovieCursorAdapter(this, null);
        gridView.setAdapter(mCursorAdapter);

        gridView.setEmptyView(emptyViewText);

        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {MovieEntry._ID,
                MovieEntry.COLUMN_MOVIE_NAME,
                MovieEntry.COLUMN_MOVIE_POSTER};
        return new CursorLoader(this,
                MovieEntry.BASE_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null) {
            return;
        }
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bookmark_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                getContentResolver().delete(MovieEntry.BASE_URI, null, null);
        }
        return super.onOptionsItemSelected(item);
    }
}
