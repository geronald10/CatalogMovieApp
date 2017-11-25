package goronald.web.id.catalogmovieuiux.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static android.provider.BaseColumns._ID;
import static goronald.web.id.catalogmovieuiux.db.DatabaseContract.FavouriteMovieColumns.MOVIE_ID;
import static goronald.web.id.catalogmovieuiux.db.DatabaseContract.TABLE_FAVOURITE_MOVIE;

public class FavouriteMovieHelper {
    public static String DATABASE_TABLE = TABLE_FAVOURITE_MOVIE;
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public FavouriteMovieHelper(Context context) {
        this.context = context;
    }

    public FavouriteMovieHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC");
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE,
                null,
                _ID + " = ?",
                new String[]{id},
                null,
                null,
                _ID + " DESC",
                null);
    }

    public Cursor queryByMovieIdProvider(String movieId) {
        return database.query(DATABASE_TABLE,
                null,
                MOVIE_ID + " = ?",
                new String[]{movieId},
                null,
                null,
                _ID + " DESC",
                null);
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }

    public int deleteByMovieIdProvider(String movieId) {
        return database.delete(DATABASE_TABLE, MOVIE_ID + " = ?", new String[]{movieId});
    }
}
