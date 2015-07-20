package fagagy.szeged.hu.szegednight.pages;

import android.app.Activity;
import android.app.ListActivity;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pubList.Pub;
import fagagy.szeged.hu.szegednight.pubList.PubAdapter;

public class BrowsingWithRow extends ListActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Pub> pubList = new ArrayList<Pub>();
        pubList.add(new Pub("Cool Club",true, 3.2));
        pubList.add(new Pub("Pivo",false, 0.4));
        pubList.add(new Pub("Var", true, 7.2));

        Collections.sort(pubList, new Comparator<Pub>() {
            @Override
            public int compare(Pub p1, Pub p2) {
                return Double.compare(p1.getDistance(), p2.getDistance());
            }
        });

        PubAdapter pubAdapter = new PubAdapter(getApplicationContext(), pubList);
        setListAdapter(pubAdapter);
        registerForContextMenu(getListView());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_browser_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
