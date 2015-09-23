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
 * Created by TheSorrow on 15/07/28.
 */
public class PartyFragmentList extends ListFragment implements AdapterView.OnItemClickListener  {

    public static final String TAG = "Események";
    private ArrayList<Party> partyList = new ArrayList<>();
    private LocationManager lm;
    private MyCurrentLocationListener locListener;
    private Location gpsLoc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = View.inflate(getActivity(), R.layout.partyfragmentrow, null);
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locListener = new MyCurrentLocationListener();
        lm.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 20000, 50,
                locListener);
        gpsLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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

        if (gpsLoc == null) {
            Toast.makeText(getActivity(), "Nem érhető el a jelenlegi pozíció!", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < serverList.size(); i++) {
                String place = serverList.get(i).getString("Place");
                String event = serverList.get(i).getString("EventName");
                Date date = serverList.get(i).getDate("Date");
                double distance  = 0.00;
                Party p1 = new Party(place, event, distance, date);
                partyList.add(p1);
            }
        } else if(gpsLoc != null) {
            for (int i = 0; i < serverList.size(); i++) {
                String place = serverList.get(i).getString("Place");
                String event = serverList.get(i).getString("EventName");
                Date date = serverList.get(i).getDate("Date");
                Location targetLocation = new Location("");
                double longitude = serverList.get(i).getDouble("Longitude");
                double latitude = serverList.get(i).getDouble("Latitude");
                targetLocation.setLongitude(longitude);
                targetLocation.setLatitude(latitude);
                double distance = gpsLoc.distanceTo(targetLocation) / 1000;
                Party p1 = new Party(place, event, distance, date);
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

        if (gpsLoc == null) {
            Toast.makeText(getActivity(), "GPS koordináta nem elérhető", Toast.LENGTH_LONG).show();
        } else if (gpsLoc != null) {
            String uri = "http://maps.google.com/maps?saddr="+gpsLoc.getLatitude()+","+gpsLoc.getLongitude()+
                    "&daddr="+partyList.get(position).getLatitude()+","+partyList.get(position).getLongitude();
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(i);
            //Todo 0.00 0.000 a célkoordináta
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (lm != null) {
            lm.removeUpdates(locListener);
        }
    }
}
