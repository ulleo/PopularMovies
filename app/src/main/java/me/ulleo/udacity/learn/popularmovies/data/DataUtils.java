package me.ulleo.udacity.learn.popularmovies.data;

import java.util.ArrayList;
import java.util.List;

import me.ulleo.udacity.learn.popularmovies.model.Movie;
import me.ulleo.udacity.learn.popularmovies.model.Movies;

public class DataUtils {

    public static final String SAVED_MOVIE_LIST = "SAVED_MOVIE_LIST";
    public static final String SAVED_SORT_TYPE = "SAVED_SORT_TYPE";
    public static final String SAVED_MOVIE_DETAIL = "SAVED_MOVIE_DETAIL";
    public static final String SEND_MOVIE_DETAIL = "SEND_MOVIE_DETAIL";


    public static List<Movie> getMovies(List<Movie> movieList) {
        if (movieList == null) {
            movieList = new ArrayList<>();
        }
        return movieList;
    }

    public static List<Movie> updateMovies(List<Movie> movieList, Movies moviesResponse) {
        if (movieList == null) {
            movieList = new ArrayList<>();
        }
        if (moviesResponse == null) {
            return movieList;
        }
        List<Movie> tempList = moviesResponse.getMovies();
        if (moviesResponse.getPage() == 1) {
            movieList = tempList;
        } else {
            movieList.addAll(tempList);
        }
        return movieList;
    }

    public static void clearMovies(List<Movie> movieList) {
        movieList.clear();
    }

}
