package me.ulleo.udacity.learn.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import me.ulleo.udacity.learn.popularmovies.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org";

    private static final String PIC_URL = "http://image.tmdb.org/t/p";

    private static final String API_VERSION = "3";

    private static final String MOVIE = "movie";
    private static final String VIDEO = "videos";
    private static final String REVIEW = "reviews";

    private static final String API_KEY = BuildConfig.API_KEY;

    private static final String LANGUAGE = "en-US";


    private static final String PAGE_PARAM = "page";
    private static final String LANGUAGE_PARAM = "language";
    private static final String API_KEY_PARAM = "api_key";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    public static URL buildUrl(String searchType, int page) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(API_VERSION)
                .appendEncodedPath(searchType)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .appendQueryParameter(PAGE_PARAM, String.valueOf(page))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildMoviesUrl(int id, int page) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(API_VERSION)
                .appendEncodedPath(MOVIE)
                .appendEncodedPath(String.valueOf(id))
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .appendQueryParameter(PAGE_PARAM, String.valueOf(page))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildVideosUrl(int id, int page) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(API_VERSION)
                .appendEncodedPath(MOVIE)
                .appendEncodedPath(String.valueOf(id))
                .appendEncodedPath(VIDEO)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .appendQueryParameter(PAGE_PARAM, String.valueOf(page))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildReviewsUrl(int id, int page) {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(API_VERSION)
                .appendEncodedPath(MOVIE)
                .appendEncodedPath(String.valueOf(id))
                .appendEncodedPath(REVIEW)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .appendQueryParameter(PAGE_PARAM, String.valueOf(page))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String buildPicPath(String size, String path) {
        Uri builtUri = Uri.parse(PIC_URL).buildUpon()
                .appendPath(size)
                .appendEncodedPath(path).build();
        String url = builtUri.toString();
        Log.d(TAG, "Built Pic URI " + url);
        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        //return httpURLConnectionRun(url);
        return okHttpRun(url);
    }

    private static String okHttpRun(URL url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    private static String httpURLConnectionRun(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(30000);
        urlConnection.setReadTimeout(10000);
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                String result = scanner.next();
                Log.d(TAG, "Result : " + result);
                return result;
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}