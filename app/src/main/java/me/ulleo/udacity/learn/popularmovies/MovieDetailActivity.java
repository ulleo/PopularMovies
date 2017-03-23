package me.ulleo.udacity.learn.popularmovies;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.ulleo.udacity.learn.popularmovies.data.DataUtils;
import me.ulleo.udacity.learn.popularmovies.model.Movie;
import me.ulleo.udacity.learn.popularmovies.utils.DensityUtil;
import me.ulleo.udacity.learn.popularmovies.utils.NetworkUtils;
import me.ulleo.udacity.learn.popularmovies.utils.PictureSize;

public class MovieDetailActivity extends AppCompatActivity {

    private final String TAG = MovieDetailActivity.class.getSimpleName();

    private TextView mTvDetailTitleL, mTvDetailTitleS, mTvDetailReleaseDate, mTvDetailOriginTitle, mTvDetailAverageVote, mTvDetailOverview;

    private ImageView mImgDetailBackDrop, mImgDetailPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();

        Movie movie = null;

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

        //fixLongTitleDisplay(movie.getTitle());

    }

    private void fixLongTitleDisplay(String text) {

        float mTvDetailTitleTextSizeL = mTvDetailTitleL.getTextSize();
        int mTvDetailTitleTextSizeLdp = DensityUtil.px2dip(this, mTvDetailTitleTextSizeL);
        float mTvDetailTitleWidthL = DensityUtil.dip2px(this, 240);

        reSizeTextView(mTvDetailTitleL, text, mTvDetailTitleWidthL, mTvDetailTitleTextSizeLdp);

        Log.d(TAG, "mTvDetailTitleTextSizeL : " + mTvDetailTitleTextSizeL + "\nmTvDetailTitleTextSizeLdp : " + mTvDetailTitleTextSizeLdp);

        float mTvDetailTitleTextSizeS = mTvDetailTitleS.getTextSize();
        int mTvDetailTitleTextSizeSdp = DensityUtil.px2dip(this, mTvDetailTitleTextSizeS);
        float mTvDetailTitleWidthS = DensityUtil.dip2px(this, 240);

        reSizeTextView(mTvDetailTitleS, text, mTvDetailTitleWidthS, mTvDetailTitleTextSizeSdp);
    }

    private void reSizeTextView(TextView textView, String text, float maxWidth, int textSizeInDp) {

        Paint paint = textView.getPaint();
        float textWidth = paint.measureText(text);

        if (textWidth > maxWidth) {
            for (; textSizeInDp > 0; textSizeInDp--) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSizeInDp);
                paint = textView.getPaint();
                textWidth = paint.measureText(text);
                if (textWidth / 1.5 <= maxWidth) {
                    break;
                }
            }
        }
        textView.invalidate();
    }
}
