package fagagy.szeged.hu.szegednight.restaurantRescources;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pages.MyCurrentLocationListener;
import fagagy.szeged.hu.szegednight.pubRescources.Pub;

public class RestaurantFragmentList extends ListFragment implements OnItemClickListener {

    ArrayList<Restaurant> resList = new ArrayList<>();
    private LocationManager lm;
    private MyCurrentLocationListener locListener;
    private Location gpsLoc;
    private Location networkLoc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = View.inflate(getActivity(), R.layout.restaurantfragmentrow, null);
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

        RestaurantAdapter adapter = new RestaurantAdapter(getActivity(), resList);
        setListAdapter(adapter);
        registerForContextMenu(getListView());
        getListView().setOnItemClickListener(this);

    }

    private void generateRows() {
        List<ParseObject> serverList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Restaurant");
        try {
            serverList = query.fromPin("Restaurant").find();
        } catch (ParseException e1) {
        }

        if (gpsLoc == null && networkLoc == null) {
            Toast.makeText(getActivity(), "Nem érhető el a jelenlegi pozíció!", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < serverList.size(); i++) {
                String name = serverList.get(i).getString("Name");
                double distance  = 0.00;
                Boolean open = serverList.get(i).getBoolean("Open");
                Restaurant r1 = new Restaurant(name, open, distance);
                resList.add(r1);
            }
        } else if(gpsLoc != null) {
            for (int i = 0; i < serverList.size(); i++) {
                String name = serverList.get(i).getString("Name");
                Location targetLocation = new Location("");
                double longitude = serverList.get(i).getDouble("Longitude");
                double latitude = serverList.get(i).getDouble("Latitude");
                targetLocation.setLongitude(longitude);
                targetLocation.setLatitude(latitude);
                double distance = gpsLoc.distanceTo(targetLocation) / 1000;
                Boolean open = serverList.get(i).getBoolean("Open");
                Restaurant r1 = new Restaurant(name, open, distance);
                r1.setLatitude(latitude);
                r1.setLongitude(longitude);
                resList.add(r1);
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
                Boolean open = serverList.get(i).getBoolean("Open");
                Restaurant r1 = new Restaurant(name, open, distance);
                r1.setLatitude(latitude);
                r1.setLongitude(longitude);
                resList.add(r1);
            }

        Collections.sort(resList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant p1, Restaurant p2) {
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
                    "&daddr="+resList.get(position).getLatitude()+","+resList.get(position).getLongitude();
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(i);
        } else if(networkLoc != null)
            uri = "http://maps.google.com/maps?saddr="+networkLoc.getLatitude()+","+networkLoc.getLongitude()+
                    "&daddr="+resList.get(position).getLatitude()+","+resList.get(position).getLongitude();
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(i);
    }
}
