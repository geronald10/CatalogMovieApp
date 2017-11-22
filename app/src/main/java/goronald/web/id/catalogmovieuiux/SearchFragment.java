package goronald.web.id.catalogmovieuiux;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.SearchView;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ArrayList<Movie>>{

    public static final String TAG = SearchFragment.class.getSimpleName();
    public static final String EXTRAS_TITLE = "extras_title";
    private RecyclerView mRecyclerView;
    private SearchAdapter mAdapter;

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit " + query);
                if (!TextUtils.isEmpty(query)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(EXTRAS_TITLE, query);
                    getLoaderManager().restartLoader(0, bundle, SearchFragment.this);
                    searchView.clearFocus();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new SearchAdapter(getContext());
        mAdapter.notifyDataSetChanged();
        mRecyclerView = view.findViewById(R.id.rv_search);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),
                2, GridLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                DetailMovieFragment mDetailMovieFragment = new DetailMovieFragment();
                Bundle mBundle = new Bundle();
                mBundle.putParcelable(DetailMovieFragment.EXTRA_MOVIE, mAdapter.getData().get(position));
                mDetailMovieFragment.setArguments(mBundle);
                FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.content_main, mDetailMovieFragment,
                        DetailMovieFragment.class.getSimpleName());
                mFragmentTransaction.addToBackStack(null);
                mFragmentTransaction.commit();
            }
        });
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        String title = "";
        if (args != null) {
            title = args.getString(EXTRAS_TITLE);
        } else {
            title = null;
        }
        return new SearchAsyncTaskLoader(getContext(), title);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        mAdapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        mAdapter.setData(null);
    }
}
