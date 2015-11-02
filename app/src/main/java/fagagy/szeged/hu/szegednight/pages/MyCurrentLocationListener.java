package fagagy.szeged.hu.szegednight.pages;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by TheSorrow on 15/07/22.
 */
public class MyCurrentLocationListener implements LocationListener {

    public void onLocationChanged(Location location) {
        location.getLatitude();
        location.getLongitude();
    }

    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    public void onProviderEnabled(String s) {

    }

    public void onProviderDisabled(String s) {

    }
}