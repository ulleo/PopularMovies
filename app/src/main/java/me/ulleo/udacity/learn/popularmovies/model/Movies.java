package me.ulleo.udacity.learn.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Movies {

    @Expose
    private int page;

    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    @SerializedName("total_results")
    @Expose
    private int totalResults;

    @SerializedName("results")
    @Expose
    private List<Movie> movies = new ArrayList<>();

    private String searchType;

    public Movies() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    @Override
    public String toString() {
        return "Movies{" +
                "page=" + page +
                ", totalPages=" + totalPages +
                ", totalResults=" + totalResults +
                ", movies=" + movies +
                ", searchType='" + searchType + '\'' +
                '}';
    }
}
