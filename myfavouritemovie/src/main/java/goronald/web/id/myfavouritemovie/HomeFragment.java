package goronald.web.id.myfavouritemovie;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import goronald.web.id.myfavouritemovie.adapter.HomePagerAdapter;

public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private HomePagerAdapter mHomePagerAdapter;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tabLayout = view.findViewById(R.id.tabs);
        mViewPager = view.findViewById(R.id.container);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.now_playing)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.upcoming)));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        mViewPager.setAdapter(mHomePagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                getChildFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
        mViewPager.setAdapter(mHomePagerAdapter);
    }
}
