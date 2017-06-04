package me.ulleo.udacity.learn.popularmovies.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ulleo on 2017/5/31.
 */

public class Videos {

    @Expose
    private int id;
    @Expose
    private List<Video> results = new ArrayList<>();

    public Videos() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Videos{" +
                "id=" + id +
                ", results=" + results +
                '}';
    }
}

