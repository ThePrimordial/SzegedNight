package fagagy.szeged.hu.szegednight.partyResources;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import fagagy.szeged.hu.szegednight.R;

/**
 * Created by TheSorrow on 15/07/28.
 */
public class PartyBrowser extends FragmentActivity {


    public static FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing_with_swipe);
        fragmentManager = getSupportFragmentManager();
        Fragment partyFragmentList = new PartyFragmentList();
        Fragment szinfragment = new SZINFragmentList();
        PartyFragmentAdapter adapter = new PartyFragmentAdapter(fragmentManager,partyFragmentList,szinfragment);
        ViewPager p = (ViewPager) findViewById(R.id.pager);
        p.setAdapter(adapter);
    }
}
