package me.ulleo.udacity.learn.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.ulleo.udacity.learn.popularmovies.data.DataUtils;
import me.ulleo.udacity.learn.popularmovies.model.Movie;
import me.ulleo.udacity.learn.popularmovies.utils.AsyncTask.UpdateMovieTask;
import me.ulleo.udacity.learn.popularmovies.utils.NetworkUtils;
import me.ulleo.udacity.learn.popularmovies.utils.PictureSize;

public class MovieDetailActivity extends AppCompatActivity {

    private final String TAG = MovieDetailActivity.class.getSimpleName();

    private TextView mTvDetailTitleL, mTvDetailTitleS, mTvDetailReleaseDate, mTvDetailOriginTitle, mTvDetailAverageVote, mTvDetailOverview;

    private ImageView mImgDetailBackDrop, mImgDetailPoster;

    private Menu mMenu;

    private Movie movie;

    private boolean isFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();

        if (intent != null) {
            if (intent.hasExtra(DataUtils.SEND_MOVIE_DETAIL)) {
                movie = intent.getParcelableExtra(DataUtils.SEND_MOVIE_DETAIL);
            }
        }

        initView();

        renderData(movie);



    }

    private void initView() {
        mTvDetailTitleL = (TextView) findViewById(R.id.tv_movie_detail_title_l);
        mTvDetailTitleS = (TextView) findViewById(R.id.tv_movie_detail_title_s);
        mTvDetailReleaseDate = (TextView) findViewById(R.id.tv_movie_detail_release_date);
        mTvDetailOriginTitle = (TextView) findViewById(R.id.tv_movie_detail_origin_title);
        mTvDetailAverageVote = (TextView) findViewById(R.id.tv_movie_detail_average_vote);
        mTvDetailOverview = (TextView) findViewById(R.id.tv_movie_detail_overview);

        mImgDetailBackDrop = (ImageView) findViewById(R.id.image_detail_backdrop);
        mImgDetailPoster = (ImageView) findViewById(R.id.img_movie_detail_poster);
    }

    private void renderData(Movie movie) {
        if (movie == null) {
            Log.e(TAG, "movie is null,cannot render detail!");
            return;
        }
        String backdropImgUrl = NetworkUtils.buildPicPath(PictureSize.PIC_LARGE_500, movie.getBackdropPath());
        String posterImgUrl = NetworkUtils.buildPicPath(PictureSize.PIC_NORMAL_185, movie.getPosterPath());

        mTvDetailTitleL.setText(movie.getTitle());
        mTvDetailTitleS.setText(movie.getTitle());
        mTvDetailReleaseDate.setText(movie.getReleaseDate());
        mTvDetailOriginTitle.setText(movie.getOriginalTitle());
        mTvDetailAverageVote.setText(String.valueOf(movie.getVoteAverage()));
        mTvDetailOverview.setText(movie.getOverview());

        Picasso.with(this).load(backdropImgUrl).into(mImgDetailBackDrop);
        Picasso.with(this).load(posterImgUrl).into(mImgDetailPoster);

        isFavourite = movie.isFavourite();

    }

    private void setFavourite(Movie movie) {

        movie.setFavourite(!isFavourite);

        new UpdateMovieTask(new UpdateMovieTask.OnUpdateMovieHandler() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(Boolean success) {
                if (success) {
                    isFavourite = !isFavourite;
                    setFavouriteIcon(isFavourite);
                } else {
                    Log.i(TAG, "Save Favourite Failed");
                }
            }
        }).execute(movie);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        mMenu = menu;
        setFavouriteIcon(isFavourite);
        return true;
    }

    private void setFavouriteIcon(boolean isFavourite) {
        if (isFavourite) {
            mMenu.findItem(R.id.action_is_favourite).setVisible(true);
            mMenu.findItem(R.id.action_not_favourite).setVisible(false);
        } else {
            mMenu.findItem(R.id.action_is_favourite).setVisible(false);
            mMenu.findItem(R.id.action_not_favourite).setVisible(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.action_is_favourite:
                Log.i(TAG, isFavourite + " is");
                setFavourite(movie);
                break;
            case R.id.action_not_favourite:
                Log.i(TAG, isFavourite + " not");
                setFavourite(movie);
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
