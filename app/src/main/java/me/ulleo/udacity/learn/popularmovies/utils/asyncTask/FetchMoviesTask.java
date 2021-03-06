package me.ulleo.udacity.learn.popularmovies.utils.asyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;

import me.ulleo.udacity.learn.popularmovies.model.Movies;
import me.ulleo.udacity.learn.popularmovies.model.SearchParam;
import me.ulleo.udacity.learn.popularmovies.utils.NetworkUtils;


public class FetchMoviesTask extends AsyncTask<SearchParam, Void, Movies> {

    private final String TAG = FetchMoviesTask.class.getSimpleName();

    private final OnFetchMovieHandler mMovieHandler;

    public interface OnFetchMovieHandler {
        void onPreExecute();

        void onPostExecute(Movies movies);
    }

    public FetchMoviesTask(OnFetchMovieHandler onFetchMovieHandler) {
        mMovieHandler = onFetchMovieHandler;
    }

    @Override
    protected Movies doInBackground(SearchParam... params) {

        if (params.length == 0) {
            return null;
        }
        SearchParam searchParam = params[0];
        String searchType = searchParam.getSearchType();
        int page = searchParam.getPage();

        URL fetchMoviesURL = NetworkUtils.buildUrl(searchType, page);

        try {
            String responseJsonStr = NetworkUtils.getResponseFromHttpUrl(fetchMoviesURL);

            Log.d("response", responseJsonStr);

            Gson gson = new GsonBuilder().create();

            Movies movies = gson.fromJson(responseJsonStr, Movies.class);

            movies.setSearchType(searchType);

            return movies;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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
