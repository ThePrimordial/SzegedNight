package fagagy.szeged.hu.szegednight.atmRescources;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pages.FragmentAdapter;

/**
 * Created by TheSorrow on 15/07/23.
 */
public class AtmBrowser extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing_with_swipe);

        Fragment atmFragmentRow = new AtmFragmentList();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), atmFragmentRow, "ATM");
        ViewPager p = (ViewPager) findViewById(R.id.pager);
        p.setAdapter(adapter);
    }

}
