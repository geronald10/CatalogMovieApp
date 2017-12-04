package goronald.web.id.myfavouritemovie;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import goronald.web.id.myfavouritemovie.entity.Movie;

public class SearchAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Movie>> {

    private static final String TAG = SearchAsyncTaskLoader.class.getSimpleName();

    private ArrayList<Movie> mData;
    private boolean mHasResult = false;
    private String title;
    private int mPage;

    public SearchAsyncTaskLoader(Context context, Bundle args) {
        super(context);
        onContentChanged();
        this.title = args.getString(SearchFragment.EXTRAS_TITLE);
        this.mPage = args.getInt(SearchFragment.EXTRAS_PAGE);
        this.mData = args.getParcelableArrayList(SearchFragment.EXTRAS_MOVIE_OLD_DATA);
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        else if (mHasResult)
            deliverResult(mData);
    }

    @Override
    public void deliverResult(ArrayList<Movie> data) {
        mData = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult) {
            onReleaseResources(mData);
            mData = null;
            mHasResult = false;
        }
    }

    @Override
    public ArrayList<Movie> loadInBackground() {
        String url;
        Log.d(TAG, "title: " + title);
        Log.d(TAG, "page: " + mPage);

        if (title != null)
            url = BuildConfig.SEARCH_URL + "?api_key=" + BuildConfig.API_KEY +
                    "&language=en-US&query=" + title + "&page=" + mPage;
        else {
            url = BuildConfig.DISCOVER_URL + "?api_key=" + BuildConfig.API_KEY +
                    "&language=en-US&page=" + mPage;
        }

        final ArrayList<Movie> movieItemList;
        if (mData == null) {
            movieItemList = new ArrayList<>();
        } else {
            movieItemList = mData;
        }

        SyncHttpClient client = new SyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray resultList = responseObject.getJSONArray("results");
                    if (resultList.length() > 0) {
                        for (int i = 0; i < resultList.length(); i++) {
                            JSONObject movie = resultList.getJSONObject(i);
                            Movie movieItems = new Movie(movie);
                            movieItemList.add(movieItems);
                        }
                    } else {
                        movieItemList.add(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
        return movieItemList;
    }

    private void onReleaseResources(ArrayList<Movie> data) {
    }
}
