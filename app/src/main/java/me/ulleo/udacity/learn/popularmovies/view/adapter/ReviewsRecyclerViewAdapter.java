package me.ulleo.udacity.learn.popularmovies.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.ulleo.udacity.learn.popularmovies.R;
import me.ulleo.udacity.learn.popularmovies.model.Review;
import me.ulleo.udacity.learn.popularmovies.model.Reviews;


public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ReviewsViewHolder> {

    private final Context mContext;
    private final OnReviewItemClickHandler clickHandler;
    private List<Review> mReviewsList;

    public ReviewsRecyclerViewAdapter(Context context, @NonNull OnReviewItemClickHandler onReviewItemClickHandler, @NonNull List<Review> movieList) {
        mContext = context;
        clickHandler = onReviewItemClickHandler;
        mReviewsList = movieList;
    }

    public interface OnReviewItemClickHandler {
        void onClick(Review review);
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_review, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        Review review = mReviewsList.get(position);

        holder.mTextViewReviewAuthor.setText(review.getAuthor());
        holder.mTextViewReviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewsList.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public final TextView mTextViewReviewAuthor, mTextViewReviewContent;

        public ReviewsViewHolder(View itemView) {
            super(itemView);

            mTextViewReviewAuthor = (TextView) itemView.findViewById(R.id.tv_detail_review_author);
            mTextViewReviewContent = (TextView) itemView.findViewById(R.id.tv_detail_review_content);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Review review = mReviewsList.get(position);
            clickHandler.onClick(review);
        }
    }

    public void refreshReviewsRecyclerView(Reviews reviews) {
        mReviewsList = reviews.getResults();
        notifyDataSetChanged();
    }


}
