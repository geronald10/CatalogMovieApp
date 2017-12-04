package goronald.web.id.myfavouritemovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import goronald.web.id.myfavouritemovie.utils.CursorRecyclerAdapter;
import goronald.web.id.myfavouritemovie.utils.CustomOnItemClickListener;

import static goronald.web.id.myfavouritemovie.db.DatabaseContract.FavouriteMovieColumns.OVERVIEW;
import static goronald.web.id.myfavouritemovie.db.DatabaseContract.FavouriteMovieColumns.POSTER;
import static goronald.web.id.myfavouritemovie.db.DatabaseContract.FavouriteMovieColumns.RELEASE_DATE;
import static goronald.web.id.myfavouritemovie.db.DatabaseContract.FavouriteMovieColumns.TITLE;
import static goronald.web.id.myfavouritemovie.db.DatabaseContract.getColumnString;

public class FavouriteAdapter extends CursorRecyclerAdapter<FavouriteAdapter.FavouriteViewHolder> {

    private Context context;

    public FavouriteAdapter(Context context, Cursor cursor) {
        super(cursor);
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favourite_movie, parent, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavouriteViewHolder holder, Cursor cursor) {
        cursor.moveToPosition(cursor.getPosition());
        holder.setData(context, cursor);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    class FavouriteViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPosterPhoto;
        private TextView tvMovieTitle, tvMovieOverview, tvMovieReleaseDate;
        private Button btnDetail, btnShare;

        public FavouriteViewHolder(View itemView) {
            super(itemView);
            imgPosterPhoto = itemView.findViewById(R.id.img_item_photo);
            tvMovieTitle = itemView.findViewById(R.id.tv_item_name);
            tvMovieOverview = itemView.findViewById(R.id.tv_item_overview);
            tvMovieReleaseDate = itemView.findViewById(R.id.tv_item_date);
            btnDetail = itemView.findViewById(R.id.btn_set_detail);
            btnShare = itemView.findViewById(R.id.btn_set_share);
        }

        public void setData(final Context context, final Cursor cursor) {
            Glide.with(context)
                    .load(getColumnString(cursor, POSTER))
                    .into(imgPosterPhoto);
            tvMovieTitle.setText(getColumnString(cursor, TITLE));
            tvMovieOverview.setText(getColumnString(cursor, OVERVIEW));
            tvMovieReleaseDate.setText(getColumnString(cursor, RELEASE_DATE));
            btnDetail.setOnClickListener(new CustomOnItemClickListener(cursor.getPosition(),
                    new CustomOnItemClickListener.OnItemClickCallback() {
                        @Override
                        public void onItemClicked(View view, int position) {
                            cursor.moveToPosition(position);
                            Movie movie = new Movie(cursor);
                            Intent intent = new Intent(context, DetailActivity.class);
                            intent.putExtra(DetailActivity.EXTRA_DETAIL_MOVIE, movie);
                            context.startActivity(intent);
                        }
                    }));
            btnShare.setOnClickListener(new CustomOnItemClickListener(cursor.getPosition(),
                    new CustomOnItemClickListener.OnItemClickCallback() {
                        @Override
                        public void onItemClicked(View view, int position) {
                            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                            emailIntent.setType("text/plain");
                            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                    "[Share] My Favourite Movie - MyFavouriteMovie App");
                            emailIntent.putExtra(Intent.EXTRA_TEXT,
                                    "My Favourite Movie: \nTitle: " +
                                            getColumnString(cursor, TITLE) + "\nRelease Date: " +
                                            getColumnString(cursor, RELEASE_DATE) + "\n\nhttps://www.themoviedb.org");
                            context.startActivity(emailIntent);
                        }
                    }));
        }
    }
}
