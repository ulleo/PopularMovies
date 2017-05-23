package me.ulleo.udacity.learn.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static me.ulleo.udacity.learn.popularmovies.data.MovieContract.MovieEntry.COLUMN_FAVOURITE;
import static me.ulleo.udacity.learn.popularmovies.data.MovieContract.MovieEntry.COLUMN_MOVIE_ID;
import static me.ulleo.udacity.learn.popularmovies.data.MovieContract.MovieEntry.COLUMN_SORT_POPULARITY;
import static me.ulleo.udacity.learn.popularmovies.data.MovieContract.MovieEntry.COLUMN_SORT_RATED;
import static me.ulleo.udacity.learn.popularmovies.data.MovieContract.MovieEntry.TABLE_NAME;
import static me.ulleo.udacity.learn.popularmovies.data.MovieContract.MovieEntry._ID;


public class MovieProvider extends ContentProvider {


    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;
    public static final int MOVIE_WITH_MOVIE_ID = 102;
    public static final int MOVIES_FAVOURITE = 103;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String TAG = "MovieProvider";

    public static final String SORT_POPULARITY = MovieContract.MovieEntry.COLUMN_POPULATITY + " desc";
    public static final String SORT_RATED = MovieContract.MovieEntry.COLUMN_VOTEAVERAGE + " desc";

    private MovieDBHelper dbHelper;

    private static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE, MOVIES);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/#", MOVIE_WITH_ID);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/id/#", MOVIE_WITH_MOVIE_ID);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_MOVIE + "/" + MovieContract.PATH_MOVIE_FAVOURITE, MOVIES_FAVOURITE);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new MovieDBHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                String select;

                if (MovieProvider.SORT_RATED.equals(sortOrder)) {
                    select = COLUMN_SORT_RATED;
                } else {
                    select = COLUMN_SORT_POPULARITY;
                }

                cursor = dbHelper.getReadableDatabase()
                        .query(
                                TABLE_NAME,
                                projection,
                                select + " = ? ",
                                new String[]{"1"},
                                null,
                                null,
                                sortOrder
                        );
                break;
            case MOVIES_FAVOURITE:
                cursor = dbHelper.getReadableDatabase()
                        .query(
                                TABLE_NAME,
                                projection,
                                COLUMN_FAVOURITE + " = ? ",
                                new String[]{"1"},
                                null,
                                null,
                                sortOrder
                        );
                break;
            case MOVIE_WITH_MOVIE_ID:
                String normalizedMovieIdString = uri.getLastPathSegment();
                String[] selectionMovieIdArguments = new String[]{normalizedMovieIdString};

                cursor = dbHelper.getReadableDatabase()
                        .query(
                                TABLE_NAME,
                                projection,
                                COLUMN_MOVIE_ID + " = ? ",
                                selectionMovieIdArguments,
                                null,
                                null,
                                sortOrder);
                break;
            case MOVIE_WITH_ID:
                String normalizedIdString = uri.getLastPathSegment();
                String[] selectionIdArguments = new String[]{normalizedIdString};

                cursor = dbHelper.getReadableDatabase()
                        .query(
                                TABLE_NAME,
                                projection,
                                _ID + " = ? ",
                                selectionIdArguments,
                                null,
                                null,
                                sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new RuntimeException(
                "We are not implementing insert. Use bulkInsert instead");
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                int rowsInserted = 0;
                try {
                    db.beginTransaction();
                    for (ContentValues value : values) {

                        long _id = db.insert(TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        } else {
                            int movie_id = value.getAsInteger(COLUMN_MOVIE_ID);
                            _id = db.update(TABLE_NAME, value, COLUMN_MOVIE_ID + " = ? ", new String[]{Integer.toString(movie_id)});
                            if (_id != 0) {
                                rowsInserted++;
                                Log.d(TAG, "update movie id: " + movie_id + " , _id: " + _id);
                            }
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int delete;
        String id;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_WITH_ID:
                id = uri.getPathSegments().get(1);
                delete = dbHelper.getWritableDatabase().delete(TABLE_NAME, _ID + " = ? ", new String[]{id});
                break;
            case MOVIE_WITH_MOVIE_ID:
                id = uri.getPathSegments().get(1);
                delete = dbHelper.getWritableDatabase().delete(TABLE_NAME, COLUMN_MOVIE_ID + " = ? ", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (delete != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return delete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int update;
        String id;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_WITH_ID:
                id = uri.getPathSegments().get(1);
                update = dbHelper.getWritableDatabase().update(TABLE_NAME, values, _ID + " = ? ", new String[]{id});
                break;
            case MOVIE_WITH_MOVIE_ID:
                id = uri.getPathSegments().get(1);
                update = dbHelper.getWritableDatabase().update(TABLE_NAME, values, COLUMN_MOVIE_ID + " = ? ", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (update != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return update;
    }
}
