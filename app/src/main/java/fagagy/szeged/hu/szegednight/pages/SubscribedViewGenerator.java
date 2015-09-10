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

public class SubscribedViewGenerator{


    private Location location;

    public void generateButtonActions(final List<ParseObject> pubServerList, final List<ParseObject> subscribedServerList,
                                      final ImageButton facebookButton, final ImageButton navigateButton,
                                      final int pubRowNumber, final int subscribedRowNumber) {

        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = "http://maps.google.com/maps?saddr=" + location.getLatitude() + "," + location.getLongitude() + "&daddr="
                        + pubServerList.get(pubRowNumber).getNumber("Latitude") + ","
                        + pubServerList.get(pubRowNumber).getNumber("Latitude");
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = subscribedServerList.get(subscribedRowNumber).getString("FacebookLink");
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            }
        });
    }

    public String generateName(List<ParseObject> pubServerList, final int pubRowNumber) {

        return pubServerList.get(pubRowNumber).getString("Name");
    }

    public StringBuilder generateOffers(List<ParseObject> subscribedServerList, final int subscribedRowNumber) {
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

        return sb;
    }

    public String generateDescription(List<ParseObject> subscribedServerList, final int subscribedRowNumber) {

        return subscribedServerList.get(subscribedRowNumber).getString("Description");
    }

    public StringBuilder generateOpeningHours(List<ParseObject> pubServerList, final int pubRowNumber) {

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
        return sb;
    }

    public int getPubRowNumber(List<ParseObject> pubServerList, final String identifier) {
        int pubRowNumber = 0;
        for (ParseObject object : pubServerList) {
            if (object.getObjectId().equals(identifier)) {
                break;
            } else {
                pubRowNumber++;
            }
        }

        return pubRowNumber;
    }

    public int getSubscribedRowNumber(List<ParseObject> subscribedServerList, final String identifier) {
        int subscribedRowNumber = 0;
        for (ParseObject object : subscribedServerList) {
            if (object.getString("Identifier").equals(identifier))
                break;
            else {
                subscribedRowNumber++;
            }
        }
        return subscribedRowNumber;
    }
}
