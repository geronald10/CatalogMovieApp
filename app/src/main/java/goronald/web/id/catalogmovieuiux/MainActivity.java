package goronald.web.id.catalogmovieuiux;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String TAG = MainActivity.class.getSimpleName();

    CircleImageView profileCircleImageView;
    String profileImageUrl = "https://avatars0.githubusercontent.com/u/12123500?s=400&v=4";

    DrawerLayout drawer;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getResources().getString(R.string.now_playing));

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        profileCircleImageView = navigationView.getHeaderView(0)
                .findViewById(R.id.iv_profile_pict);

        Glide.with(MainActivity.this)
                .load(profileImageUrl)
                .into(profileCircleImageView);

        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            Fragment currentFragment = new NowPlayingFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, currentFragment)
                    .commit();
            navigationView.setCheckedItem(R.id.nav_now_playing);
        }
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        AppBarLayout appBarLayout = findViewById(R.id.fragment_app_bar_layout);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (appBarLayout.getVisibility() == View.GONE) {
            appBarLayout.setVisibility(View.VISIBLE);
            onResume();
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected");
        int id = item.getItemId();
        Fragment fragment = null;
        onResume();
        if (id == R.id.nav_now_playing) {
            title = getResources().getString(R.string.now_playing);
            fragment = new NowPlayingFragment();
        } else if (id == R.id.nav_upcoming) {
            title = getResources().getString(R.string.upcoming);
            fragment = new UpcomingFragment();
        } else if (id == R.id.nav_search) {
            title = getResources().getString(R.string.search_movies);
            fragment = new SearchFragment();
        } else if (id == R.id.nav_favourite) {
            title = getString(R.string.favourite_movies);
            fragment = new FavouriteMovieFragment();
        } else if (id == R.id.nav_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        Log.d(TAG, title);
        toolbar.setTitle(title);

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, fragment)
                    .commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
