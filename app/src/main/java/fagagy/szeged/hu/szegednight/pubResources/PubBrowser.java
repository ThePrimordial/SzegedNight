package fagagy.szeged.hu.szegednight.pubResources;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.atmResources.AtmBrowser;
import fagagy.szeged.hu.szegednight.pages.FragmentAdapter;
import fagagy.szeged.hu.szegednight.partyResources.PartyBrowser;
import fagagy.szeged.hu.szegednight.restaurantResources.RestaurantBrowser;
import fagagy.szeged.hu.szegednight.shopResources.ShopBrowser;
import fagagy.szeged.hu.szegednight.tobaccoResources.TobaccoBrowser;

public class PubBrowser extends AppCompatActivity{

    public static FragmentManager fragmentManager;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing_with_swipe);

        fragmentManager = getSupportFragmentManager();
        Fragment pubFragmentRow = new PubFragmentList();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), pubFragmentRow, "Pub");
        ViewPager p = (ViewPager) findViewById(R.id.pager);
        p.setAdapter(adapter);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Set the menu icon instead of the launcher icon.
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

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
            default:
                i.setClass(this, ShopBrowser.class);
                startActivity(i);
        }

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.info) {
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
        }else if (id == R.id.help) {
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_starting_page, menu);
        return true;
    }
}