package goronald.web.id.myfavouritemovie;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import goronald.web.id.myfavouritemovie.entity.Movie;

public class NowPlayingAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Movie>> {

    private ArrayList<Movie> mData;
    private boolean mHasResult = false;
    private int mPage;

    public NowPlayingAsyncTaskLoader(Context context, Bundle args) {
        super(context);
        onContentChanged();
        this.mData = args.getParcelableArrayList(TabUpComingFragment.EXTRAS_MOVIE_OLD_DATA);
        this.mPage = args.getInt(TabUpComingFragment.EXTRAS_PAGE);
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
        SyncHttpClient client = new SyncHttpClient();
        String url = BuildConfig.BASE_URL + "now_playing?api_key=" +
                BuildConfig.API_KEY + "&language=en-US&page=" + mPage;

        final ArrayList<Movie> movieList;
        if (mData == null) {
            movieList = new ArrayList<>();
        } else {
            movieList = mData;
        }

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
                    for (int i=0; i< resultList.length(); i++) {
                        JSONObject movie = resultList.getJSONObject(i);
                        Movie movieItems = new Movie(movie);
                        movieList.add(movieItems);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        return movieList;
    }

    private void onReleaseResources(ArrayList<Movie> data) {
    }
}
