package fagagy.szeged.hu.szegednight.pubRescources;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pages.MyCurrentLocationListener;
import fagagy.szeged.hu.szegednight.pages.StartingPage;

/**
 * Created by TheSorrow on 15/07/20.
 */
public class PubFragmentList extends ListFragment implements OnItemClickListener {

    public static final String TAG = "ListaNézet";
    private ArrayList<Pub> pubList = new ArrayList<>();
    private LocationManager lm;
    private MyCurrentLocationListener locListener;
    private Location gpsLoc;
    private Location networkLoc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = View.inflate(getActivity(), R.layout.pubfragmentrow, null);
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

        PubAdapter pubAdapter = new PubAdapter(getActivity(), pubList);
        setListAdapter(pubAdapter);
        registerForContextMenu(getListView());
        getListView().setOnItemClickListener(this);

    }

    private void generateRows() {
        List<ParseObject> serverList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Pub");
        try {
            serverList = query.fromPin("Pub").find();
        } catch (ParseException e1) {
        }

        if (gpsLoc == null && networkLoc == null) {
            Toast.makeText(getActivity(), "Nem érhető el a jelenlegi pozíció!", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < serverList.size(); i++) {
                String name = serverList.get(i).getString("Name");
                double distance  = 0.00;
                Boolean open = serverList.get(i).getBoolean("Open");
                Pub p1 = new Pub(name, open, distance);
                pubList.add(p1);
            }
        } else if(gpsLoc != null) {
            for (int i = 0; i < serverList.size(); i++) {
                String name = serverList.get(i).getString("Name");
                Location targetLocation = new Location("");
                double longitude = serverList.get(i).getDouble("Longitude");
                double latitude = serverList.get(i).getDouble("Latitude");
                targetLocation.setLongitude(longitude);
                targetLocation.setLatitude(latitude);
                double distance = networkLoc.distanceTo(targetLocation) / 1000;
                Boolean open = serverList.get(i).getBoolean("Open");
                Pub p1 = new Pub(name, open, distance);
                p1.setLatitude(latitude);
                p1.setLongitude(longitude);
                pubList.add(p1);
            }
        } else
            for (int i = 0; i < serverList.size(); i++) {
                String name = serverList.get(i).getString("Name");
                Location targetLocation = new Location("");
                double longitude = serverList.get(i).getDouble("Longitude");
                double latitude = serverList.get(i).getDouble("Latitude");
                targetLocation.setLongitude(longitude);
                targetLocation.setLatitude(latitude);
                double distance = gpsLoc.distanceTo(targetLocation) / 1000;
                Boolean open = serverList.get(i).getBoolean("Open");
                Pub p1 = new Pub(name, open, distance);
                p1.setLatitude(latitude);
                p1.setLongitude(longitude);
                pubList.add(p1);
            }

        Collections.sort(pubList, new Comparator<Pub>() {
            @Override
            public int compare(Pub p1, Pub p2) {
                return Double.compare(p1.getDistance(), p2.getDistance());
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
                    "&daddr="+pubList.get(position).getLatitude()+","+pubList.get(position).getLongitude();
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(i);
        } else if(networkLoc != null)
            uri = "http://maps.google.com/maps?saddr="+networkLoc.getLatitude()+","+networkLoc.getLongitude()+
                    "&daddr="+pubList.get(position).getLatitude()+","+pubList.get(position).getLongitude();
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

