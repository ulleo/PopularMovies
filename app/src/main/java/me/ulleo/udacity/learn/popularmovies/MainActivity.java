package me.ulleo.udacity.learn.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import me.ulleo.udacity.learn.popularmovies.model.ReadParam;
import me.ulleo.udacity.learn.popularmovies.model.SearchParam;
import me.ulleo.udacity.learn.popularmovies.utils.AsyncTask.FetchMoviesTask;
import me.ulleo.udacity.learn.popularmovies.utils.AsyncTask.ReadMoviesTask;
import me.ulleo.udacity.learn.popularmovies.utils.AsyncTask.SaveMoviesTask;
import me.ulleo.udacity.learn.popularmovies.utils.Sort;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMoviesRecyclerView;

    private ProgressBar mPbLoading;

    private FrameLayout mLoadingFailedLayout;

    private FrameLayout mLoadingRefreshLayout;

    private GridLayoutManager gridLayoutManager;
    private MoviesRecyclerViewAdapter mMoviesRecyclerViewAdapter;

    private MoviesRecyclerViewAdapter.OnMovieItemClickHandler mMovieItemClickHandler;

    private Toast mToast;

    private int page = 1;

    private boolean isFavourite = false;

    private int tempPosition = 0;

    private static final String TEMP_POSITION = "temp_position";
    private static final String TEMP_FAVOURITE = "temp_favourite";

    private String sort = Sort.POPULAR;

    private ArrayList<Movie> mMovieList;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null && savedInstanceState.containsKey(DataUtils.SAVED_SORT_TYPE) && savedInstanceState.containsKey(TEMP_POSITION) && savedInstanceState.containsKey(TEMP_FAVOURITE)) {
            sort = savedInstanceState.getString(DataUtils.SAVED_SORT_TYPE);
            tempPosition = savedInstanceState.getInt(TEMP_POSITION);
            isFavourite = savedInstanceState.getBoolean(TEMP_FAVOURITE);
        }

        setTitle(sort, isFavourite);

        initView();

        initMovieItemClickHandler();

        setRecyclerView();

        loadLocalData();

    }

    private void initView() {
        mMoviesRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_movie_list);

        mPbLoading = (ProgressBar) findViewById(R.id.progress_movie_list);

        mLoadingFailedLayout = (FrameLayout) findViewById(R.id.layout_loading_failed);

        mLoadingRefreshLayout = (FrameLayout) findViewById(R.id.layout_loading_refresh);

        mLoadingFailedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //refreshData();
                loadData();
            }
        });

        mLoadingRefreshLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
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

        gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);

        mMoviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(MainActivity.this, mMovieItemClickHandler, mMovieList);

        mMoviesRecyclerView.setAdapter(mMoviesRecyclerViewAdapter);
    }

    private void loadData() {

        if (isFavourite) {
            loadLocalData();
            return;
        }

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
                    Log.i(TAG, "Fetch Data Failed");
                } else {
                    //showSuccessedView();
                    Log.i(TAG, "movies sort:" + movies.getSearchType());
                    saveData(movies);
                }
                //displayData(movies);
            }
        }).execute(searchParam);

    }

    private void loadLocalData() {

        ReadParam readParam = new ReadParam(sort, isFavourite);

        new ReadMoviesTask(new ReadMoviesTask.OnReadMoviesHandler() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(Movies movies) {
                if (movies == null) {
                    showFailedView();
                    Log.i(TAG, "Load Local Data Failed");
                } else {
                    if (movies.getMovies().size() > 0) {
                        showSuccessedView();
                    } else {
                        showRefreshView();
                    }
                }
                displayData(movies);

                if (tempPosition != 0) {
                    mMoviesRecyclerView.scrollToPosition(tempPosition);
                    tempPosition = 0;
                }
            }
        }).execute(readParam);
    }

    private void saveData(Movies movies) {
        new SaveMoviesTask(new SaveMoviesTask.OnSaveMovieHandler() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(Boolean success) {
                if (success) {
                    loadLocalData();
                } else {
                    showFailedView();
                    Log.i(TAG, "Save Data Failed");
                }
            }
        }).execute(movies);
    }

    private void refreshData() {
        clearList();
        //loadData();
        loadLocalData();
    }

    private void displayData(Movies movies) {
        mMoviesRecyclerViewAdapter.refreshMovieRecyclerView(movies);
    }

    private void clearList() {
        mMoviesRecyclerViewAdapter.clearMovieRecyclerView();
    }

    private void showLoadingView() {
        mLoadingFailedLayout.setVisibility(View.INVISIBLE);
        mLoadingRefreshLayout.setVisibility(View.INVISIBLE);
        mPbLoading.setVisibility(View.VISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showSuccessedView() {
        mLoadingFailedLayout.setVisibility(View.INVISIBLE);
        mLoadingRefreshLayout.setVisibility(View.INVISIBLE);
        mPbLoading.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showFailedView() {
        mLoadingFailedLayout.setVisibility(View.VISIBLE);
        mLoadingRefreshLayout.setVisibility(View.INVISIBLE);
        mPbLoading.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void showRefreshView() {
        mLoadingFailedLayout.setVisibility(View.INVISIBLE);
        if (isFavourite) {
            mLoadingRefreshLayout.setVisibility(View.INVISIBLE);
        } else {
            mLoadingRefreshLayout.setVisibility(View.VISIBLE);
        }
        mPbLoading.setVisibility(View.INVISIBLE);
        mMoviesRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (isFavourite) {
            menu.findItem(R.id.action_favourite_movie).setIcon(R.drawable.ic_favorite_24dp);
        } else {
            menu.findItem(R.id.action_favourite_movie).setIcon(R.drawable.ic_favorite_border_24dp);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_sort_popular:
                sort = Sort.POPULAR;
                //isFavourite = false;
                refreshData();
                break;
            case R.id.action_sort_top_voted:
                sort = Sort.TOP_RATED;
                //isFavourite = false;
                refreshData();
                break;
            case R.id.action_favourite_movie:
                isFavourite = !isFavourite;
                if (isFavourite) {
                    item.setIcon(R.drawable.ic_favorite_24dp);
                } else {
                    item.setIcon(R.drawable.ic_favorite_border_24dp);
                }
                refreshData();
                break;
        }

        setTitle(sort, isFavourite);

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence(DataUtils.SAVED_SORT_TYPE, sort);
        tempPosition = gridLayoutManager.findFirstVisibleItemPosition();
        outState.putInt(TEMP_POSITION, tempPosition);
        outState.putBoolean(TEMP_FAVOURITE, isFavourite);
        super.onSaveInstanceState(outState);
    }

    private void setTitle(String sort, boolean favourite) {

        if (favourite) {
            getSupportActionBar().setTitle("My Favourite");
            return;
        }

        switch (sort) {
            case Sort.POPULAR:
                getSupportActionBar().setTitle("Popular Movies");
                break;
            case Sort.TOP_RATED:
                getSupportActionBar().setTitle("Top Rated Movies");
        }

    }
}
