package goronald.web.id.catalogmovieuiux;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailMovieFragment extends Fragment {

    private static final String TAG = DetailMovieFragment.class.getSimpleName();

    public static String EXTRA_MOVIE = "extra_movie";
    private ImageView ivMoviePoster, ivMovieBackdrop;
    private TextView tvMovieTitle, tvMovieReleaseDate, tvMovieOverview, tvMovieRate, tvMovieYear;
    private RatingBar rateMovie;
    private Toolbar toolbar;
    private AppBarLayout parentAppBarLayout;

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
        Movie movie = getArguments().getParcelable(EXTRA_MOVIE);
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
}
