package fagagy.szeged.hu.szegednight.pages;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import fagagy.szeged.hu.szegednight.R;

/**
 * Created by TheSorrow on 15/07/22.
 */
public class LocationObserver implements LocationListener {

    LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    boolean network_connected = false;
    private static final int THIRTY_SECONDS = 1000 * 30;

    Context context;
    Long timeout;
    int minTime;
    int minDistance;
    Handler handler = new Handler();
    private Location currentLocation = null;

    public LocationObserver(Context pContext, int minTime,
                            int minDistance, long timeout) {

        this.context = pContext;
        this.timeout = timeout;
        this.minDistance = minDistance;
        this.minTime = minTime;
    }


    public void start() {
        if (lm == null)
            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {

        }
        try {
            network_enabled = lm
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            network_connected = activeNetwork != null
                    && activeNetwork.isConnectedOrConnecting();
        } catch (Exception ex) {

        }

        if (!gps_enabled && !network_enabled) {

            Toast.makeText(context, R.string.NoAvaibleLoc, Toast.LENGTH_LONG).show();
        }

        else {
            if (gps_enabled) { // gps enabled, network disabled
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        minTime, minDistance, this);

            } else {

                if (network_connected) {
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            minTime, minDistance, this);

                }


                else  {

                    //ask the user to connect to any network ( Settings.ACTION_WIRELESS_SETTINGS)
                    //TODO Dialog enable wifi gps or none
                }

            }
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Location l = getLastKnownLocation();
                onLocationChanged(l);

            }
        }, timeout);

    }

    public Location getLastKnownLocation() {

        Location net_loc = null, gps_loc = null;
        if (gps_enabled)
            gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (network_enabled)
            net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        // if there are both values use the best one
        if (gps_loc != null && net_loc != null) {
            if (isBetterLocation(gps_loc, net_loc))
                return gps_loc;
            else
                return net_loc;

        }

        else if (gps_loc != null) {
            return gps_loc;

        } else if (net_loc != null) {
            return net_loc;

        } else
            return null;
    }

    public synchronized void onLocationChanged(Location location) {

        if (location == null || currentLocation == null
                || isBetterLocation(location, currentLocation)) {
            currentLocation = location;

        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            return true;
        }

        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > THIRTY_SECONDS;
        boolean isSignificantlyOlder = timeDelta < -THIRTY_SECONDS;
        boolean isNewer = timeDelta > 0;


        if (isSignificantlyNewer) {
            return true;
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

}