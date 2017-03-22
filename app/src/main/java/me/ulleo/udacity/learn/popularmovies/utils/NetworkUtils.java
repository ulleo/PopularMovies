package me.ulleo.udacity.learn.popularmovies.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import me.ulleo.udacity.learn.popularmovies.BuildConfig;


public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org";

    private static final String PIC_URL = "http://image.tmdb.org/t/p";

    private static final String API_VERSION = "3";

    public static final String POPULAR = "movie/popular";

    public static final String TOP_RATED = "movie/top_rated";

    private static final String API_KEY = BuildConfig.API_KEY;

    private static final String LANGUAGE = "en-US";


    private final static String PAGE_PARAM = "page";
    private final static String LANGUAGE_PARAM = "language";
    private final static String API_KEY_PARAM = "api_key";


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
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                String result = scanner.next();
                //Log.d(TAG, "Result : " + result);
                return result;
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}