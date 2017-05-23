package me.ulleo.udacity.learn.popularmovies;

import android.app.Application;

/**
 * Created by ulleo on 2017/5/23.
 */

public class MovieApplication extends Application {
    private static MovieApplication instance;

    public static MovieApplication getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
