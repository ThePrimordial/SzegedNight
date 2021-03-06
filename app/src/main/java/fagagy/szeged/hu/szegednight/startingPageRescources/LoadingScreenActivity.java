package fagagy.szeged.hu.szegednight.startingPageRescources;

/**
 * Created by TheSorrow on 15/09/24.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.ArrayList;
import java.util.List;

import fagagy.szeged.hu.szegednight.R;

public class LoadingScreenActivity extends Activity {

    private Shimmer shimmer;
    private ShimmerTextView shimmerText;

    //TODO GPS, Network enabling option

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
        shimmer = new Shimmer();
        shimmerText = (ShimmerTextView) findViewById(R.id.shimmer_tv);
        shimmer.start(shimmerText);
        new LoadViewTask().execute();
    }

    private class LoadViewTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            if (!isNetworkAvailable()) {
                Toast.makeText(getApplicationContext(), R.string.NoInternetConnection, Toast.LENGTH_LONG).show();
                shimmer.cancel();
                Intent i = new Intent();
                i.setClass(getApplicationContext(), StartingPage.class);
                startActivity(i);
                finish();
            }
            }


            @Override
            protected Void doInBackground (Void...params){
                try {
                    List<ParseObject> serverList;
                    List<ParseObject> deleteList;
                    ArrayList<String> whatToRefresh = new ArrayList<>();

                    whatToRefresh.add("Pub");
                    whatToRefresh.add("Restaurant");
                    whatToRefresh.add("ATM");
                    whatToRefresh.add("Tobacco");
                    whatToRefresh.add("Party");
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

                return null;
            }


        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(getApplicationContext(), R.string.SuccesfulDatabaseUpdate, Toast.LENGTH_LONG).show();
            shimmer.cancel();
            Intent i = new Intent();
            i.setClass(getApplicationContext(), StartingPage.class);
            startActivity(i);
            finish();
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
    }
}
