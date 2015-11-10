package fagagy.szeged.hu.szegednight.startingPageRescources;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import fagagy.szeged.hu.szegednight.tobaccoResources.TobaccoBrowser;


public class StartingPage extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private List<ParseObject> subscribedServerList = null;
    private static boolean isGPSEnabledAsked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page_material);

        if(!isGPSEnabledAsked) {
            statusCheck();
            isGPSEnabledAsked = true;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Subscribed").fromLocalDatastore();
        List<String> identifiers = new ArrayList<>();
        identifiers.add("Cool Club");
        try {
            subscribedServerList = query.fromPin("Subscribed").find();
        } catch (ParseException ignored) {
        }

        if (subscribedServerList.size() != 0) {
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
        ab.setHomeAsUpIndicator(R.drawable.home);
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


    //TODO start drawing after nav drawer sliding

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
                        "            GPS vagy Internet használata szükséges. Ha nincs bekapcsolva csak a nyitvatartást láthatod a távolságot nem, valamint " +
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
                        "            GPS vagy Internet használata szükséges. Ha nincs bekapcsolva csak a nyitvatartást láthatod a távolságot nem, valamint " +
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
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View infoView = View.inflate(this, R.layout.gpsenable, null);
        TextView infoText = (TextView) infoView.findViewById(R.id.tw_Question);
        builder.setCancelable(false)
                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        dialog.cancel();
                    }
                })
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                })
                .setView(infoView)
                .setIcon(android.R.drawable.ic_dialog_alert);
        infoText.setText(R.string.GpsEnableQuestion);
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, R.string.pressBackToExit,
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }
}

