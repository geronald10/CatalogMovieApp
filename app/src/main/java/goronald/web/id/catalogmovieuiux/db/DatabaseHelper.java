package goronald.web.id.catalogmovieuiux.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbmovieuiuxapp";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_FAVOURITE_MOVIE =
            String.format("CREATE TABLE %s"
                            + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " %s INTEGER NOT NULL," +
                            " %s TEXT NOT NULL," +
                            " %s TEXT," +
                            " %s TEXT," +
                            " %s TEXT," +
                            " %s TEXT," +
                            " %s REAL)",
                    DatabaseContract.TABLE_FAVOURITE_MOVIE,
                    DatabaseContract.FavouriteMovieColumns._ID,
                    DatabaseContract.FavouriteMovieColumns.MOVIE_ID,
                    DatabaseContract.FavouriteMovieColumns.TITLE,
                    DatabaseContract.FavouriteMovieColumns.OVERVIEW,
                    DatabaseContract.FavouriteMovieColumns.RELEASE_DATE,
                    DatabaseContract.FavouriteMovieColumns.POSTER,
                    DatabaseContract.FavouriteMovieColumns.BACKDROP,
                    DatabaseContract.FavouriteMovieColumns.VOTE_AVG
            );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_FAVOURITE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_FAVOURITE_MOVIE);
        onCreate(sqLiteDatabase);
    }
}
