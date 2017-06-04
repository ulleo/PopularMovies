package me.ulleo.udacity.learn.popularmovies.utils.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URL;

import me.ulleo.udacity.learn.popularmovies.model.Reviews;
import me.ulleo.udacity.learn.popularmovies.utils.NetworkUtils;


public class FetchReviewsTask extends AsyncTask<Integer, Void, Reviews> {

    private final String TAG = FetchReviewsTask.class.getSimpleName();

    private final OnFetcReviewsHandler mHandler;

    public interface OnFetcReviewsHandler {
        void onPreExecute();

        void onPostExecute(Reviews reviews);
    }

    public FetchReviewsTask(OnFetcReviewsHandler onFetchReviewsHandler) {
        mHandler = onFetchReviewsHandler;
    }

    @Override
    protected Reviews doInBackground(Integer... params) {

        if (params.length == 0) {
            return null;
        }
        int id = params[0];
        int page = 1;

        URL fetchVideosURL = NetworkUtils.buildReviewsUrl(id, page);

        try {
            String responseJsonStr = NetworkUtils.getResponseFromHttpUrl(fetchVideosURL);

            Log.d("response reviews: ", responseJsonStr);

            Gson gson = new GsonBuilder().create();

            Reviews reviews = gson.fromJson(responseJsonStr, Reviews.class);

            Log.d(TAG, reviews.toString());

            return reviews;

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
    protected void onPostExecute(Reviews reviews) {

        mHandler.onPostExecute(reviews);

    }

}
