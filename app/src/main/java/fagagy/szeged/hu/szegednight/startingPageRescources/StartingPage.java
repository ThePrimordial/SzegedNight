package fagagy.szeged.hu.szegednight.startingPageRescources;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
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


public class StartingPage extends AppCompatActivity {

    private LocationManager lm;
    private MyCurrentLocationListener locListener;
    private DrawerLayout mDrawer;
    private List<ParseObject> subscribedServerList = null;
    //TODO csak indításkor frissüljön az adatb

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

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Subscribed").fromLocalDatastore();


        List <String> identifiers = new ArrayList<>();
        identifiers.add("Cool Club");
        try {
            subscribedServerList = query.fromPin("Subscribed").find();
        } catch (ParseException ignored) {
        }

        if(subscribedServerList.size() != 0) {
            identifiers.clear();
            for (int i = 0; i < subscribedServerList.size(); i++) {
                identifiers.add(subscribedServerList.get(i).getString("Name"));
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Set the menu icon instead of the launcher icon.
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new RecyclerAdapter(identifiers);
        recyclerView.setAdapter(adapter);

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }


    public void selectDrawerItem(MenuItem menuItem) {

        Intent i = new Intent();
        switch (menuItem.getItemId()) {
            case R.id.nav_pub_fragment:
                i.setClass(this, PubBrowser.class);
                startActivity(i);
                break;
            case R.id.nav_party_fragment:
                i.setClass(this, PartyBrowser.class);
                startActivity(i);
                break;
            case R.id.nav_restaurant_fragment:
                i.setClass(this, RestaurantBrowser.class);
                startActivity(i);
                break;
            case R.id.nav_atm_fragment:
                i.setClass(this, AtmBrowser.class);
                startActivity(i);
                break;
            case R.id.nav_shop_fragment:
                i.setClass(this, ShopBrowser.class);
                startActivity(i);
                break;
            case R.id.nav_tobacco_fragment:
                i.setClass(this, TobaccoBrowser.class);
                startActivity(i);
                break;
            case R.id.nav_info:
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
            case R.id.nav_connection:
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
            default:
                break;
        }

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_starting_page, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            case R.id.info:
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
            case R.id.help:
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
            default:break;
        }
        return super.onOptionsItemSelected(item);
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
            Toast.makeText(getApplicationContext(), "Adatbázis frissítése megtörtént", Toast.LENGTH_LONG).show();
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

