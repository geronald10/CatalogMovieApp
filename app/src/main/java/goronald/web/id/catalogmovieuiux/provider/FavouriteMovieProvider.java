package goronald.web.id.catalogmovieuiux.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import goronald.web.id.catalogmovieuiux.db.DatabaseContract;
import goronald.web.id.catalogmovieuiux.db.FavouriteMovieHelper;

import static goronald.web.id.catalogmovieuiux.db.DatabaseContract.AUTHORITY;
import static goronald.web.id.catalogmovieuiux.db.DatabaseContract.CONTENT_URI;

public class FavouriteMovieProvider extends ContentProvider {

    private static final int FAVOURITE = 1;
    private static final int FAVOURITE_ID = 2;
    private static final int MOVIE_ID = 3;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, DatabaseContract.TABLE_FAVOURITE_MOVIE, FAVOURITE);
        sUriMatcher.addURI(AUTHORITY, DatabaseContract.TABLE_FAVOURITE_MOVIE + "/#", FAVOURITE_ID);
        sUriMatcher.addURI(AUTHORITY, DatabaseContract.TABLE_FAVOURITE_MOVIE + "/#", MOVIE_ID);
    }

    private FavouriteMovieHelper favouriteMovieHelper;

    @Override
    public boolean onCreate() {
        favouriteMovieHelper = new FavouriteMovieHelper(getContext());
        favouriteMovieHelper.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case FAVOURITE:
                cursor = favouriteMovieHelper.queryProvider();
                break;
            case FAVOURITE_ID:
                cursor = favouriteMovieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            case MOVIE_ID:
                cursor = favouriteMovieHelper.queryByMovieIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long added;
        switch (sUriMatcher.match(uri)) {
            case FAVOURITE:
                added = favouriteMovieHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updated;
        switch (sUriMatcher.match(uri)) {
            case FAVOURITE_ID:
                updated = favouriteMovieHelper.updateProvider(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }

        if (updated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case FAVOURITE_ID:
                deleted = favouriteMovieHelper.deleteProvider(uri.getLastPathSegment());
                break;
            case MOVIE_ID:
                deleted = favouriteMovieHelper.deleteByMovieIdProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (deleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }
}
