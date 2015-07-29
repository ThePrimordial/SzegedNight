package fagagy.szeged.hu.szegednight.pages;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import fagagy.szeged.hu.szegednight.pubResources.PubFragmentList;

public class FragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;

    public FragmentAdapter(FragmentManager fm, Fragment specificFragmentRow, String type) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(specificFragmentRow);
        fragments.add(new FragmentMap().newInstance(type));

    }

    //For party
    public FragmentAdapter(FragmentManager fm, Fragment specificFragmentRow) {
        super(fm);
        fragments = new ArrayList<>();
        fragments.add(specificFragmentRow);
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
            case 0: return PubFragmentList.TAG;
            case 1: return FragmentMap.TAG;
            default: return "unknown";
        }
    }
}
