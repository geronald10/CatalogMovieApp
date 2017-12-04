package goronald.web.id.myfavouritemovie.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import goronald.web.id.myfavouritemovie.TabNowPlayingFragment;
import goronald.web.id.myfavouritemovie.TabUpComingFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    int numOfTabs;

    public HomePagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new TabNowPlayingFragment();
            case 1:
                return new TabUpComingFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
