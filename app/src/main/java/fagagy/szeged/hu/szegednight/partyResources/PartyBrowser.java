package fagagy.szeged.hu.szegednight.partyResources;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;

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
        Fragment partyFragmentList = new PartyFragmentList();
        fragmentManager = getSupportFragmentManager();
        Fragment szinList = new SZINFragmentList();
        Fragment golyataborList = new GolyaTaborFragmentList();
        PartyFragmentAdapter adapter = new PartyFragmentAdapter(fragmentManager,partyFragmentList,szinList,golyataborList);
        ViewPager p = (ViewPager) findViewById(R.id.pager);
        p.setAdapter(adapter);
    }
}
