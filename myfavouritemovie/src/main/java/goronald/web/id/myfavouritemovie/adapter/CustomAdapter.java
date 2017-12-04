package goronald.web.id.myfavouritemovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import goronald.web.id.myfavouritemovie.R;

public abstract class CustomAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected boolean showLoader;
    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_LOADER = 2;

    protected List<T> mItems;
    protected LayoutInflater mInflater;

    public CustomAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADER) {
            View view = mInflater.inflate(R.layout.progress_item, parent, false);
            return new LoaderViewHolder(view);
        } else if (viewType == VIEW_TYPE_ITEM) {
            return getYourItemViewHolder(parent);
        }
        throw new IllegalArgumentException("Invalid ViewType: " + viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof LoaderViewHolder) {
            LoaderViewHolder loaderViewHolder = (LoaderViewHolder) viewHolder;
            if (showLoader) {
                loaderViewHolder.mProgressBar.setVisibility(View.VISIBLE);
            } else {
                loaderViewHolder.mProgressBar.setVisibility(View.GONE);
            }
            return;
        }
        bindYourViewHolder(viewHolder, position);
    }

    @Override
    public int getItemCount() {
        if (mItems == null || mItems.size() == 0) {
            return 0;
        }
        return mItems.size() + 1;
    }

    @Override
    public long getItemId(int position) {
        if (position != 0 && position == getItemCount() - 1) {
            return -1;
        }
        return getYourItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position != 0 && position == getItemCount() - 1) {
            return VIEW_TYPE_LOADER;
        }
        return VIEW_TYPE_ITEM;
    }

    public void showLoading(boolean status) {
        showLoader = status;
    }

    public void setItems(List<T> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    public List<T> getItems() {
        return mItems;
    }

    public abstract long getYourItemId(int position);
    public abstract RecyclerView.ViewHolder getYourItemViewHolder(ViewGroup parent);
    public abstract void bindYourViewHolder(RecyclerView.ViewHolder holder, int position);
}
