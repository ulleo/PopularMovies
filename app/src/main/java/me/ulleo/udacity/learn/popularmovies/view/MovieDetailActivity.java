package me.ulleo.udacity.learn.popularmovies.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.ulleo.udacity.learn.popularmovies.R;
import me.ulleo.udacity.learn.popularmovies.data.DataUtils;
import me.ulleo.udacity.learn.popularmovies.model.Movie;
import me.ulleo.udacity.learn.popularmovies.model.Movies;
import me.ulleo.udacity.learn.popularmovies.model.ReadParam;
import me.ulleo.udacity.learn.popularmovies.model.Review;
import me.ulleo.udacity.learn.popularmovies.model.Reviews;
import me.ulleo.udacity.learn.popularmovies.model.Video;
import me.ulleo.udacity.learn.popularmovies.model.Videos;
import me.ulleo.udacity.learn.popularmovies.utils.AsyncTask.FetchMovieTask;
import me.ulleo.udacity.learn.popularmovies.utils.AsyncTask.FetchReviewsTask;
import me.ulleo.udacity.learn.popularmovies.utils.AsyncTask.FetchVideoTask;
import me.ulleo.udacity.learn.popularmovies.utils.AsyncTask.ReadMoviesTask;
import me.ulleo.udacity.learn.popularmovies.utils.AsyncTask.UpdateMovieTask;
import me.ulleo.udacity.learn.popularmovies.utils.NetworkUtils;
import me.ulleo.udacity.learn.popularmovies.utils.PictureSize;
import me.ulleo.udacity.learn.popularmovies.view.adapter.ReviewsRecyclerViewAdapter;
import me.ulleo.udacity.learn.popularmovies.view.adapter.VideosRecyclerViewAdapter;

public class MovieDetailActivity extends AppCompatActivity {

    private final String TAG = MovieDetailActivity.class.getSimpleName();

    private TextView mTvDetailTitleL, mTvDetailTitleS, mTvDetailReleaseDate, mTvDetailOriginTitle, mTvDetailAverageVote, mTvDetailOverview, mTvRuntime;

    private ImageView mImgDetailBackDrop, mImgDetailPoster;

    private LinearLayoutManager mVideosLayoutManager, mReviewsLayoutManager;

    private ReviewsRecyclerViewAdapter mReviewsRecyclerViewAdapter;

    private VideosRecyclerViewAdapter mVideosRecyclerViewAdapter;

    private RecyclerView mVideosRv, mReviewsRv;

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

        initRecyclerViews();
    }

    private void initView() {
        mTvDetailTitleL = (TextView) findViewById(R.id.tv_movie_detail_title_l);
        mTvDetailTitleS = (TextView) findViewById(R.id.tv_movie_detail_title_s);
        mTvDetailReleaseDate = (TextView) findViewById(R.id.tv_movie_detail_release_date);
        mTvDetailOriginTitle = (TextView) findViewById(R.id.tv_movie_detail_origin_title);
        mTvDetailAverageVote = (TextView) findViewById(R.id.tv_movie_detail_average_vote);
        mTvDetailOverview = (TextView) findViewById(R.id.tv_movie_detail_overview);
        mTvRuntime = (TextView) findViewById(R.id.tv_movie_detail_runtime);

        mImgDetailBackDrop = (ImageView) findViewById(R.id.image_detail_backdrop);
        mImgDetailPoster = (ImageView) findViewById(R.id.img_movie_detail_poster);

        mVideosRv = (RecyclerView) findViewById(R.id.recycler_view_video_list);
        mReviewsRv = (RecyclerView) findViewById(R.id.recycler_view_remark_list);
    }

    private void initRecyclerViews() {
        mReviewsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mVideosLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mVideosRv.setLayoutManager(mVideosLayoutManager);
        mReviewsRv.setLayoutManager(mReviewsLayoutManager);

        mVideosRv.setHasFixedSize(true);
        mReviewsRv.setHasFixedSize(true);

        mVideosRecyclerViewAdapter = new VideosRecyclerViewAdapter(this, new VideosRecyclerViewAdapter.OnVideoItemClickHandler() {
            @Override
            public void onClick(Video video) {
                watchYoutubeVideo(video.getKey());
            }
        }, new ArrayList<Video>());
        mReviewsRecyclerViewAdapter = new ReviewsRecyclerViewAdapter(this, new ReviewsRecyclerViewAdapter.OnReviewItemClickHandler() {
            @Override
            public void onClick(Review review) {

            }
        }, new ArrayList<Review>());

        mVideosRv.setAdapter(mVideosRecyclerViewAdapter);
        mReviewsRv.setAdapter(mReviewsRecyclerViewAdapter);

    }

    private void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private void renderData(Movie movie) {
        if (movie == null) {
            Log.e(TAG, "movie is null,cannot render detail!");
            return;
        }

        ReadParam readParam = new ReadParam(movie.getId());

        new ReadMoviesTask(new ReadMoviesTask.OnReadMoviesHandler() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(Movies movies) {
                if (movies == null || movies.getMovies().size() == 0) {
                    Log.e(TAG, "movie is null,cannot render detail!");
                    return;
                }

                Movie movie = movies.getMovies().get(0);

                String backdropImgUrl = NetworkUtils.buildPicPath(PictureSize.PIC_LARGE_500, movie.getBackdropPath());
                String posterImgUrl = NetworkUtils.buildPicPath(PictureSize.PIC_NORMAL_185, movie.getPosterPath());

                mTvDetailTitleL.setText(movie.getTitle());
                mTvDetailTitleS.setText(movie.getTitle());
                mTvDetailReleaseDate.setText(movie.getReleaseDate());
                mTvDetailOriginTitle.setText(movie.getOriginalTitle());
                mTvDetailAverageVote.setText(String.valueOf(movie.getVoteAverage()));
                mTvDetailOverview.setText(movie.getOverview());

                Picasso.with(MovieDetailActivity.this).load(backdropImgUrl).into(mImgDetailBackDrop);
                Picasso.with(MovieDetailActivity.this).load(posterImgUrl).into(mImgDetailPoster);

                isFavourite = movie.isFavourite();

                setFavouriteIcon(isFavourite);

                getMovieVideos(movie.getId(), 1);
                getMovie(movie.getId(), 1);
                getReviews(movie.getId(), 1);
            }
        }).execute(readParam);


    }

    private void getMovieVideos(int id, int page) {
        new FetchVideoTask(new FetchVideoTask.OnFetchVideoHandler() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(Videos videos) {
                mVideosRecyclerViewAdapter.refreshVideosRecyclerView(videos);
            }
        }).execute(id);
    }

    private void getMovie(int id, int page) {
        new FetchMovieTask(new FetchMovieTask.OnFetchMovieHandler() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(Movie movie) {
                mTvRuntime.setText(movie.getRuntime() + " min");
            }
        }).execute(id);
    }

    private void getReviews(int id, int page) {
        new FetchReviewsTask(new FetchReviewsTask.OnFetcReviewsHandler() {
            @Override
            public void onPreExecute() {

            }

            @Override
            public void onPostExecute(Reviews reviews) {
                mReviewsRecyclerViewAdapter.refreshReviewsRecyclerView(reviews);
            }
        }).execute(id);
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
        if (mMenu == null) {
            return;
        }
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
