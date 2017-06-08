package me.ulleo.udacity.learn.popularmovies.utils.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;

import me.ulleo.udacity.learn.popularmovies.model.Movie;
import me.ulleo.udacity.learn.popularmovies.utils.NetworkUtils;


public class FetchMovieTask extends AsyncTask<Integer, Void, Movie> {

    private final String TAG = FetchMovieTask.class.getSimpleName();

    private final OnFetchMovieHandler mHandler;

    public interface OnFetchMovieHandler {
        void onPreExecute();

        void onPostExecute(Movie movie);
    }

    public FetchMovieTask(OnFetchMovieHandler onFetchMovieHandler) {
        mHandler = onFetchMovieHandler;
    }

    @Override
    protected Movie doInBackground(Integer... params) {

        if (params.length == 0) {
            return null;
        }
        int id = params[0];
        int page = 1;

        URL fetchVideosURL = NetworkUtils.buildMoviesUrl(id, page);

        try {
            String responseJsonStr = NetworkUtils.getResponseFromHttpUrl(fetchVideosURL);

            Log.d("response movie: ", responseJsonStr);

            Gson gson = new GsonBuilder().create();

            Movie movie = gson.fromJson(responseJsonStr, Movie.class);

            Log.d(TAG, movie.toString());

            return movie;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mHandler.onPreExecute();
    }

    @Override
    protected void onPostExecute(Movie movie) {

        //DataUtils.updateMovies(movies);
        mHandler.onPostExecute(movie);

    }

}
