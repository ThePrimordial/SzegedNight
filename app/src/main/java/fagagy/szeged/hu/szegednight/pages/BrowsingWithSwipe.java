package fagagy.szeged.hu.szegednight.pages;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import fagagy.szeged.hu.szegednight.FragmentAdapter;
import fagagy.szeged.hu.szegednight.R;
/**
 * Created by Ádám on 15/07/20.
 */
public class BrowsingWithSwipe extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_browser_page);

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        ViewPager p = (ViewPager) findViewById(R.id.pager);
        p.setAdapter(adapter);
        p.setCurrentItem(FragmentAdapter.NUM_ITEMS);
    }
}