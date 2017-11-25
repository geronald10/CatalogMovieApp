package goronald.web.id.catalogmovieuiux;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import goronald.web.id.catalogmovieuiux.entity.Movie;

import static goronald.web.id.catalogmovieuiux.db.DatabaseContract.CONTENT_URI;
import static goronald.web.id.catalogmovieuiux.db.DatabaseContract.FavouriteMovieColumns.BACKDROP;
import static goronald.web.id.catalogmovieuiux.db.DatabaseContract.FavouriteMovieColumns.MOVIE_ID;
import static goronald.web.id.catalogmovieuiux.db.DatabaseContract.FavouriteMovieColumns.OVERVIEW;
import static goronald.web.id.catalogmovieuiux.db.DatabaseContract.FavouriteMovieColumns.POSTER;
import static goronald.web.id.catalogmovieuiux.db.DatabaseContract.FavouriteMovieColumns.RELEASE_DATE;
import static goronald.web.id.catalogmovieuiux.db.DatabaseContract.FavouriteMovieColumns.TITLE;
import static goronald.web.id.catalogmovieuiux.db.DatabaseContract.FavouriteMovieColumns.VOTE_AVG;

public class DetailMovieFragment extends Fragment {

    private static final String TAG = DetailMovieFragment.class.getSimpleName();

    public static String EXTRA_MOVIE = "extra_movie";
    public static String EXTRA_MOVIE_ID_URI = "extra_uri";

    private Movie movie;
    private ImageView ivMoviePoster, ivMovieBackdrop;
    private TextView tvMovieTitle, tvMovieReleaseDate, tvMovieOverview, tvMovieRate, tvMovieYear;
    private RatingBar rateMovie;
    private Toolbar toolbar;
    private AppBarLayout parentAppBarLayout;
    private boolean isFavourited;

    public DetailMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hideParentAppBar();
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_detail_movie, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movie = getArguments().getParcelable(EXTRA_MOVIE);
        toolbar = view.findViewById(R.id.toolbar_fragment);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setTitle(movie.getMovieName());
        ((AppCompatActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);

        ivMovieBackdrop = view.findViewById(R.id.img_item_photo);
        ivMoviePoster = view.findViewById(R.id.iv_movie_poster);
        tvMovieTitle = view.findViewById(R.id.tv_item_name);
        tvMovieReleaseDate = view.findViewById(R.id.tv_item_date);
        tvMovieOverview = view.findViewById(R.id.tv_item_overview);
        tvMovieYear = view.findViewById(R.id.tv_item_year);
        tvMovieRate = view.findViewById(R.id.tv_item_rate);
        rateMovie = view.findViewById(R.id.rateBar);

        Glide.with(getContext())
                .load(movie.getMovieBackdrop())
                .into(ivMovieBackdrop);
        Glide.with(getContext())
                .load(movie.getMoviePoster())
                .into(ivMoviePoster);
        tvMovieTitle.setText(movie.getMovieName());
        tvMovieRate.setText(String.valueOf(movie.getMovieVoteAverage()));
        tvMovieYear.setText(movie.getMovieReleaseYear());
        tvMovieReleaseDate.setText(movie.getMovieReleaseDate());
        tvMovieOverview.setText(movie.getMovieOverview());
        rateMovie.setNumStars(5);
        rateMovie.setStepSize(0.1f);
        rateMovie.setRating((float) movie.getMovieVoteAverage() / 2.0f);
        rateMovie.setIsIndicator(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "onOptionItemSelected");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(this);
                fragmentTransaction.commit();
                showParentAppBar();
                fragmentManager.popBackStack();
                break;
            case R.id.action_favourite:
                StateListDrawable stateListDrawable = (StateListDrawable)
                        getResources().getDrawable(R.drawable.btn_favourite_star);
                if (!item.isChecked()) {
                    item.setChecked(true);
                    addToFavourite(movie);
                } else {
                    item.setChecked(false);
                    removeFromFavourite();
                }
                int[] state = {item.isChecked() ? android.R.attr.state_checked : android.R.attr.state_empty};
                stateListDrawable.setState(state);
                item.setIcon(stateListDrawable.getCurrent());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideParentAppBar() {
        parentAppBarLayout = ((AppCompatActivity) getActivity())
                .findViewById(R.id.fragment_app_bar_layout);
        parentAppBarLayout.setVisibility(View.GONE);
    }

    private void showParentAppBar() {
        parentAppBarLayout = ((AppCompatActivity) getActivity())
                .findViewById(R.id.fragment_app_bar_layout);
        parentAppBarLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Uri uri = Uri.parse(getArguments().getString(EXTRA_MOVIE_ID_URI));
        Cursor cursor = getContext().getContentResolver()
                .query(uri,
                        null,
                        null,
                        null,
                        null);
        MenuItem item = menu.findItem(R.id.action_favourite);
        StateListDrawable stateListDrawable = (StateListDrawable)
                getResources().getDrawable(R.drawable.btn_favourite_star);
        Log.d(TAG, String.valueOf(cursor.getCount()));
        if (cursor.getCount() > 0) {
            item.setChecked(true);
        } else {
            item.setChecked(false);
        }
        int[] state = {item.isChecked() ? android.R.attr.state_checked : android.R.attr.state_empty};
        stateListDrawable.setState(state);
        item.setIcon(stateListDrawable.getCurrent());
        super.onPrepareOptionsMenu(menu);
    }

    private void addToFavourite(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(MOVIE_ID, movie.getMovieId());
        values.put(TITLE, movie.getMovieName());
        values.put(OVERVIEW, movie.getMovieOverview());
        values.put(RELEASE_DATE, movie.getMovieReleaseDate());
        values.put(POSTER, movie.getMoviePoster());
        values.put(BACKDROP, movie.getMovieBackdrop());
        values.put(VOTE_AVG, movie.getMovieVoteAverage());
        getContext().getContentResolver().insert(CONTENT_URI, values);
        Toast.makeText(getContext(), R.string.add_to_favourite, Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavourite() {
        Uri uri = null;
        if (getArguments().getString(EXTRA_MOVIE_ID_URI) != null) {
            uri = Uri.parse(getArguments().getString(EXTRA_MOVIE_ID_URI));
            getContext().getContentResolver().delete(uri, null, null);
        }
        Toast.makeText(getContext(), R.string.remove_from_favourite, Toast.LENGTH_SHORT).show();
    }
}
