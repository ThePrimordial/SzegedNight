package fagagy.szeged.hu.szegednight.atmResources;

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
import java.util.List;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pages.LocationObserver;

/**
 * Created by TheSorrow on 15/07/23.
 */
public class AtmFragmentList extends ListFragment implements AdapterView.OnItemClickListener {

    public static String TAG = "Listanézet";
    private ArrayList<Atm> atmList = new ArrayList<>();
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

        AtmAdapter atmAdapter = new AtmAdapter(getActivity(), atmList);
        setListAdapter(atmAdapter);
        registerForContextMenu(getListView());
        getListView().setOnItemClickListener(this);

    }

    private void generateRows() {
        List<ParseObject> serverList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ATM");
        try {
            serverList = query.fromPin("ATM").find();
        } catch (ParseException e1) {
        }

        if (myLoc == null) {
            for (int i = 0; i < serverList.size(); i++) {
                String name = serverList.get(i).getString("Name");
                double distance = 0.00;
                String type = serverList.get(i).getString("Type");
                Atm a1 = new Atm(name, type, distance);
                atmList.add(a1);
            }
        } else {
            for (int i = 0; i < serverList.size(); i++) {
                String name = serverList.get(i).getString("Name");
                Location targetLocation = new Location("");
                double longitude = serverList.get(i).getDouble("Longitude");
                double latitude = serverList.get(i).getDouble("Latitude");
                targetLocation.setLongitude(longitude);
                targetLocation.setLatitude(latitude);
                double distance = myLoc.distanceTo(targetLocation) / 1000;
                String type = serverList.get(i).getString("Type");
                Atm a1 = new Atm(name, type, distance);
                a1.setLatitude(latitude);
                a1.setLongitude(longitude);
                atmList.add(a1);
            }
        }

        Collections.sort(atmList, new Comparator<Atm>() {
            @Override
            public int compare(Atm a1, Atm a2) {
                return Double.compare(a1.getDistance(), a2.getDistance());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (myLoc == null) {
            Toast.makeText(getActivity(), R.string.NoAvaibleLoc, Toast.LENGTH_LONG).show();
        } else if (myLoc != null) {
            String uri = "http://maps.google.com/maps?saddr=" + myLoc.getLatitude() + "," + myLoc.getLongitude() +
                    "&daddr=" + atmList.get(position).getLatitude() + "," + atmList.get(position).getLongitude();
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(i);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
