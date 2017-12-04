package goronald.web.id.myfavouritemovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class SearchAdapter extends CustomAdapter<Movie> {

    private Context context;

    public SearchAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public long getYourItemId(int position) {
        return mItems.get(position).getId();
    }

    @Override
    public RecyclerView.ViewHolder getYourItemViewHolder(ViewGroup parent) {
        return new SearchViewHolder(mInflater.inflate(R.layout.item_search_movie, parent, false));
    }

    @Override
    public void bindYourViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchViewHolder) {
            Log.d("bindViewHolder", "movie title: " + mItems.get(position).getMovieName());
            SearchViewHolder viewHolder = (SearchViewHolder) holder;
            viewHolder.tvMovieTitle.setText(mItems.get(position).getMovieName());
            Glide.with(context)
                    .load(mItems.get(position).getMoviePoster())
                    .into(viewHolder.ivMoviePoster);
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

    class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMoviePoster;
        TextView tvMovieTitle;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ivMoviePoster = itemView.findViewById(R.id.img_item_photo);
            tvMovieTitle = itemView.findViewById(R.id.tv_item_name);
        }
    }
}
