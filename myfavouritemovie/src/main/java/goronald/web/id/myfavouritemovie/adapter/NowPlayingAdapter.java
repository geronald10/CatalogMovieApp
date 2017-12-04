package goronald.web.id.myfavouritemovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import goronald.web.id.myfavouritemovie.DetailActivity;
import goronald.web.id.myfavouritemovie.R;
import goronald.web.id.myfavouritemovie.entity.Movie;
import goronald.web.id.myfavouritemovie.utils.CustomOnItemClickListener;

public class NowPlayingAdapter extends CustomAdapter<Movie> {

    private Context context;

    public NowPlayingAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public long getYourItemId(int position) {
        return mItems.get(position).getId();
    }

    @Override
    public RecyclerView.ViewHolder getYourItemViewHolder(ViewGroup parent) {
        return new NowPlayingViewHolder(mInflater.inflate(R.layout.item_now_playing_movie, parent, false));
    }

    @Override
    public void bindYourViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NowPlayingViewHolder) {
            NowPlayingViewHolder viewHolder = (NowPlayingViewHolder) holder;
            Glide.with(context)
                    .load(mItems.get(position).getMoviePoster())
                    .override(350, 350)
                    .into(viewHolder.imgPosterPhoto);
            viewHolder.tvMovieTitle.setText(mItems.get(position).getMovieName());
            viewHolder.tvMovieOverview.setText(mItems.get(position).getMovieOverview());
            viewHolder.tvMovieReleaseDate.setText(mItems.get(position).getMovieReleaseDate());
            viewHolder.btnDetail.setOnClickListener(new CustomOnItemClickListener(position,
                    new CustomOnItemClickListener.OnItemClickCallback() {
                        @Override
                        public void onItemClicked(View view, int position) {
                            Intent intent = new Intent(context, DetailActivity.class);
                            intent.putExtra(DetailActivity.EXTRA_DETAIL_MOVIE, mItems.get(position));
                            context.startActivity(intent);
                        }
                    }));
            viewHolder.btnShare.setOnClickListener(new CustomOnItemClickListener(position,
                    new CustomOnItemClickListener.OnItemClickCallback() {
                        @Override
                        public void onItemClicked(View view, int position) {
                            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                            emailIntent.setType("text/plain");
                            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                    "[Share] My Favourite Movie - MyFavouriteMovie App");
                            emailIntent.putExtra(Intent.EXTRA_TEXT,
                                    "My Favourite Movie: \nTitle: " +
                                            mItems.get(position).getMovieName() + "\nRelease Date: " +
                                            mItems.get(position).getMovieReleaseDate() + "\n\nhttps://www.themoviedb.org");
                            context.startActivity(emailIntent);
                        }
                    }));
        }
    }

    class NowPlayingViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPosterPhoto;
        TextView tvMovieTitle, tvMovieOverview, tvMovieReleaseDate;
        Button btnDetail, btnShare;

        public NowPlayingViewHolder(View itemView) {
            super(itemView);
            imgPosterPhoto = itemView.findViewById(R.id.img_item_photo);
            tvMovieTitle = itemView.findViewById(R.id.tv_item_name);
            tvMovieOverview = itemView.findViewById(R.id.tv_item_overview);
            tvMovieReleaseDate = itemView.findViewById(R.id.tv_item_date);
            btnDetail = itemView.findViewById(R.id.btn_set_detail);
            btnShare = itemView.findViewById(R.id.btn_set_share);
        }
    }
}
