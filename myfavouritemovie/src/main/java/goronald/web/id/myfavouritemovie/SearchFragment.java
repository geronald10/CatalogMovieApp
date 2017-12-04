package goronald.web.id.myfavouritemovie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import goronald.web.id.myfavouritemovie.adapter.SearchAdapter;
import goronald.web.id.myfavouritemovie.entity.Movie;
import goronald.web.id.myfavouritemovie.utils.RecyclerViewScrollListener;

public class SearchFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    public static final String TAG = SearchFragment.class.getSimpleName();

    public static final String EXTRAS_TITLE = "extras_title";
    public static final String EXTRAS_MOVIE_OLD_DATA = "extras_movie_old_data";
    public static final String EXTRAS_PAGE = "extras_page";

    private RecyclerView mRecyclerView;
//    private RecyclerViewScrollListener scrollListener;
    private SearchAdapter mAdapter;
    private ProgressBar progressBar;
    private Bundle mBundle;
    private ArrayList<Movie> oldData;
    private int page;
    private boolean newSearchFlag = false;
    private boolean endOfPage = false;

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit " + query);
                if (!TextUtils.isEmpty(query)) {
                    newSearchFlag = true;
                    onResume();
//                    endOfPage = false;
                    page = 1;
                    oldData = null;
                    mBundle.putString(EXTRAS_TITLE, query);
                    mBundle.putInt(EXTRAS_PAGE, page);
                    mBundle.putParcelableArrayList(EXTRAS_MOVIE_OLD_DATA, oldData);
                    getLoaderManager().restartLoader(0, mBundle, SearchFragment.this);
                    searchView.clearFocus();
                    showProgressBar();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            page = savedInstanceState.getInt(EXTRAS_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress_bar);
        mRecyclerView = view.findViewById(R.id.rv_search);

        mBundle = new Bundle();
        oldData = new ArrayList<>();
        page = 1;

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mAdapter = new SearchAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerViewScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onLoadMore() {
                Log.d("SearchFragment", "onLoadMore called");
                if (newSearchFlag || endOfPage) {
                    onDataCleared();
                    newSearchFlag = false;
                    endOfPage = false;
                } else {
                    loadMoreData();
                }
            }
        });
        showProgressBar();
        mBundle.putInt(EXTRAS_PAGE, page);
        getLoaderManager().initLoader(0, mBundle, this);
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        Log.d("onCreateLoader", "pages: " + args.getInt(EXTRAS_PAGE));
        return new SearchAsyncTaskLoader(getContext(), args);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        oldData = data;
        if (oldData.get(oldData.size() - 1) == null) {
            oldData.remove(oldData.size() - 1);
            endOfPage = true;
            Toast.makeText(getContext(), "End of search", Toast.LENGTH_SHORT).show();
        }
        mAdapter.setItems(oldData);
        mAdapter.showLoading(false);
        hideProgressBar();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        mAdapter.setItems(null);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void loadMoreData() {
        mAdapter.showLoading(true);
        page = page + 1;
        mBundle.putInt(EXTRAS_PAGE, page);
        mBundle.putParcelableArrayList(EXTRAS_MOVIE_OLD_DATA, oldData);
        getLoaderManager().restartLoader(0, mBundle, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRAS_PAGE, page);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
