package fagagy.szeged.hu.szegednight.pages;

import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import fagagy.szeged.hu.szegednight.R;

public class FragmentMap extends Fragment {

    public MapView mMapView;
    public GoogleMap googleMap;
    public static final String TAG = "Térképnézet";


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
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setUpMarkers(type);
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


    private void setUpMarkers(String type) {
        googleMap = mMapView.getMap();
        List<ParseObject> serverList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(type);
        try {
            serverList = query.fromPin(type).find();
        } catch (ParseException e1) {
        }

        for (int i = 0; i < serverList.size(); i++) {

            String name = serverList.get(i).getString("Name");
            Double longitude = serverList.get(i).getDouble("Longitude");
            Double latitude = serverList.get(i).getDouble("Latitude");

            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(latitude, longitude));
            googleMap.addMarker(marker).setTitle(name);

        }

        LocationManager mng = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        MyCurrentLocationListener listener = new MyCurrentLocationListener();
        Location location = getLocation(mng, listener);
        double myLat = location.getLatitude();
        double myLong = location.getLongitude();

        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(myLat, myLong)).title("Saját Pozíció");
        googleMap.addMarker(marker).showInfoWindow();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(myLat, myLong), 15);
        googleMap.animateCamera(cameraUpdate);
    }

    private Location getLocation(LocationManager locationManager, MyCurrentLocationListener listener) {

        Location location = null;

        try {

            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(getActivity(), "Nincs internet vagy GPS pozíció", Toast.LENGTH_LONG).show();
            } else {
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20000, 50, listener);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        Log.d("lokáció", "network pz megtalálva");
                    }
                } else
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER, 20000, 50, listener);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            Log.d("lokáció", "gps pz megtalálva");
                        }
                    }
                }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }
    }
