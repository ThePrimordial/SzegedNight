package fagagy.szeged.hu.szegednight.startingPageRescources;

/**
 * Created by TheSorrow on 15/09/24.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pages.MyCurrentLocationListener;

public class LoadingScreenActivity extends Activity {

    private ProgressDialog progressDialog;
    private LocationManager lm;
    private MyCurrentLocationListener locListener;
    private Location myLoc;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LoadViewTask().execute();

    }

    private class LoadViewTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LoadingScreenActivity.this, "Loading...",
                    "Loading application View, please wait...", false, false);

            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locListener = new MyCurrentLocationListener();
            lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 50,
                    locListener);
            myLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }

        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params) {
            if (isNetworkAvailable()) {
                try {
                    List<ParseObject> serverList;
                    List<ParseObject> deleteList;
                    ArrayList<String> whatToRefresh = new ArrayList<>();

                    whatToRefresh.add("Pub");
                    whatToRefresh.add("Restaurant");
                    whatToRefresh.add("ATM");
                    whatToRefresh.add("Tobacco");
                    whatToRefresh.add("Party");
                    whatToRefresh.add("Shop");
                    whatToRefresh.add("Subscribed");
                    for (int i = 0; i < whatToRefresh.size(); i++) {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery(whatToRefresh.get(i));
                        ParseQuery<ParseObject> queryDelete = ParseQuery.getQuery(whatToRefresh.get(i)).fromLocalDatastore();
                        serverList = query.find();
                        deleteList = queryDelete.find();
                        ParseObject.unpinAll(whatToRefresh.get(i), deleteList);
                        ParseObject.pinAll(whatToRefresh.get(i), serverList);
                    }
                } catch (ParseException ignored) {

                }
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            if (!isNetworkAvailable()) {
                Toast.makeText(LoadingScreenActivity.this, "Nincs Internetkapcsolat, adatbázis elavult lehet!", Toast.LENGTH_SHORT).show();
            }
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            if (isNetworkAvailable()) {
                Toast.makeText(getApplicationContext(), "Adatbázis frissítése megtörtént", Toast.LENGTH_LONG).show();
            }

            Intent i = new Intent();
            i.setClass(getApplicationContext(), StartingPage.class);
            startActivity(i);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (lm != null) {
            lm.removeUpdates(locListener);
        }
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}
