package me.ulleo.udacity.learn.popularmovies.utils.asyncTask;

import android.content.ContentValues;
import android.os.AsyncTask;

import me.ulleo.udacity.learn.popularmovies.MovieApplication;
import me.ulleo.udacity.learn.popularmovies.data.MovieContract;
import me.ulleo.udacity.learn.popularmovies.model.Movie;


public class UpdateMovieTask extends AsyncTask<Movie, Void, Boolean> {

    private final String TAG = UpdateMovieTask.class.getSimpleName();

    private final OnUpdateMovieHandler mMovieHandler;

    public interface OnUpdateMovieHandler {
        void onPreExecute();

        void onPostExecute(Boolean success);
    }

    public UpdateMovieTask(OnUpdateMovieHandler onUpdateMovieHandler) {
        mMovieHandler = onUpdateMovieHandler;
    }

    @Override
    protected Boolean doInBackground(Movie... movieArray) {

        if (movieArray.length == 0) {
            return false;
        }
        Movie movie = movieArray[0];

        ContentValues contentValues;

        contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry._ID, movie.getId());

        contentValues.put(MovieContract.MovieEntry.COLUMN_FAVOURITE, movie.isFavourite() ? 1 : 0);


        int update = 0;
        try {
            update = MovieApplication.getContext().getContentResolver()
                    .update(MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(movie.getId())).build(), contentValues, null, null);
        } catch (Exception e) {
            //e.printStackTrace();
        }


        return update != 0;
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
