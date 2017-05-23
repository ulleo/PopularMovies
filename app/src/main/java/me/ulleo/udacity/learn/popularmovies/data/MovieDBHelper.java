package me.ulleo.udacity.learn.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.ulleo.udacity.learn.popularmovies.data.MovieContract.MovieEntry;


public class MovieDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";

    private static final int DATABASE_VERSION = 1;


    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieEntry.COLUMN_ADULT + " INTEGER DEFAULT 0, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_GENREIDS + " TEXT, " +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                MovieEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT, " +
                MovieEntry.COLUMN_TITLE + " TEXT, " +
                MovieEntry.COLUMN_BACKDROPPATH + " TEXT, " +
                MovieEntry.COLUMN_POPULATITY + " REAL, " +
                MovieEntry.COLUMN_VOTE_COUNT + " INTEGER, " +
                MovieEntry.COLUMN_VIDEO + " INTEGER DEFAULT 0, " +
                MovieEntry.COLUMN_VOTEAVERAGE + " REAL, " +
                MovieEntry.COLUMN_FAVOURITE + " INTEGER DEFAULT 0, " +
                MovieEntry.COLUMN_SORT_POPULARITY + " INTEGER DEFAULT 0, " +
                MovieEntry.COLUMN_SORT_RATED + " INTEGER DEFAULT 0" +
                ");";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
