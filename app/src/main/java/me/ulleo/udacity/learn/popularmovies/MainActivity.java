package me.ulleo.udacity.learn.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import me.ulleo.udacity.learn.popularmovies.data.DataUtils;
import me.ulleo.udacity.learn.popularmovies.model.Movie;
import me.ulleo.udacity.learn.popularmovies.model.Movies;
import me.ulleo.udacity.learn.popularmovies.model.SearchParam;
import me.ulleo.udacity.learn.popularmovies.utils.FetchMoviesTask;
import me.ulleo.udacity.learn.popularmovies.utils.Sort;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMoviesRecyclerView;

    private ProgressBar mPbLoading;

    private FrameLayout mLoadingFailedLayout;

    private MoviesRecyclerViewAdapter mMoviesRecyclerViewAdapter;

    private MoviesRecyclerViewAdapter.OnMovieItemClickHandler mMovieItemClickHandler;

    private Toast mToast;

    private int page = 1;

    private String sort = Sort.POPULAR;

    private ArrayList<Movie> mMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null && savedInstanceState.containsKey(DataUtils.SAVED_SORT_TYPE)) {
            sort = savedInstanceState.getString(DataUtils.SAVED_SORT_TYPE);
        }

        initView();

        initMovieItemClickHandler();

        setRecyclerView();

        loadData();

    }

    private void initView() {
        mMoviesRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_movie_list);

        mPbLoading = (ProgressBar) findViewById(R.id.progress_movie_list);

        mLoadingFailedLayout = (FrameLayout) findViewById(R.id.layout_loading_failed);

        mLoadingFailedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();
            }
        });

    }

    private void initMovieItemClickHandler() {

        mMovieItemClickHandler = new MoviesRecyclerViewAdapter.OnMovieItemClickHandler() {
            @Override
            public void onClick(Movie movie) {
                /*if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(MainActivity.this, movie.toString(), Toast.LENGTH_SHORT);
                mToast.show();*/

                Intent openDetailIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
                openDetailIntent.putExtra(DataUtils.SEND_MOVIE_DETAIL, movie);
                startActivity(openDetailIntent);
            }
        };
    }

    private void setRecyclerView() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);

        mMoviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(MainActivity.this, mMovieItemClickHandler, mMovieList);

        mMoviesRecyclerView.setAdapter(mMoviesRecyclerViewAdapter);
    }

    private void loadData() {

        SearchParam searchParam = new SearchParam(sort, page);

        new FetchMoviesTask(new FetchMoviesTask.OnFetchMovieHandler() {
            @Override
            public void onPreExecute() {
                showLoadingView();
            }

            @Override
            public void onPostExecute(Movies movies) {
                if (movies == null) {
                    showFailedView();
                } else {
                    showSuccessedView();
                }
                displayData(movies);
            }
        }).execute(searchParam);

    }

    private void refreshData() {
        clearList();
        loadData();
    }

    private void displayData(Movies movies) {
        mMoviesRecyclerViewAdapter.refreshMovieRecyclerView(movies);
    }

    private void clearList() {
        mMoviesRecyclerViewAdapter.clearMovieRecyclerView();
    }

    private void showLoadingView() {
        mLoadingFailedLayout.setVisibility(View.INVISIBLE);
        mPbLoading.setVisibility(View.VISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showSuccessedView() {
        mLoadingFailedLayout.setVisibility(View.INVISIBLE);
        mPbLoading.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showFailedView() {
        mLoadingFailedLayout.setVisibility(View.VISIBLE);
        mPbLoading.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_sort_popular:
                sort = Sort.POPULAR;
                refreshData();
                break;
            case R.id.action_sort_top_rated:
                sort = Sort.TOP_RATED;
                refreshData();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence(DataUtils.SAVED_SORT_TYPE, sort);
        super.onSaveInstanceState(outState);
    }
}
