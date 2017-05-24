package me.ulleo.udacity.learn.popularmovies.model;

import android.support.annotation.NonNull;

import me.ulleo.udacity.learn.popularmovies.data.MovieProvider;
import me.ulleo.udacity.learn.popularmovies.utils.Sort;

/**
 * Created by ulleo on 2017/5/23.
 */

public class ReadParam {

    private String sort;

    private boolean isFavourite;

    private int movieId;

    public ReadParam(@NonNull String sort, boolean isFavourite) {
        checkOrder(sort);
        this.isFavourite = isFavourite;
        this.movieId = -1;
    }

    public ReadParam(@NonNull String sort, boolean isFavourite, int movieId) {
        checkOrder(sort);
        this.isFavourite = isFavourite;
        this.movieId = movieId;
    }

    public ReadParam(int movieId) {
        this.movieId = movieId;
    }

    private void checkOrder(String sort) {
        if (sort.equals(Sort.TOP_RATED)) {
            this.sort = MovieProvider.SORT_RATED;
        } else {
            this.sort = MovieProvider.SORT_POPULARITY;
        }
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
