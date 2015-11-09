package fagagy.szeged.hu.szegednight.restaurantResources;

import android.content.Intent;
import android.location.Location;
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

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pages.LocationObserver;

public class RestaurantFragmentList extends ListFragment implements OnItemClickListener {

    ArrayList<Restaurant> resList = new ArrayList<>();
    private LocationObserver observer;
    private Location myLoc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = View.inflate(getActivity(), R.layout.restaurantfragmentrow, null);
        observer = new LocationObserver(v.getContext(), 20000, 50, 30000);
        observer.start();
        myLoc = observer.getLastKnownLocation();
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

        RestaurantAdapter adapter = new RestaurantAdapter(getActivity(), resList);
        setListAdapter(adapter);
        registerForContextMenu(getListView());
        getListView().setOnItemClickListener(this);

    }

    private String getDay(int day) {
        switch (day) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
        }
        return null;
    }

    private void generateRows(String day, int currHour) {
        List<ParseObject> serverList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Restaurant").fromLocalDatastore();
        try {
            serverList = query.fromPin("Restaurant").find();
        } catch (ParseException e1) {
        }

        if (myLoc == null) {
            for (int i = 0; i < serverList.size(); i++) {
                String name = serverList.get(i).getString("Name");
                double distance = 0.00;
                Boolean open = checkOpen(serverList, day, currHour, i);
                String openUntil = getOpenUntil(serverList, day, currHour, i);
                if (!open) {
                    Restaurant r1 = new Restaurant(name, false, distance);
                    resList.add(r1);
                } else {
                    Restaurant r1 = new Restaurant(name, true, distance, openUntil);
                    resList.add(r1);
                }
            }
        } else if (myLoc != null) {
            for (int i = 0; i < serverList.size(); i++) {
                String name = serverList.get(i).getString("Name");
                Location targetLocation = new Location("");
                double longitude = serverList.get(i).getDouble("Longitude");
                double latitude = serverList.get(i).getDouble("Latitude");
                targetLocation.setLongitude(longitude);
                targetLocation.setLatitude(latitude);
                double distance = myLoc.distanceTo(targetLocation) / 1000;
                Boolean open = checkOpen(serverList, day, currHour, i);
                String openUntil = getOpenUntil(serverList, day, currHour, i);
                if (!open) {
                    Restaurant r1 = new Restaurant(name, false, distance);
                    r1.setLatitude(latitude);
                    r1.setLongitude(longitude);
                    resList.add(r1);
                } else {
                    Restaurant r1 = new Restaurant(name, true, distance, openUntil);
                    r1.setLatitude(latitude);
                    r1.setLongitude(longitude);
                    resList.add(r1);
                }
            }
        }
        Collections.sort(resList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant p1, Restaurant p2) {
                return Double.compare(p1.getDistance(), p2.getDistance());
            }
        });
    }

    private String getOpenUntil(List<ParseObject> serverList, String day, int currHour, int position) {

        try {
            if (String.valueOf(serverList.get(position).getJSONArray(day).get(1)).equals("24")) {
                return "0";
            } else {
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (openHour == closeHour) {
            return false;
        }

        if (openHour <= currHour && currHour < closeHour) {
            return true;
        } else if (closeHour < 7) {
            if (openHour <= currHour && currHour < 24) {
                return true;
            } else if (0 <= currHour && currHour < closeHour) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (myLoc == null) {
            Toast.makeText(getActivity(), R.string.NoAvaibleLoc, Toast.LENGTH_LONG).show();
        } else {
           String uri = "http://maps.google.com/maps?saddr=" + myLoc.getLatitude() + "," + myLoc.getLongitude() +
                    "&daddr=" + resList.get(position).getLatitude() + "," + resList.get(position).getLongitude();
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(i);
        }
    }
}
