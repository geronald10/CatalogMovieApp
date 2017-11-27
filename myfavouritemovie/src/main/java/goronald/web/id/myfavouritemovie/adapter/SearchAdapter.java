package goronald.web.id.myfavouritemovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import goronald.web.id.myfavouritemovie.R;
import goronald.web.id.myfavouritemovie.entity.Movie;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private ArrayList<Movie> searchMovieList = new ArrayList<>();
    private Context context;

    public SearchAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Movie> searchMovieList) {
        this.searchMovieList = searchMovieList;
        notifyDataSetChanged();
    }

    public ArrayList<Movie> getData() {
        return searchMovieList;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_movie, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        Movie movie = getData().get(position);

        Glide.with(context)
                .load(movie.getMoviePoster())
                .into(holder.ivMoviePoster);
        holder.tvMovieTitle.setText(movie.getMovieName());
    }

    @Override
    public int getItemCount() {
        return getData().size();
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
