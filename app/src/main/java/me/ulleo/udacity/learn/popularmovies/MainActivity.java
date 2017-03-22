package me.ulleo.udacity.learn.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import me.ulleo.udacity.learn.popularmovies.model.Movie;
import me.ulleo.udacity.learn.popularmovies.model.Movies;
import me.ulleo.udacity.learn.popularmovies.model.SearchParam;
import me.ulleo.udacity.learn.popularmovies.utils.FetchMoviesTask;
import me.ulleo.udacity.learn.popularmovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mMoviesRecyclerView;

    private MoviesRecyclerViewAdapter mMoviesRecyclerViewAdapter;

    private MoviesRecyclerViewAdapter.OnMovieItemClickHandler mMovieItemClickHandler;

    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initMovieItemClickHandler();

        setRecyclerView();

        loadData();

    }

    private void initView() {
        mMoviesRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_movie_list);

    }

    private void initMovieItemClickHandler() {

        mMovieItemClickHandler = new MoviesRecyclerViewAdapter.OnMovieItemClickHandler() {
            @Override
            public void onClick(Movie movie) {
                if (mToast != null) {
                    mToast.cancel();
                }
                mToast = Toast.makeText(MainActivity.this, movie.toString(), Toast.LENGTH_SHORT);
                mToast.show();
            }
        };
    }

    private void setRecyclerView() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);

        mMoviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(MainActivity.this, mMovieItemClickHandler);

        mMoviesRecyclerView.setAdapter(mMoviesRecyclerViewAdapter);
    }

    private void loadData() {

        SearchParam searchParam = new SearchParam(NetworkUtils.POPULAR, 1);

        new FetchMoviesTask(new FetchMoviesTask.OnFetchMovieHandler() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(Movies movies) {
                displayData(movies);
            }
        }).execute(searchParam);

    }

    private void displayData(Movies movies) {
        mMoviesRecyclerViewAdapter.updateMovieRecyclerView(movies);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_refresh:
                loadData();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
