package fagagy.szeged.hu.szegednight.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;

import java.util.List;

import fagagy.szeged.hu.szegednight.R;

public class SubscribedPage extends Activity {

    private String identifier;
    private int pubRowNumber = 0;
    private int subscribedRowNumber = 0;
    private Location location;
    private List<ParseObject> pubServerList = null;
    private List<ParseObject> subscribedServerList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed_page);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        identifier = (String) b.get("objectId");
        Log.d("identifier1", "Identifier onCreate: " + identifier);

        ParseQuery<ParseObject> pubQuery = ParseQuery.getQuery("Pub").fromLocalDatastore();
        try {
            pubServerList = pubQuery.fromPin("Pub").find();
        } catch (ParseException ignored) {
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Subscribed").fromLocalDatastore();
        try {
            subscribedServerList = query.fromPin("Subscribed").find();
        } catch (ParseException ignored) {
        }

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        getPubRowNumber(pubServerList);
        getSubscribedRowNumber(subscribedServerList);

        generateOpeningHours(pubServerList);
        generateName(pubServerList);
        generateOffers(subscribedServerList);
        generateButtonActions(pubServerList, subscribedServerList);
        generateDescription(subscribedServerList);
    }

    private void generateButtonActions(final List<ParseObject> pubServerList, final List<ParseObject> subscribedServerList) {

        ImageButton facebookButton = (ImageButton) findViewById(R.id.btnFacebook);
        ImageButton navigateButton = (ImageButton) findViewById(R.id.btnNavigate);

        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = "http://maps.google.com/maps?saddr=" + location.getLatitude() + "," + location.getLongitude() + "&daddr="
                        + pubServerList.get(pubRowNumber).getNumber("Latitude") + ","
                        + pubServerList.get(pubRowNumber).getNumber("Latitude");
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(i);
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = subscribedServerList.get(subscribedRowNumber).getString("FacebookLink");
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(i);
            }
        });
    }

    private void generateName(List<ParseObject> pubServerList) {

        TextView pubName = (TextView) findViewById(R.id.pub_name);
        pubName.setText(pubServerList.get(pubRowNumber).getString("Name"));
    }

    private void generateOffers(List<ParseObject> subscribedServerList) {

        TextView offers = (TextView) findViewById(R.id.offers);
        StringBuilder sb = new StringBuilder();
        sb.append("Akciók: ");
        for(int i = 0; i < subscribedServerList.get(subscribedRowNumber).getJSONArray("Offers").length(); i++){
            sb.append('\n');
            try {
                sb.append(subscribedServerList.get(subscribedRowNumber).getJSONArray("Offers").get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        offers.setText(sb.toString());
        offers.setTextSize(21);
        offers.setTextColor(getResources().getColor(R.color.Wheat));
    }

    private void generateDescription(List<ParseObject> subscribedServerList) {

        TextView description = (TextView) findViewById(R.id.description);
        description.setText(subscribedServerList.get(subscribedRowNumber).getString("Description"));
    }

    private void generateOpeningHours(List<ParseObject> pubServerList) {

        TextView openingHours = (TextView) findViewById(R.id.openClose);
        StringBuilder sb = new StringBuilder();

        try {
            sb.append("Nyitvatartás:");
            sb.append('\n');
            sb.append("Hétfő: ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Monday").get(0)));
            sb.append(".00");
            sb.append(" - ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Monday").get(1)));
            sb.append(".00");
            sb.append('\n');
            sb.append("Kedd: ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Tuesday").get(0)));
            sb.append(".00");
            sb.append(" - ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Tuesday").get(1)));
            sb.append(".00");
            sb.append('\n');
            sb.append("Szerda: ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Wednesday").get(0)));
            sb.append(".00");
            sb.append(" - ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Wednesday").get(1)));
            sb.append(".00");
            sb.append('\n');
            sb.append("Csütörtök: ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Thursday").get(0)));
            sb.append(".00");
            sb.append(" - ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Thursday").get(1)));
            sb.append(".00");
            sb.append('\n');
            sb.append("Péntek: ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Friday").get(0)));
            sb.append(".00");
            sb.append(" - ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Friday").get(1)));
            sb.append(".00");
            sb.append('\n');
            sb.append("Szombat: ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Saturday").get(0)));
            sb.append(".00");
            sb.append(" - ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Saturday").get(1)));
            sb.append(".00");
            sb.append('\n');
            sb.append("Vasárnap: ");
            if ((pubServerList.get(pubRowNumber).getJSONArray("Sunday").get(0)).equals(pubServerList.get(pubRowNumber).getJSONArray("Sunday").get(1)))
                sb.append("Zárva");
            else {
                sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Sunday").get(0)));
                sb.append(".00");
                sb.append(" - ");
                sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Sunday").get(1)));
                sb.append(".00");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        openingHours.setText(sb.toString());
    }

    private void getPubRowNumber(List<ParseObject> pubServerList) {
        for (ParseObject object : pubServerList) {
            if (object.getObjectId().equals(identifier)) {
                break;
            } else {
                pubRowNumber++;
            }
        }
    }

    private void getSubscribedRowNumber(List<ParseObject> subscribedServerList) {
        for (ParseObject object : subscribedServerList) {
            if (object.getString("Identifier").equals(identifier))
                break;
            else {
                subscribedRowNumber++;
            }
        }
    }
}
