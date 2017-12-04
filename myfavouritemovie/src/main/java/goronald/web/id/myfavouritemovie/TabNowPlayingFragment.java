package goronald.web.id.myfavouritemovie;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import goronald.web.id.myfavouritemovie.adapter.NowPlayingAdapter;
import goronald.web.id.myfavouritemovie.entity.Movie;
import goronald.web.id.myfavouritemovie.utils.RecyclerViewScrollListener;

public class TabNowPlayingFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    public static final String EXTRAS_MOVIE_OLD_DATA = "extras_movie_old_data";
    public static final String EXTRAS_PAGE = "extras_page";

    private RecyclerView mRecyclerView;
    private NowPlayingAdapter mAdapter;
    private ProgressBar progressBar;
    private Bundle mBundle;
    private ArrayList<Movie> oldData;
    private boolean endOfPage;
    private int page;

    public TabNowPlayingFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab_now_playing, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progress_bar);
        mRecyclerView = view.findViewById(R.id.rv_now_playing);

        mBundle = new Bundle();
        oldData = new ArrayList<>();
        page = 1;
        endOfPage = false;

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        mAdapter = new NowPlayingAdapter(getContext());
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
                if(endOfPage) {
                    stopInfiniteScrolling();
                }
                loadMoreData();
            }
        });
        showProgressBar();
        mBundle.putInt(EXTRAS_PAGE, page);
        getLoaderManager().initLoader(0, mBundle, this);
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        return new NowPlayingAsyncTaskLoader(getContext(), args);
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
}
