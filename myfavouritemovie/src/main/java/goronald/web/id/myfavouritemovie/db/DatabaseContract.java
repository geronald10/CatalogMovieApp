package goronald.web.id.myfavouritemovie.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static String TABLE_FAVOURITE_MOVIE = "favourite_movie";

    public static final class FavouriteMovieColumns implements BaseColumns {
        public static String MOVIE_ID = "id";
        public static String TITLE = "title";
        public static String OVERVIEW = "overview";
        public static String RELEASE_DATE = "release_date";
        public static String POSTER = "poster_path";
        public static String BACKDROP = "backdrop_path";
        public static String VOTE_AVG = "vote_average";
    }

    public static final String AUTHORITY = "goronald.web.id.catalogmovieuiux";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_FAVOURITE_MOVIE)
            .build();

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static double getColumnDouble(Cursor cursor, String columnName) {
        return cursor.getDouble(cursor.getColumnIndex(columnName));
    }
}
