package me.ulleo.udacity.learn.popularmovies.model;

import android.support.annotation.NonNull;

/**
 * Created by ulleo on 2017/3/22.
 */

public class SearchParam {

    private String searchType;
    private int page;

    public SearchParam(@NonNull String searchType, int page) {
        this.searchType = searchType;
        this.page = page;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
