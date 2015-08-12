package fagagy.szeged.hu.szegednight.tobaccoResources;

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
 * Created by TheSorrow on 15/07/27.
 */
public class TobaccoFragmentList extends ListFragment implements AdapterView.OnItemClickListener {

    public static final String TAG = "ListaNézet";
    private ArrayList<Tobacco> tobaccoList = new ArrayList<>();
    private LocationManager lm;
    private MyCurrentLocationListener locListener;
    private Location gpsLoc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = View.inflate(getActivity(), R.layout.tobaccofragmentrow, null);
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locListener = new MyCurrentLocationListener();
        lm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 20000, 50,
                locListener);
        gpsLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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

        TobaccoAdapter tobaccoAdapter = new TobaccoAdapter(getActivity(), tobaccoList);
        setListAdapter(tobaccoAdapter);
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tobacco");
        try {
            serverList = query.fromPin("Tobacco").find();
        } catch (ParseException e1) {
        }

        if (gpsLoc == null) {
            Toast.makeText(getActivity(), "Nem érhető el a jelenlegi pozíció!", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < serverList.size(); i++) {
                String name = serverList.get(i).getString("Name");
                double distance = 0.00;
                Boolean open = checkOpen(serverList, day, currHour, i);
                try {
                    int openingtime = Integer.parseInt(String.valueOf(serverList.get(i).getJSONArray(day).get(0)));
                    String openUntil = getOpenUntil(serverList, day, currHour, i);
                    if (!open) {
                        Tobacco t1 = new Tobacco(name, false, distance, openingtime);
                        tobaccoList.add(t1);
                    } else {
                        Tobacco t1 = new Tobacco(name,true,  distance, openUntil);
                        tobaccoList.add(t1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                Boolean open = checkOpen(serverList, day, currHour, i);
                try {
                    int openingtime = Integer.parseInt(String.valueOf(serverList.get(i).getJSONArray(day).get(0)));
                    String openUntil = getOpenUntil(serverList, day, currHour, i);
                    if (!open) {
                        Tobacco t1 = new Tobacco(name, false, distance, openingtime);
                        tobaccoList.add(t1);
                    } else {
                        Tobacco t1 = new Tobacco(name, true,  distance, openUntil);
                        tobaccoList.add(t1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        Collections.sort(tobaccoList, new Comparator<Tobacco>() {
            @Override
            public int compare(Tobacco t1, Tobacco t2) {
                return Double.compare(t1.getDistance(), t2.getDistance());
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

        if (gpsLoc == null) {
            Toast.makeText(getActivity(), "GPS koordináta nem elérhető", Toast.LENGTH_LONG).show();
        } else if (gpsLoc != null) {
            String uri = "http://maps.google.com/maps?saddr=" + gpsLoc.getLatitude() + "," + gpsLoc.getLongitude() +
                    "&daddr=" + tobaccoList.get(position).getLatitude() + "," + tobaccoList.get(position).getLongitude();
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(i);
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
