package fagagy.szeged.hu.szegednight.partyResources;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by TheSorrow on 15/08/04.
 */
public class PartyFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;

    public PartyFragmentAdapter(FragmentManager fm, Fragment... specificFragmentRow) {
        super(fm);
        fragments = new ArrayList<>();
        Collections.addAll(fragments, specificFragmentRow);
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return PartyFragmentList.TAG;
            case 1: return SZINFragmentList.TAG;
            case 2: return GolyaTaborFragmentList.TAG;
            default: return "unknown";
        }
    }
}

