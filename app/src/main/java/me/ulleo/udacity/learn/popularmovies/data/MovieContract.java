package me.ulleo.udacity.learn.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ulleo on 2017/5/21.
 */

public class MovieContract {
    public static final String CONTENT_AUTHORITY = "me.ulleo.udacity.learn.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_MOVIE_FAVOURITE = "favourite";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIE)
                .build();

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_GENREIDS = "genreIds";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "originalLanguage";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROPPATH = "backdropPath";
        public static final String COLUMN_POPULATITY = "popularity";
        public static final String COLUMN_VOTE_COUNT = "voteCount";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_VOTEAVERAGE = "voteAverage";


        public static final String COLUMN_FAVOURITE = "favourite";
        public static final String COLUMN_SORT_POPULARITY = "sort_pop";
        public static final String COLUMN_SORT_RATED = "sort_rated";


    }
}
