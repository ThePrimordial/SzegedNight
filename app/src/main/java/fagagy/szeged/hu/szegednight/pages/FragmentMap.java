package fagagy.szeged.hu.szegednight.pages;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import fagagy.szeged.hu.szegednight.R;

public class FragmentMap extends Fragment {

    private MapView mMapView;
    private GoogleMap googleMap;
    public static String TAG = "Mapview";
    private Location location;
    private boolean googlePlayServicesAvaible;


    public static FragmentMap newInstance(String type) {
        FragmentMap map = new FragmentMap();
        Bundle bdl = new Bundle(2);
        bdl.putString("TYPE", type);
        map.setArguments(bdl);
        return map;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_map_fragment, container,
                false);
        String type = getArguments().getString("TYPE");
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        MapsInitializer.initialize(getContext());
        TAG = getContext().getResources().getString(R.string.MapView);
        setUpMarkers(type, v.getContext());
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    private void setUpMarkers(String type, Context context) {
        googleMap = mMapView.getMap();
        List<ParseObject> serverList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(type);
        try {
            serverList = query.fromPin(type).find();
        } catch (ParseException e1) {
        }

        if (googleMap != null) {
            for (int i = 0; i < serverList.size(); i++) {

                String name = serverList.get(i).getString("Name");
                Double longitude = serverList.get(i).getDouble("Longitude");
                Double latitude = serverList.get(i).getDouble("Latitude");

                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(latitude, longitude));
                googleMap.addMarker(marker).setTitle(name);
            }
        }

        googlePlayServicesAvaible = checkGooglePlayServiceAvailability(getContext(), -1);
        if (googlePlayServicesAvaible) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(46.253010, 20.141425), 14);
            googleMap.animateCamera(cameraUpdate);

            LocationObserver observer = new LocationObserver(context, 20000, 50, 30000);
            observer.start();
            location = observer.getLastKnownLocation();

            if (location != null) {
                double myLat = location.getLatitude();
                double myLong = location.getLongitude();
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(myLat, myLong)).title("Saját Pozíció");
                googleMap.addMarker(marker).showInfoWindow();

                cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(myLat, myLong), 15);
                googleMap.animateCamera(cameraUpdate);
            }
        } else {
            if (location != null) {
                double myLat = location.getLatitude();
                double myLong = location.getLongitude();
                MarkerOptions marker = new MarkerOptions().position(
                        new LatLng(myLat, myLong)).title("Saját Pozíció");
                googleMap.addMarker(marker).showInfoWindow();
            }
        }
    }

    public static boolean checkGooglePlayServiceAvailability(Context context, int versionCode) {

        int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if ((statusCode == ConnectionResult.SUCCESS)
                && (GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE >= versionCode)) {
            return true;
        } else {
            return false;
        }
    }

}
