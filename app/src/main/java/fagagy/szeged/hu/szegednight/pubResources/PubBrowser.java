package fagagy.szeged.hu.szegednight.pubResources;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
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
import fagagy.szeged.hu.szegednight.startingPageRescources.FragmentDrawer;
import fagagy.szeged.hu.szegednight.tobaccoResources.TobaccoBrowser;

public class PubBrowser extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{

    public static FragmentManager fragmentManager;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing_with_swipe);

        fragmentManager = getSupportFragmentManager();
        Fragment pubFragmentRow = new PubFragmentList();
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), pubFragmentRow, "Pub");
        ViewPager p = (ViewPager) findViewById(R.id.pager);
        p.setAdapter(adapter);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                fragmentManager.findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

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
            default:
                break;
        }
    }
}