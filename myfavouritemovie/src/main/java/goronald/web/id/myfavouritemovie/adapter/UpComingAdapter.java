package goronald.web.id.myfavouritemovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import goronald.web.id.myfavouritemovie.DetailActivity;
import goronald.web.id.myfavouritemovie.R;
import goronald.web.id.myfavouritemovie.entity.Movie;
import goronald.web.id.myfavouritemovie.utils.CustomOnItemClickListener;

import static goronald.web.id.myfavouritemovie.DetailActivity.EXTRA_DETAIL_MOVIE;

public class UpComingAdapter extends CustomAdapter<Movie> {

    private Context context;

    public UpComingAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public long getYourItemId(int position) {
        return mItems.get(position).getId();
    }

    @Override
    public RecyclerView.ViewHolder getYourItemViewHolder(ViewGroup parent) {
        return new UpComingViewHolder(mInflater.inflate(R.layout.item_upcoming_movie, parent, false));
    }

    @Override
    public void bindYourViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UpComingViewHolder) {
            UpComingViewHolder viewHolder = (UpComingViewHolder) holder;
            Glide.with(context)
                    .load(mItems.get(position).getMoviePoster())
                    .override(350, 350)
                    .into(viewHolder.ivMoviePoster);
            viewHolder.tvMovieTitle.setText(mItems.get(position).getMovieName());
            viewHolder.tvMovieOverview.setText(mItems.get(position).getMovieOverview());
            viewHolder.tvMovieReleaseDate.setText(mItems.get(position).getMovieReleaseDate());
            viewHolder.itemView.setOnClickListener(new CustomOnItemClickListener(position,
                    new CustomOnItemClickListener.OnItemClickCallback() {
                        @Override
                        public void onItemClicked(View view, int position) {
                            Intent intent = new Intent(context, DetailActivity.class);
                            intent.putExtra(EXTRA_DETAIL_MOVIE, mItems.get(position));
                            context.startActivity(intent);
                        }
                    }));
        }
    }


    class UpComingViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMoviePoster;
        TextView tvMovieTitle;
        TextView tvMovieOverview;
        TextView tvMovieReleaseDate;

        public UpComingViewHolder(View itemView) {
            super(itemView);
            ivMoviePoster = itemView.findViewById(R.id.img_item_photo);
            tvMovieTitle = itemView.findViewById(R.id.tv_item_name);
            tvMovieOverview = itemView.findViewById(R.id.tv_item_overview);
            tvMovieReleaseDate = itemView.findViewById(R.id.tv_item_date);
        }
    }
}
