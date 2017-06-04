package me.ulleo.udacity.learn.popularmovies.utils.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;

import me.ulleo.udacity.learn.popularmovies.model.Videos;
import me.ulleo.udacity.learn.popularmovies.utils.NetworkUtils;


public class FetchVideoTask extends AsyncTask<Integer, Void, Videos> {

    private final String TAG = FetchVideoTask.class.getSimpleName();

    private final OnFetchVideoHandler mHandler;

    public interface OnFetchVideoHandler {
        void onPreExecute();

        void onPostExecute(Videos videos);
    }

    public FetchVideoTask(OnFetchVideoHandler onFetchVideoHandler) {
        mHandler = onFetchVideoHandler;
    }

    @Override
    protected Videos doInBackground(Integer... params) {

        if (params.length == 0) {
            return null;
        }
        int id = params[0];
        int page = 1;

        URL fetchVideosURL = NetworkUtils.buildVideosUrl(id, page);

        try {
            String responseJsonStr = NetworkUtils.getResponseFromHttpUrl(fetchVideosURL);

            Log.d("response videos: ", responseJsonStr);

            Gson gson = new GsonBuilder().create();

            Videos videos = gson.fromJson(responseJsonStr, Videos.class);

            Log.d(TAG, videos.toString());

            return videos;

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
    protected void onPostExecute(Videos videos) {

        //DataUtils.updateMovies(movies);
        mHandler.onPostExecute(videos);

    }

}
