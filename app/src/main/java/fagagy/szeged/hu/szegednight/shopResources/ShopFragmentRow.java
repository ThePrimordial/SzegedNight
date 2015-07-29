package fagagy.szeged.hu.szegednight.shopResources;

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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pages.MyCurrentLocationListener;

/**
 * Created by TheSorrow on 15/07/28.
 */
public class ShopFragmentRow extends ListFragment implements AdapterView.OnItemClickListener {

    public static final String TAG = "ListaNézet";
    private ArrayList<Shop> shopList = new ArrayList<>();
    private LocationManager lm;
    private MyCurrentLocationListener locListener;
    private Location gpsLoc;
    private Location networkLoc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = View.inflate(getActivity(), R.layout.shopfragmentrow, null);
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

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int currHour = calendar.get(Calendar.HOUR_OF_DAY);
        String sDay = getDay(day);
        generateRows(sDay, currHour);

        ShopAdapter shopAdapter = new ShopAdapter(getActivity(), shopList);
        setListAdapter(shopAdapter);
        registerForContextMenu(getListView());
        getListView().setOnItemClickListener(this);

    }

    private String getDay(int day) {
        switch (day){
            case 1:return "Sunday";
            case 2:return "Monday";
            case 3:return "Tuesday";
            case 4:return "Wednesday";
            case 5:return "Thursday";
            case 6:return "Friday";
            case 7:return "Saturday";
        }
        return null;
    }

    private void generateRows(String day, int currHour) {
        List<ParseObject> serverList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Shop");
        try {
            serverList = query.fromPin("Shop").find();
        } catch (ParseException e1) {
        }

        if (gpsLoc == null && networkLoc == null) {
            Toast.makeText(getActivity(), "Nem érhető el a jelenlegi pozíció!", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < serverList.size(); i++) {
                String name = serverList.get(i).getString("Name");
                double distance  = 0.00;
                Boolean open = checkOpen(serverList,  day, currHour, i);
                Shop s1 = new Shop(name, open, distance);
                shopList.add(s1);
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
                Boolean open = checkOpen(serverList, day, currHour, i);
                String openUntil = getOpenUntil(serverList, day, currHour, i);
                if(!open) {
                    Shop s1 = new Shop(name, false, distance);
                    s1.setLatitude(latitude);
                    s1.setLongitude(longitude);
                    shopList.add(s1);
                }else{
                    Shop s1 = new Shop(name, true, distance, openUntil);
                    s1.setLatitude(latitude);
                    s1.setLongitude(longitude);
                    shopList.add(s1);
                }
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
                Boolean open = checkOpen(serverList, day, currHour, i);
                String openUntil = getOpenUntil(serverList, day, currHour, i);
                if(!open) {
                    Shop s1 = new Shop(name, false, distance);
                    s1.setLatitude(latitude);
                    s1.setLongitude(longitude);
                    shopList.add(s1);
                }else{
                    Shop s1 = new Shop(name, true, distance, openUntil);
                    s1.setLatitude(latitude);
                    s1.setLongitude(longitude);
                    shopList.add(s1);
                }
            }

        Collections.sort(shopList, new Comparator<Shop>() {
            @Override
            public int compare(Shop s1, Shop s2) {
                return Double.compare(s1.getDistance(), s2.getDistance());
            }
        });
    }

    private String getOpenUntil(List<ParseObject> serverList, String day, int currHour, int position) {

        try {
            if (String.valueOf(serverList.get(position).getJSONArray(day).get(1)).equals("24")){
                return "0";
            }else {
                return String.valueOf(serverList.get(position).getJSONArray(day).get(1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Database error";
    }

    private Boolean checkOpen(List<ParseObject> serverList, String day, int currHour, int position) {

        int openHour = 0;
        int closeHour = 0;

        try {
            openHour = Integer.parseInt(String.valueOf(serverList.get(position).getJSONArray(day).get(0)));
            closeHour = Integer.parseInt(String.valueOf(serverList.get(position).getJSONArray(day).get(1)));
            Log.d("yxcv", "openhour" + String.valueOf(serverList.get(position).getJSONArray(day).get(0)));
            Log.d("yxcv", "closehour" + String.valueOf(serverList.get(position).getJSONArray(day).get(1)));
            Log.d("yxcv", "curr" + String.valueOf(currHour));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ;

        if (openHour <= currHour && currHour < closeHour) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String uri =null;
        if (gpsLoc == null && networkLoc == null) {
            Toast.makeText(getActivity(), "GPS koordináta vagy Internet kapcsolat nem elérhető", Toast.LENGTH_LONG).show();
        } else if (gpsLoc != null) {
            uri = "http://maps.google.com/maps?saddr="+gpsLoc.getLatitude()+","+gpsLoc.getLongitude()+
                    "&daddr="+shopList.get(position).getLatitude()+","+shopList.get(position).getLongitude();
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(i);
        } else
            uri = "http://maps.google.com/maps?saddr="+networkLoc.getLatitude()+","+networkLoc.getLongitude()+
                    "&daddr="+shopList.get(position).getLatitude()+","+shopList.get(position).getLongitude();
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

