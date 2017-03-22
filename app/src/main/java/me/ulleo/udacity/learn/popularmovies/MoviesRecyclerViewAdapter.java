package me.ulleo.udacity.learn.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import me.ulleo.udacity.learn.popularmovies.data.DataUtils;
import me.ulleo.udacity.learn.popularmovies.model.Movie;
import me.ulleo.udacity.learn.popularmovies.model.Movies;
import me.ulleo.udacity.learn.popularmovies.utils.NetworkUtils;


public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.MoviesViewHolder> {

    private final Context mContext;
    private final OnMovieItemClickHandler clickHandler;

    public MoviesRecyclerViewAdapter(Context context, @NonNull OnMovieItemClickHandler onMovieItemClickHandler) {
        mContext = context;
        clickHandler = onMovieItemClickHandler;
    }

    public interface OnMovieItemClickHandler {
        void onClick(Movie movie);
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {
        Movie movie = DataUtils.getMovies().get(position);
        holder.mTextViewMovieTitle.setText(movie.getTitle());
        Picasso.with(mContext).load(NetworkUtils.buildPicPath("w185",movie.getPosterPath())).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return DataUtils.getMovies().size();
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mImageView;
        public final TextView mTextViewMovieTitle;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_movie_item);
            mTextViewMovieTitle = (TextView) itemView.findViewById(R.id.tv_movie_item);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Movie movie = DataUtils.getMovies().get(position);
            clickHandler.onClick(movie);
        }
    }

    public void updateMovieRecyclerView(Movies movies) {
        DataUtils.updateMovies(movies);
        notifyDataSetChanged();
    }

}
