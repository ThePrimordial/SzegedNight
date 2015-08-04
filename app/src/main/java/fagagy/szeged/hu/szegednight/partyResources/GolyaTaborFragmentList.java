package fagagy.szeged.hu.szegednight.partyResources;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pages.MyCurrentLocationListener;

/**
 * Created by TheSorrow on 15/08/04.
 */
public class GolyaTaborFragmentList extends ListFragment {


    public static final String TAG = "Összegyetemi";
    private ArrayList<Party> partyList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = View.inflate(getActivity(), R.layout.partyfragmentrow, null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        generateRows();

        PartyAdapter partyAdapter = new PartyAdapter(getActivity(), partyList);
        setListAdapter(partyAdapter);
        registerForContextMenu(getListView());
    }

    private void generateRows() {
        List<ParseObject> serverList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Golyatabor").fromLocalDatastore();
        try {
            serverList = query.fromPin("Golyatabor").find();
        } catch (ParseException e1) {
        }
            for (int i = 0; i < serverList.size(); i++) {
                String place = serverList.get(i).getString("band");
                String event = serverList.get(i).getString("stage");
                Date date = serverList.get(i).getDate("Date");
                Party p1 = new Party(place, event, date);
                partyList.add(p1);
            }

        Collections.sort(partyList, new Comparator<Party>() {
            @Override
            public int compare(Party p1, Party p2) {
                return p1.getDate().compareTo(p2.getDate());
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}

