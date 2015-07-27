package fagagy.szeged.hu.szegednight.atmRescources;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pages.MyCurrentLocationListener;

/**
 * Created by TheSorrow on 15/07/23.
 */
public class AtmFragmentList extends ListFragment implements AdapterView.OnItemClickListener {

    public static final String TAG = "ListaNézet";
    private ArrayList<Atm> atmList = new ArrayList<>();
    private LocationManager lm;
    private MyCurrentLocationListener locListener;
    private Location gpsLoc;
    private Location networkLoc;
    private MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = View.inflate(getActivity(), R.layout.atmfragmentrow, null);
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locListener = new MyCurrentLocationListener();
        lm.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 20000, 50,
                locListener);
        lm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 20000, 50,
                locListener);
        gpsLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        networkLoc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
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

        if (gpsLoc == null && networkLoc == null) {
            Toast.makeText(getActivity(), "Nem érhető el a jelenlegi pozíció!", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < serverList.size(); i++) {
                String name = serverList.get(i).getString("Name");
                double distance = 0.00;
                String type = serverList.get(i).getString("Type");
                Atm a1 = new Atm(name, type, distance);
                atmList.add(a1);
            }
        } else if (gpsLoc != null) {
            for (int i = 0; i < serverList.size(); i++) {
                String name = serverList.get(i).getString("Name");
                Location targetLocation = new Location("");
                double longitude = serverList.get(i).getDouble("Longitude");
                double latitude = serverList.get(i).getDouble("Latitude");
                targetLocation.setLongitude(longitude);
                targetLocation.setLatitude(latitude);
                double distance = gpsLoc.distanceTo(targetLocation) / 1000;
                String type = serverList.get(i).getString("Type");
                Atm a1 = new Atm(name, type, distance);
                a1.setLatitude(latitude);
                a1.setLongitude(longitude);
                Log.d("atmtype", a1.getType());
                atmList.add(a1);
            }
        } else
            for (int i = 0; i < serverList.size(); i++) {
                String name = serverList.get(i).getString("Name");
                Location targetLocation = new Location("");
                double longitude = serverList.get(i).getDouble("Longitude");
                double latitude = serverList.get(i).getDouble("Latitude");
                targetLocation.setLongitude(longitude);
                targetLocation.setLatitude(latitude);
                double distance = networkLoc.distanceTo(targetLocation) / 1000;
                String type = serverList.get(i).getString("Type");
                Atm a1 = new Atm(name, type, distance);
                a1.setLatitude(latitude);
                a1.setLongitude(longitude);
                Log.d("atmtype", a1.getType());
                atmList.add(a1);
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

        String uri =null;
        if (gpsLoc == null && networkLoc == null) {
            Toast.makeText(getActivity(), "GPS koordináta vagy Internet kapcsolat nem elérhető", Toast.LENGTH_LONG).show();
        } else if (gpsLoc != null) {
            uri = "http://maps.google.com/maps?saddr="+gpsLoc.getLatitude()+","+gpsLoc.getLongitude()+
                    "&daddr="+atmList.get(position).getLatitude()+","+atmList.get(position).getLongitude();
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(i);
        } else if(networkLoc != null)
            uri = "http://maps.google.com/maps?saddr="+networkLoc.getLatitude()+","+networkLoc.getLongitude()+
                    "&daddr="+atmList.get(position).getLatitude()+","+atmList.get(position).getLongitude();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(i);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (lm != null) {
            lm.removeUpdates(locListener);
        }
    }

}
