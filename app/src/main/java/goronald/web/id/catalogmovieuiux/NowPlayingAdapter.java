package goronald.web.id.catalogmovieuiux;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class NowPlayingAdapter extends RecyclerView.Adapter<NowPlayingAdapter.MovieViewHolder> {

    private Context context;
    private ArrayList<Movie> movieList = new ArrayList<>();

    public NowPlayingAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<Movie> getData() {
        return movieList;
    }

    public void setData(ArrayList<Movie> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_now_playing_card, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = getData().get(position);
        Glide.with(context)
                .load(movie.getMoviePoster())
                .override(350, 350)
                .into(holder.imgPosterPhoto);
        holder.tvMovieTitle.setText(movie.getMovieName());
        holder.tvMovieOverview.setText(movie.getMovieOverview());
        holder.tvMovieReleaseDate.setText(movie.getMovieReleaseDate());
        holder.btnDetail.setOnClickListener(new CustomOnItemClickListener(position,
                new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                DetailMovieFragment mDetailMovieFragment = new DetailMovieFragment();
                Bundle mBundle = new Bundle();
                mBundle.putParcelable(DetailMovieFragment.EXTRA_MOVIE, movie);
                mDetailMovieFragment.setArguments(mBundle);
                FragmentManager mFragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.content_main, mDetailMovieFragment,
                        DetailMovieFragment.class.getSimpleName());
                mFragmentTransaction.addToBackStack(null);
                mFragmentTransaction.commit();
            }
        }));
        holder.btnShare.setOnClickListener(new CustomOnItemClickListener(position,
                new CustomOnItemClickListener.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(View view, int position) {
                        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                "[Share] Now Playing Movie - Catalog Movie UI/UX");
                        emailIntent.putExtra(Intent.EXTRA_TEXT,
                                "Now Playing Movie: \nTitle: " +
                                        movie.getMovieName() + "\nRelease Date: " +
                                        movie.getMovieReleaseDate() + "\n\nhttps://www.themoviedb.org");
                        context.startActivity(emailIntent);
                    }
                }));
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPosterPhoto;
        TextView tvMovieTitle, tvMovieOverview, tvMovieReleaseDate;
        Button btnDetail, btnShare;

        public MovieViewHolder(View itemView) {
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
