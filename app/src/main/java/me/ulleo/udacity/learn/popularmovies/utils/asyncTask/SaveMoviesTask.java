package me.ulleo.udacity.learn.popularmovies.utils.asyncTask;

import android.content.ContentValues;
import android.os.AsyncTask;

import java.util.ArrayList;

import me.ulleo.udacity.learn.popularmovies.MovieApplication;
import me.ulleo.udacity.learn.popularmovies.data.MovieContract;
import me.ulleo.udacity.learn.popularmovies.model.Movie;
import me.ulleo.udacity.learn.popularmovies.model.Movies;
import me.ulleo.udacity.learn.popularmovies.utils.Sort;


public class SaveMoviesTask extends AsyncTask<Movies, Void, Boolean> {

    private final String TAG = SaveMoviesTask.class.getSimpleName();

    private final OnSaveMovieHandler mMovieHandler;

    public interface OnSaveMovieHandler {
        void onPreExecute();

        void onPostExecute(Boolean success);
    }

    public SaveMoviesTask(OnSaveMovieHandler onSaveMovieHandler) {
        mMovieHandler = onSaveMovieHandler;
    }

    @Override
    protected Boolean doInBackground(Movies... moviesArray) {

        if (moviesArray.length == 0) {
            return false;
        }
        Movies movies = moviesArray[0];
        ArrayList<ContentValues> arrayList = new ArrayList<>();
        ContentValues contentValues;
        String genreIds = "";
        int isSortPop, isSortRated;
        for (Movie movie : movies.getMovies()) {
            contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieEntry._ID, movie.getId());
            contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
            contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
            contentValues.put(MovieContract.MovieEntry.COLUMN_ADULT, movie.isAdult() ? 1 : 0);
            contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
            contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            for (Integer i : movie.getGenreIds()) {
                genreIds = i + ",";
            }
            contentValues.put(MovieContract.MovieEntry.COLUMN_GENREIDS, genreIds);
            contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
            contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
            contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
            contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROPPATH, movie.getBackdropPath());
            contentValues.put(MovieContract.MovieEntry.COLUMN_POPULATITY, movie.getPopularity());
            contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
            contentValues.put(MovieContract.MovieEntry.COLUMN_VIDEO, movie.isVideo() ? 1 : 0);
            contentValues.put(MovieContract.MovieEntry.COLUMN_VOTEAVERAGE, movie.getVoteAverage());

            if (movies.getSearchType().equals(Sort.TOP_RATED)) {
                isSortPop = 0;
                isSortRated = 1;
            } else {
                isSortPop = 1;
                isSortRated = 0;
            }
            contentValues.put(MovieContract.MovieEntry.COLUMN_SORT_POPULARITY, isSortPop);
            contentValues.put(MovieContract.MovieEntry.COLUMN_SORT_RATED, isSortRated);

            arrayList.add(contentValues);
        }
        ContentValues[] values = arrayList.toArray(new ContentValues[arrayList.size()]);

        int insert = 0;
        try {
            insert = MovieApplication.getContext().getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, values);
        } catch (Exception e) {
            //e.printStackTrace();
        }


        return insert > 0;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mMovieHandler.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean success) {

        //DataUtils.updateMovies(movies);
        mMovieHandler.onPostExecute(success);

    }

}
