package goronald.web.id.catalogmovieuiux.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

import goronald.web.id.catalogmovieuiux.DetailMovieFragment;
import goronald.web.id.catalogmovieuiux.R;
import goronald.web.id.catalogmovieuiux.entity.Movie;
import goronald.web.id.catalogmovieuiux.utility.CustomOnItemClickListener;

import static goronald.web.id.catalogmovieuiux.db.DatabaseContract.CONTENT_URI;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {
    private Cursor listFavourites;
    private Context context;

    public FavouriteAdapter(Context context) {
        this.context = context;
    }

    public void setListFavourites(Cursor listFavourites) {
        this.listFavourites = listFavourites;
        notifyDataSetChanged();
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_now_playing_card, parent, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavouriteViewHolder holder, int position) {
        final Movie movie = getItem(position);
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
                        Uri uri = Uri.parse(CONTENT_URI + "/" + movie.getMovieId());
                        mBundle.putString(DetailMovieFragment.EXTRA_MOVIE_ID_URI, String.valueOf(uri));
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
                                "[Share] My Favourite Movie - Catalog Movie UI/UX");
                        emailIntent.putExtra(Intent.EXTRA_TEXT,
                                "My Favourite Movie: \nTitle: " +
                                        movie.getMovieName() + "\nRelease Date: " +
                                        movie.getMovieReleaseDate() + "\n\nhttps://www.themoviedb.org");
                        context.startActivity(emailIntent);
                    }
                }));
    }

    @Override
    public int getItemCount() {
        if (listFavourites == null)
            return 0;
        return listFavourites.getCount();
    }

    private Movie getItem(int position) {
        if (!listFavourites.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new Movie(listFavourites);
    }

    class FavouriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPosterPhoto;
        TextView tvMovieTitle, tvMovieOverview, tvMovieReleaseDate;
        Button btnDetail, btnShare;

        public FavouriteViewHolder(View itemView) {
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
