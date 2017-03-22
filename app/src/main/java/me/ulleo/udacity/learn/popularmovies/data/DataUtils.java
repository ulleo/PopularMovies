package me.ulleo.udacity.learn.popularmovies.data;

import java.util.ArrayList;
import java.util.List;

import me.ulleo.udacity.learn.popularmovies.model.Movie;
import me.ulleo.udacity.learn.popularmovies.model.Movies;

public class DataUtils {

    private static List<Movie> movieList = new ArrayList<>();

    public static List<Movie> getMovies() {
        if (movieList == null) {
            movieList = new ArrayList<>();
        }
        return movieList;
    }

    public static void updateMovies(Movies moviesResponse) {
        if (moviesResponse == null) {
            return;
        }
        List<Movie> tempList = moviesResponse.getMovies();
        if (moviesResponse.getPage() == 1) {
            movieList = tempList;
        } else {
            movieList.addAll(tempList);
        }
    }

    public static void clearMovies() {
        movieList.clear();
    }

}
