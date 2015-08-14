package fagagy.szeged.hu.szegednight.pubResources;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pages.FragmentAdapter;
import fagagy.szeged.hu.szegednight.startingPageRescources.FragmentDrawer;

public class PubBrowser extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{

    public static FragmentManager fragmentManager;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing_with_swipe);

        fragmentManager = getSupportFragmentManager();
        Fragment pubFragmentRow = new PubFragmentList();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), pubFragmentRow, "Pub");
        ViewPager p = (ViewPager) findViewById(R.id.pager);
        p.setAdapter(adapter);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                fragmentManager.findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_starting_page, menu);
        return true;
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

    }
}