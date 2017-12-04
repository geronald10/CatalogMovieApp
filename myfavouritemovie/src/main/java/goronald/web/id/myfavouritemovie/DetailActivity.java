package goronald.web.id.myfavouritemovie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import goronald.web.id.myfavouritemovie.entity.Movie;

import static goronald.web.id.myfavouritemovie.db.DatabaseContract.CONTENT_URI;
import static goronald.web.id.myfavouritemovie.db.DatabaseContract.FavouriteMovieColumns.BACKDROP;
import static goronald.web.id.myfavouritemovie.db.DatabaseContract.FavouriteMovieColumns.MOVIE_ID;
import static goronald.web.id.myfavouritemovie.db.DatabaseContract.FavouriteMovieColumns.OVERVIEW;
import static goronald.web.id.myfavouritemovie.db.DatabaseContract.FavouriteMovieColumns.POSTER;
import static goronald.web.id.myfavouritemovie.db.DatabaseContract.FavouriteMovieColumns.RELEASE_DATE;
import static goronald.web.id.myfavouritemovie.db.DatabaseContract.FavouriteMovieColumns.TITLE;
import static goronald.web.id.myfavouritemovie.db.DatabaseContract.FavouriteMovieColumns.VOTE_AVG;
import static goronald.web.id.myfavouritemovie.db.DatabaseContract.getColumnString;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    public static String EXTRA_DETAIL_MOVIE = "extra_movie";
    private TextView tvTitle, tvReleaseDate, tvOverview, tvRating;
    private RatingBar rbMovieRating;
    private ImageButton btnFavourite;
    private ImageView ivMoviePoster;
    private Movie movie;
    private StateListDrawable stateListDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movie = getIntent().getParcelableExtra(EXTRA_DETAIL_MOVIE);
        tvTitle = findViewById(R.id.tv_movie_title);
        tvReleaseDate = findViewById(R.id.tv_movie_release_date);
        tvOverview = findViewById(R.id.tv_movie_overview);
        tvRating = findViewById(R.id.tv_movie_rate);
        btnFavourite = findViewById(R.id.btn_favourite);
        rbMovieRating = findViewById(R.id.rb_movie_rating);
        ivMoviePoster = findViewById(R.id.iv_movie_poster);

        tvTitle.setText(movie.getMovieName());
        tvReleaseDate.setText(movie.getMovieReleaseDate());
        tvOverview.setText(movie.getMovieOverview());
        tvRating.setText(String.valueOf(movie.getMovieVoteAverage()));
        Glide.with(this)
                .load(movie.getMoviePoster())
                .into(ivMoviePoster);
        btnFavourite.setOnClickListener(this);
        rbMovieRating.setStepSize(0.1f);
        rbMovieRating.setIsIndicator(true);
        rbMovieRating.setRating((float) movie.getMovieVoteAverage() / 2.0f);

        stateListDrawable = (StateListDrawable)
                getResources().getDrawable(R.drawable.btn_favourite_star);
        int[] state = {isFavourited() ? android.R.attr.state_checked : android.R.attr.state_empty};
        stateListDrawable.setState(state);
        btnFavourite.setImageDrawable(stateListDrawable.getCurrent());
        btnFavourite.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        boolean favourited = isFavourited();
        if (favourited) {
            removeFromFavourite();
            favourited = false;
        }
        else {
            addToFavourite(movie);
            favourited = true;
        }
        int[] state = {favourited ? android.R.attr.state_checked : android.R.attr.state_empty};
        stateListDrawable.setState(state);
        btnFavourite.setImageDrawable(stateListDrawable.getCurrent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_share:
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setType("text/plain");
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        "[Share] My Favourite Movie - MyFavouriteMovie App");
                emailIntent.putExtra(Intent.EXTRA_TEXT,
                        "My Favourite Movie: \nTitle: " +
                                movie.getMovieName() + "\nRelease Date: " +
                                movie.getMovieReleaseDate() + "\n\nhttps://www.themoviedb.org");
                startActivity(emailIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
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
        getContentResolver().insert(CONTENT_URI, values);
        Toast.makeText(this, R.string.add_to_favourite, Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavourite() {
        Uri uri = null;
        if (movie.getMovieId() != 0) {
            uri = Uri.parse(CONTENT_URI + "/" + movie.getMovieId());
            getContentResolver().delete(uri, null, null);
        }
        Toast.makeText(this, R.string.remove_from_favourite, Toast.LENGTH_SHORT).show();
    }

    private boolean isFavourited() {
        boolean favourite;
        Uri uri = Uri.parse(CONTENT_URI + "/" + movie.getMovieId());
        Cursor cursor = getContentResolver().query(uri,
                null,
                null,
                null,
                null);
        if (cursor.getCount() > 0) {
            favourite = true;
        } else {
            favourite = false;
        }
        cursor.close();
        return favourite;
    }
}
