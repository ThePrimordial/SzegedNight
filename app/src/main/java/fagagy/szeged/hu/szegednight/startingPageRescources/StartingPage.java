package fagagy.szeged.hu.szegednight.startingPageRescources;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.atmResources.AtmBrowser;
import fagagy.szeged.hu.szegednight.pages.MyCurrentLocationListener;
import fagagy.szeged.hu.szegednight.partyResources.PartyBrowser;
import fagagy.szeged.hu.szegednight.pubResources.PubBrowser;
import fagagy.szeged.hu.szegednight.restaurantResources.RestaurantBrowser;
import fagagy.szeged.hu.szegednight.shopResources.ShopBrowser;
import fagagy.szeged.hu.szegednight.tobaccoResources.TobaccoBrowser;


public class StartingPage extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private LocationManager lm;
    private MyCurrentLocationListener locListener;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FetchCordinates fetchCordinates = new FetchCordinates();
        fetchCordinates.execute();
        if (!isNetworkAvailable()) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_starting_page_material);
            Toast.makeText(this, "Nincs internetkapcsolat. Adatbázis elavult lehet!", Toast.LENGTH_LONG)
                    .show();

        } else {
            UpdateDataBase updateDataBase = new UpdateDataBase();
            updateDataBase.execute();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_starting_page_material);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_starting_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        onClick(position);
    }


    public void onClick(int position) {
        Intent i = new Intent();
        switch (position) {
            case 0:
                i.setClass(this, PubBrowser.class);
                startActivity(i);
                break;
            case 1:
                i.setClass(this, PartyBrowser.class);
                startActivity(i);
                break;
            case 2:
                i.setClass(this, RestaurantBrowser.class);
                startActivity(i);
                break;
            case 3:
                i.setClass(this, ShopBrowser.class);
                startActivity(i);
                break;
            case 4:
                i.setClass(this, AtmBrowser.class);
                startActivity(i);
                break;
            case 5:
                i.setClass(this, TobaccoBrowser.class);
                startActivity(i);
                break;
            case 6:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");

                StringBuilder sb = new StringBuilder();
                sb.append("Hely neve:");
                sb.append('\n');
                sb.append("Címe:");
                sb.append('\n');
                sb.append("Javítandó adat:");

                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"szeged.nights@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Helyadat változtatás");
                intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
                startActivity(Intent.createChooser(intent, ""));
                break;
            case 7:
                View infoView = View.inflate(this, R.layout.info_view, null);
                TextView infoText = (TextView) infoView.findViewById(R.id.infoText);
                infoText.setText(Html.fromHtml("<ul>\n" +
                        "    <li>\n" +
                        "        <p align=\"left\">\n" +
                        "            Az alkalmazás a jelenlegi pozíciódtól való távolság sorrendjében kilistázza a választott helyeket, " +
                        "               valamint mutatja ha az adott hely épp nyitva van\n" +
                        "            (és meddig).\n" +
                        "        </p>\n" +
                        "    </li>\n" +
                        "</ul>\n" +
                        "<ul>\n" +
                        "    <li>\n" +
                        "        <p align=\"left\">\n" +
                        "            GPS használata szükséges. Ha nincs bekapcsolva csak a nyitvatartást láthatod a távolságot nem, valamint " +
                        "ha megérintesz egy helyet nem fog tudni odavezetni." +
                        "\n" +
                        "        </p>\n" +
                        "    </li>\n" +
                        "</ul>\n" +
                        "<ul>\n" +
                        "    <li>\n" +
                        "        <p align=\"left\">\n" +
                        "            Minden indításkor automatikusan frissül az adatbázis. Ha épp nincs internetkapcsolatod akkor is láthatod az egyes helyeket, eseményeket de ilyenkor\n" +
                        "            az adatok elavultak lehetnek.\n" +
                        "        </p>\n" +
                        "    </li>\n" +
                        "</ul>\n" +
                        "<ul>\n" +
                        "    <li>\n" +
                        "        <p align=\"left\">\n" +
                        "            Térkép nézeten láthatod előre bejelölve az összes helyet, abban a témában amit kiválasztottál.\n" +
                        "        </p>\n" +
                        "    </li>\n" +
                        "</ul>\n" +
                        "<ul>\n" +
                        "    <li>\n" +
                        "        <p align=\"left\">\n" +
                        "            <strong>FONTOS: </strong>\n" +
                        "            A fejlesztő nem vállal felelősséget az adatok pontosságáért. Különösen a kocsmák, dohányboltok esetében gyakran változik a nyitvatartás, helyek\n" +
                        "            zárnak be, újak nyitnak ki. Ezért hogy minél pontosabb adatokkal tudjunk szolgálni a Te segítségedre is szükség van!\n" +
                        "            <br/>\n" +
                        "        </p>\n" +
                        "        <p align=\"left\">\n" +
                        "Bármilyen helyet találsz ami nem szerepel, vagy olyat ami szerepel de rossz adatokkal, esetleg már nem létezik, kérjük a <strong>“Javítás kérése” </strong>menüpont alatt jelentsd nekünk.\n" +
                        "        </p>\n" +
                        "        <p align=\"left\">\n" +
                        "            Itt kizárólag egy előre formázott emailt kell kitöltened három adattal, egy hely névvel-címmel, illetve mi az amit változtassunk rajta. Ez nagyon\n" +
                        "            nagy segítség nekünk, hogy naprakész adatokat tudjunk Neked is nyújtani.\n" +
                        "        </p>\n" +
                        "    </li>\n" +
                        "</ul>"));
                infoText.setTextSize(18);
                new AlertDialog.Builder(this)
                        .setTitle("Info")
                        .setView(infoView)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                break;
            default:
                break;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public class UpdateDataBase extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {
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
                whatToRefresh.add("Golyatabor");
                whatToRefresh.add("SZIN");
                for (int i = 0; i < whatToRefresh.size(); i++) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(whatToRefresh.get(i));
                    ParseQuery<ParseObject> queryDelete = ParseQuery.getQuery(whatToRefresh.get(i)).fromLocalDatastore();
                    serverList = query.find();
                    deleteList = queryDelete.find();
                    ParseObject.unpinAll(whatToRefresh.get(i), deleteList);
                    ParseObject.pinAll(whatToRefresh.get(i), serverList);
                }
            } catch (ParseException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }

    public class FetchCordinates extends AsyncTask<String, Integer, String> {

        Location myLoc;

        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPreExecute() {
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locListener = new MyCurrentLocationListener();
            lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 50,
                    locListener);
            myLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (myLoc == null)
                Toast.makeText(StartingPage.this, "Nem érhető el GPS pozíció, távolság ismeretlen lesz!", Toast.LENGTH_LONG)
                        .show();
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

