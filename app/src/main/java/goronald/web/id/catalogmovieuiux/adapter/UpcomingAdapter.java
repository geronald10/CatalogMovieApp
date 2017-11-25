package goronald.web.id.catalogmovieuiux.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import goronald.web.id.catalogmovieuiux.entity.Movie;
import goronald.web.id.catalogmovieuiux.R;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder> {

    private Context context;
    private ArrayList<Movie> upcomingMovieList = new ArrayList<>();

    public UpcomingAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Movie> getData() {
        return upcomingMovieList;
    }

    public void setData(ArrayList<Movie> upcomingMovieList) {
        this.upcomingMovieList = upcomingMovieList;
        notifyDataSetChanged();
    }

    @Override
    public UpcomingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_upcoming_card, parent, false);
        return new UpcomingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UpcomingViewHolder holder, int position) {
        Movie movie = getData().get(position);
        Glide.with(context)
                .load(movie.getMoviePoster())
                .override(350, 350)
                .into(holder.ivMoviePoster);
        holder.tvMovieTitle.setText(movie.getMovieName());
        holder.tvMovieOverview.setText(movie.getMovieOverview());
        holder.tvMovieReleaseDate.setText(movie.getMovieReleaseDate());
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }


    class UpcomingViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMoviePoster;
        TextView tvMovieTitle;
        TextView tvMovieOverview;
        TextView tvMovieReleaseDate;

        public UpcomingViewHolder(View itemView) {
            super(itemView);
            ivMoviePoster = itemView.findViewById(R.id.img_item_photo);
            tvMovieTitle = itemView.findViewById(R.id.tv_item_name);
            tvMovieOverview = itemView.findViewById(R.id.tv_item_overview);
            tvMovieReleaseDate = itemView.findViewById(R.id.tv_item_date);
        }
    }
}
