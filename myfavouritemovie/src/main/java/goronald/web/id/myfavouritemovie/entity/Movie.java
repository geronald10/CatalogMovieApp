package goronald.web.id.myfavouritemovie.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import goronald.web.id.myfavouritemovie.db.DatabaseContract;

import static goronald.web.id.myfavouritemovie.db.DatabaseContract.getColumnDouble;
import static goronald.web.id.myfavouritemovie.db.DatabaseContract.getColumnInt;
import static goronald.web.id.myfavouritemovie.db.DatabaseContract.getColumnString;

public class Movie implements Parcelable {

    private int id;
    private int movieId;
    private String movieName;
    private String movieOverview;
    private String movieReleaseDate;
    private String moviePoster;
    private String movieBackdrop;
    private double movieVoteAverage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMovieBackdrop() {
        return movieBackdrop;
    }

    public void setMovieBackdrop(String movieBackdrop) {
        this.movieBackdrop = movieBackdrop;
    }

    public double getMovieVoteAverage() {
        return movieVoteAverage;
    }

    public void setMovieVoteAverage(double movieVoteAverage) {
        this.movieVoteAverage = movieVoteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.movieId);
        dest.writeString(this.movieName);
        dest.writeString(this.movieOverview);
        dest.writeString(this.movieReleaseDate);
        dest.writeString(this.moviePoster);
        dest.writeString(this.movieBackdrop);
        dest.writeDouble(this.movieVoteAverage);
    }

    public Movie() {
    }

    public Movie(Cursor cursor) {
        this.id = getColumnInt(cursor, DatabaseContract.FavouriteMovieColumns._ID);
        this.movieId = getColumnInt(cursor, DatabaseContract.FavouriteMovieColumns.MOVIE_ID);
        this.movieName = getColumnString(cursor, DatabaseContract.FavouriteMovieColumns.TITLE);
        this.movieOverview = getColumnString(cursor, DatabaseContract.FavouriteMovieColumns.OVERVIEW);
        this.movieReleaseDate = getColumnString(cursor, DatabaseContract.FavouriteMovieColumns.RELEASE_DATE);
        this.moviePoster = getColumnString(cursor, DatabaseContract.FavouriteMovieColumns.POSTER);
        this.movieBackdrop = getColumnString(cursor, DatabaseContract.FavouriteMovieColumns.BACKDROP);
        this.movieVoteAverage = getColumnDouble(cursor, DatabaseContract.FavouriteMovieColumns.VOTE_AVG);
    }

    public Movie(JSONObject object) {
        try {
            int id = object.getInt("id");
            String title = object.getString("title");
            String overview = object.getString("overview");
            String releaseDate = object.getString("release_date");
            String posterPath = object.getString("poster_path");
            String backDropPath = object.getString("backdrop_path");
            double movieVote = object.getDouble("vote_average");

            String newDateformatted = "";
            String fullPosterPath = "";
            String fullBackdropPath = "";
            if (!releaseDate.equals("")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date currentDate = dateFormat.parse(releaseDate);
                SimpleDateFormat newDateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy");
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                newDateformatted = newDateFormat.format(currentDate);
            }

            if (!posterPath.equals("null")) {
                fullPosterPath = "http://image.tmdb.org/t/p/w342" + posterPath;
            }

            if (!backDropPath.equals("null")) {
                fullBackdropPath = "http://image.tmdb.org/t/p/w780" + backDropPath;
            }

            movieId = id;
            movieName = title;
            movieOverview = overview;
            movieReleaseDate = newDateformatted;
            moviePoster = fullPosterPath;
            movieBackdrop = fullBackdropPath;
            movieVoteAverage = movieVote;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.movieId = in.readInt();
        this.movieName = in.readString();
        this.movieOverview = in.readString();
        this.movieReleaseDate = in.readString();
        this.moviePoster = in.readString();
        this.movieBackdrop = in.readString();
        this.movieVoteAverage = in.readDouble();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
