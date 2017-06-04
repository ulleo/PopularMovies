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
import me.ulleo.udacity.learn.popularmovies.model.Video;
import me.ulleo.udacity.learn.popularmovies.model.Videos;


public class VideosRecyclerViewAdapter extends RecyclerView.Adapter<VideosRecyclerViewAdapter.VideosViewHolder> {

    private final Context mContext;
    private final OnVideoItemClickHandler clickHandler;
    private List<Video> mVideosList;

    public VideosRecyclerViewAdapter(Context context, @NonNull OnVideoItemClickHandler onVideoItemClickHandler, @NonNull List<Video> movieList) {
        mContext = context;
        clickHandler = onVideoItemClickHandler;
        mVideosList = movieList;
    }

    public interface OnVideoItemClickHandler {
        void onClick(Video video);
    }

    @Override
    public VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_video, parent, false);
        return new VideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideosViewHolder holder, int position) {
        Video video = mVideosList.get(position);
        holder.mTextViewVideoName.setText(video.getName());
    }

    @Override
    public int getItemCount() {
        return mVideosList.size();
    }

    public class VideosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public final TextView mTextViewVideoName;

        public VideosViewHolder(View itemView) {
            super(itemView);

            mTextViewVideoName = (TextView) itemView.findViewById(R.id.tv_detail_video_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Video video = mVideosList.get(position);
            clickHandler.onClick(video);
        }
    }

    public void refreshVideosRecyclerView(Videos videos) {
        mVideosList = videos.getResults();
        notifyDataSetChanged();
    }


}
