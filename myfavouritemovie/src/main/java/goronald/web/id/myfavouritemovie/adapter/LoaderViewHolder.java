package goronald.web.id.myfavouritemovie.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import goronald.web.id.myfavouritemovie.R;

public class LoaderViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar mProgressBar;

    public LoaderViewHolder(View itemView) {
        super(itemView);
        mProgressBar = itemView.findViewById(R.id.progress_bar);
    }
}