package fagagy.szeged.hu.szegednight.pages;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import fagagy.szeged.hu.szegednight.pubRescources.PubFragmentList;

public class FragmentAdapter extends FragmentPagerAdapter {

    public static final int NUM_ITEMS = 2;
    private ArrayList<Fragment> fragments;

    public FragmentAdapter(FragmentManager fm, Fragment specificFragmentRow) {
        super(fm);
        fragments = new ArrayList<Fragment>();
        fragments.add(specificFragmentRow);
        fragments.add(new FragmentMap());

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return PubFragmentList.TAG;
            case 1: return FragmentMap.TAG;
            default: return "unknown";
        }
    }
}
