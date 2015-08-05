package fagagy.szeged.hu.szegednight.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.atmResources.AtmBrowser;
import fagagy.szeged.hu.szegednight.partyResources.PartyBrowser;
import fagagy.szeged.hu.szegednight.pubResources.PubBrowser;
import fagagy.szeged.hu.szegednight.restaurantResources.RestaurantBrowser;
import fagagy.szeged.hu.szegednight.shopResources.ShopBrowser;
import fagagy.szeged.hu.szegednight.tobaccoResources.TobaccoBrowser;


public class StartingPage extends Activity {


    private LocationManager lm;
    private MyCurrentLocationListener locListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FetchCordinates fetchCordinates = new FetchCordinates();
        fetchCordinates.execute();
        if (!isNetworkAvailable()) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_starting_page);
            Toast.makeText(this, "Nincs internetkapcsolat. Adatbázis elavult lehet!", Toast.LENGTH_LONG)
                    .show();

        } else {
            UpdateDataBase updateDataBase = new UpdateDataBase();
            updateDataBase.execute();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_starting_page);
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        Intent i = new Intent();
        switch (id) {
            case R.id.btnPubs:
                i.setClass(this, PubBrowser.class);
                startActivity(i);
                break;
            case R.id.btnRestaurants:
                i.setClass(this, RestaurantBrowser.class);
                startActivity(i);
                break;
            case R.id.btnATM:
                i.setClass(this, AtmBrowser.class);
                startActivity(i);
                break;
            case R.id.btnTobaccoShops:
                i.setClass(this, TobaccoBrowser.class);
                startActivity(i);
                break;
            case R.id.btnParties:
                Toast.makeText(this, "Oldalra húzva megtekintheted a SZIN eseményeit! (feltöltés alatt)", Toast.LENGTH_SHORT).show();
                i.setClass(this, PartyBrowser.class);
                startActivity(i);
                break;
            case R.id.btnShops:
                i.setClass(this, ShopBrowser.class);
                startActivity(i);
                break;
            case R.id.btnContact:
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
            case R.id.btnInfo:
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
                        "ha megérintesz egy helyet, nem fog tudni odavezetni." +
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
            Toast.makeText(StartingPage.this, "Adatbázis frissítése folyamatban...", Toast.LENGTH_LONG).show();
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
            Toast.makeText(StartingPage.this, "Adatbázis frissítése megtörtént", Toast.LENGTH_LONG).show();
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
            Toast.makeText(StartingPage.this, "Jelenlegi pozíció keresése", Toast.LENGTH_LONG)
                    .show();
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
            if (myLoc == null) {
                Toast.makeText(StartingPage.this, "Nem érhető el GPS pozíció, távolság ismeretlen lesz!", Toast.LENGTH_LONG)
                        .show();
            } else
                Toast.makeText(StartingPage.this, "GPS pozíció megtalálva!", Toast.LENGTH_LONG)
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

