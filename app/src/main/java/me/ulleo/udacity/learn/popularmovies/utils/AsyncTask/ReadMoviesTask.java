package me.ulleo.udacity.learn.popularmovies.utils.AsyncTask;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.ulleo.udacity.learn.popularmovies.MovieApplication;
import me.ulleo.udacity.learn.popularmovies.data.MovieContract;
import me.ulleo.udacity.learn.popularmovies.model.Movie;
import me.ulleo.udacity.learn.popularmovies.model.Movies;
import me.ulleo.udacity.learn.popularmovies.model.ReadParam;


public class ReadMoviesTask extends AsyncTask<ReadParam, Void, Movies> {

    private final String TAG = ReadMoviesTask.class.getSimpleName();

    private final OnReadMoviesHandler mMovieHandler;

    public interface OnReadMoviesHandler {
        void onPreExecute();

        void onPostExecute(Movies movies);
    }

    public ReadMoviesTask(OnReadMoviesHandler onReadMovieHandler) {
        mMovieHandler = onReadMovieHandler;
    }

    @Override
    protected Movies doInBackground(ReadParam... params) {

        if (params.length == 0) {
            return null;
        }

        ReadParam readParam = params[0];
        String sort = readParam.getSort();
        boolean isFavourite = readParam.isFavourite();
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        Cursor cursor = null;
        if (readParam.getMovieId() == -1) {
            if (isFavourite) {
                uri = uri.buildUpon().appendPath(MovieContract.PATH_MOVIE_FAVOURITE).build();
            }
        } else {
            int movieId = readParam.getMovieId();
            uri = uri.buildUpon().appendPath(Integer.toString(movieId)).build();
        }
        try {
            cursor = MovieApplication.getContext().getContentResolver().query(uri, null, null, null, sort);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cursor == null) {
            return null;
        }

        Movie movie;
        List<Movie> movieList = new ArrayList<>();
        String genreIds;
        String[] genreIdsArray;
        List<Integer> genreIdsList = new ArrayList<>();
        while (cursor.moveToNext()) {
            movie = new Movie();
            movie.setId(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)));
            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));
            movie.setAdult(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ADULT)) == 1);
            movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)));
            genreIds = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_GENREIDS));
            genreIdsArray = genreIds.split(",");
            for (String isStr : genreIdsArray) {
                try {
                    genreIdsList.add(Integer.parseInt(isStr));
                } catch (NumberFormatException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            movie.setGenreIds(genreIdsList);
            movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE)));
            movie.setOriginalLanguage(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE)));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
            movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROPPATH)));
            movie.setPopularity(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULATITY)));
            movie.setVoteCount(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT)));
            movie.setVideo(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VIDEO)) == 1);
            movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTEAVERAGE)));
            movie.setFavourite(cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_FAVOURITE)) == 1);

            movieList.add(movie);
        }

        Movies movies = new Movies();
        movies.setMovies(movieList);
        movies.setPage(1);

        cursor.close();

        return movies;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mMovieHandler.onPreExecute();
    }

    @Override
    protected void onPostExecute(Movies movies) {

        //DataUtils.updateMovies(movies);
        mMovieHandler.onPostExecute(movies);

    }

}
