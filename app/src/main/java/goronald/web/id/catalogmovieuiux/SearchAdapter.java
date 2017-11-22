package goronald.web.id.catalogmovieuiux;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
                .inflate(R.layout.item_movie_search_card, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        Movie movie = getData().get(position);

        Glide.with(context)
                .load(movie.getMoviePoster())
                .override(350, 350)
                .into(holder.ivMoviePoster);
        holder.tvMovieTitle.setText(movie.getMovieName());
        if (movie.getMovieReleaseYear() != null)
            holder.tvMovieYear.setText(movie.getMovieReleaseYear());
        else
            holder.tvMovieYear.setText("-");
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMoviePoster;
        TextView tvMovieTitle;
        TextView tvMovieYear;

        public SearchViewHolder(View itemView) {
            super(itemView);
            ivMoviePoster = itemView.findViewById(R.id.img_item_photo);
            tvMovieTitle = itemView.findViewById(R.id.tv_item_name);
            tvMovieYear = itemView.findViewById(R.id.tv_item_year);
        }
    }
}
