package fagagy.szeged.hu.szegednight.pubRescources;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pages.FragmentAdapter;

public class PubBrowser extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing_with_swipe);

        Fragment pubFragmentRow = new PubFragmentList();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), pubFragmentRow);
        ViewPager p = (ViewPager) findViewById(R.id.pager);
        p.setAdapter(adapter);
    }

}