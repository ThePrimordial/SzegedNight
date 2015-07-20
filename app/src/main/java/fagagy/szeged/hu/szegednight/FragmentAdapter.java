package fagagy.szeged.hu.szegednight;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import fagagy.szeged.hu.szegednight.pages.FragmentMap;
import fagagy.szeged.hu.szegednight.pages.FragmentRow;

/**
 * Created by Ádám on 15/07/20.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    public static final int NUM_ITEMS = 2;
    private ArrayList<Fragment> fragments;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<Fragment>();
        fragments.add(new FragmentRow());
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
}
