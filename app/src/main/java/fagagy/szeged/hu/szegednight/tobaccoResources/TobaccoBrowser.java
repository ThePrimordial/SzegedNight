package fagagy.szeged.hu.szegednight.tobaccoResources;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pages.FragmentAdapter;

/**
 * Created by TheSorrow on 15/07/27.
 */
public class TobaccoBrowser extends FragmentActivity {

    public static FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing_with_swipe);

        fragmentManager = getSupportFragmentManager();
        Fragment tobaccoFragmentList = new TobaccoFragmentList();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), tobaccoFragmentList, "Tobacco");
        ViewPager p = (ViewPager) findViewById(R.id.pager);
        p.setAdapter(adapter);
    }

}
