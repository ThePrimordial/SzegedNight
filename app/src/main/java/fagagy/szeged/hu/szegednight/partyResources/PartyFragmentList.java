package fagagy.szeged.hu.szegednight.partyResources;

import android.content.Intent;
import android.location.Location;
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
import fagagy.szeged.hu.szegednight.pages.LocationObserver;

/**
 * Created by TheSorrow on 15/07/28.
 */
public class PartyFragmentList extends ListFragment implements AdapterView.OnItemClickListener {

    public static String TAG = "Események";
    private ArrayList<Party> partyList = new ArrayList<>();
    private LocationObserver observer;
    private Location myLoc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TAG = getContext().getResources().getString(R.string.ListView);
        View v = View.inflate(getActivity(), R.layout.fragmentrow, null);
        observer = new LocationObserver(v.getContext(), 20000, 50, 30000);
        observer.start();
        myLoc = observer.getLastKnownLocation();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        generateRows();

        PartyAdapter partyAdapter = new PartyAdapter(getActivity(), partyList);
        setListAdapter(partyAdapter);
        registerForContextMenu(getListView());
        getListView().setOnItemClickListener(this);

    }

    private void generateRows() {
        List<ParseObject> serverList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Party").fromLocalDatastore();
        try {
            serverList = query.fromPin("Party").find();
        } catch (ParseException e1) {
        }

        if (myLoc == null) {
            for (int i = 0; i < serverList.size(); i++) {
                String place = serverList.get(i).getString("Place");
                String event = serverList.get(i).getString("EventName");
                Date date = serverList.get(i).getDate("Date");
                double distance = 0.00;
                Party p1 = new Party(place, event, distance, date);
                partyList.add(p1);
            }
        } else if (myLoc != null) {
            for (int i = 0; i < serverList.size(); i++) {
                String place = serverList.get(i).getString("Place");
                String event = serverList.get(i).getString("EventName");
                Date date = serverList.get(i).getDate("Date");
                Location targetLocation = new Location("");
                double longitude = serverList.get(i).getDouble("Longitude");
                double latitude = serverList.get(i).getDouble("Latitude");
                targetLocation.setLongitude(longitude);
                targetLocation.setLatitude(latitude);
                double distance = myLoc.distanceTo(targetLocation) / 1000;
                Party p1 = new Party(place, event, distance, date);
                p1.setLongitude(longitude);
                p1.setLatitude(latitude);
                partyList.add(p1);
            }
        }

        Collections.sort(partyList, new Comparator<Party>() {
            @Override
            public int compare(Party p1, Party p2) {
                return p1.getDate().compareTo(p2.getDate());

            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (myLoc == null) {
            Toast.makeText(getActivity(), R.string.NoAvaibleLoc, Toast.LENGTH_LONG).show();
        } else if (myLoc != null) {
            String uri = "http://maps.google.com/maps?saddr=" + myLoc.getLatitude() + "," + myLoc.getLongitude() +
                    "&daddr=" + partyList.get(position).getLatitude() + "," + partyList.get(position).getLongitude();
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(i);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
