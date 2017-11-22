package goronald.web.id.catalogmovieuiux;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchAsyncTaskLoader extends AsyncTaskLoader<ArrayList<Movie>> {

    private static final String TAG = SearchAsyncTaskLoader.class.getSimpleName();

    private ArrayList<Movie> mData;
    private boolean mHasResult = false;
    private String title;

    public SearchAsyncTaskLoader(Context context, String title) {
        super(context);
        onContentChanged();
        this.title = title;
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
        final ArrayList<Movie> movieItemList = new ArrayList<>();
        String url;
        Log.d(TAG, "title: " + title);
        if (title != null)
            url = BuildConfig.SEARCH_URL + "?api_key=" + BuildConfig.API_KEY +
                    "&language=en-US&query=" + title;
        else
            url = BuildConfig.DISCOVER_URL + "?api_key=" + BuildConfig.API_KEY +
                    "&language=en-US;";

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
                    for (int i = 0; i < resultList.length(); i++) {
                        JSONObject movie = resultList.getJSONObject(i);
                        Movie movieItems = new Movie(movie);
                        movieItemList.add(movieItems);
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
