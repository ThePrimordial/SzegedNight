package fagagy.szeged.hu.szegednight.pages;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.ParseObject;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.List;

import fagagy.szeged.hu.szegednight.R;

public class SubscribedViewGenerator {

    public void generateButtonActions(final List<ParseObject> pubServerList, final List<ParseObject> subscribedServerList,
                                      final ImageButton facebookButton, final ImageButton navigateButton,
                                      final int pubRowNumber, final int subscribedRowNumber, final Location location) {

        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(location != null) {
                    String link = "http://maps.google.com/maps?saddr=" + location.getLatitude() + "," + location.getLongitude() + "&daddr="
                            + pubServerList.get(pubRowNumber).getNumber("Latitude") + ","
                            + pubServerList.get(pubRowNumber).getNumber("Longitude");
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    v.getContext().startActivity(i);
                }else{
                    Toast.makeText(v.getContext(), R.string.NoGPSpos, Toast.LENGTH_SHORT).show();
                }
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookUrl = "www.facebook.com/" + subscribedServerList.get(subscribedRowNumber).getString("FacebookLink");
                String facebookID = subscribedServerList.get(subscribedRowNumber).getString("FacebookLink");
                try {
                    int versionCode = v.getContext().getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;

                    if(!facebookID.isEmpty()) {
                        // open the Facebook app using facebookID (fb://profile/facebookID or fb://page/facebookID)
                        Log.d("facebook", "id: " + facebookID + " url : " + facebookUrl);
                        Uri uri = Uri.parse("fb://profile/" + facebookID);
                        v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    } else if (versionCode >= 3002850 && !facebookUrl.isEmpty()) {
                        // open Facebook app using facebook url
                        Uri uri = Uri.parse("fb://facewebmodal/f?href=" + facebookUrl);
                        v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    } else {
                        // Facebook is not installed. Open the browser
                        Uri uri = Uri.parse(facebookUrl);
                        v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    // Facebook is not installed. Open the browser
                    Uri uri = Uri.parse(facebookUrl);
                    v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
            }
        });
    }


    public StringBuilder generateOffers(List<ParseObject> subscribedServerList, final int subscribedRowNumber) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < subscribedServerList.get(subscribedRowNumber).getJSONArray("Offers").length(); i++) {
            try {
                sb.append(subscribedServerList.get(subscribedRowNumber).getJSONArray("Offers").get(i).toString());
                sb.append('\n');
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return sb;
    }

    public String generateDescription(List<ParseObject> subscribedServerList, final int subscribedRowNumber) {

        return subscribedServerList.get(subscribedRowNumber).getString("Description");
    }

    public String generateDistance(List<ParseObject> pubServerList, final int pubRowNumber, final Location myloc){
        double distance = 0;
        Location pubLocation = new Location("");
        double longitude = pubServerList.get(pubRowNumber).getDouble("longitude");
        double latitude = pubServerList.get(pubRowNumber).getDouble("latitude");
        pubLocation.setLatitude(latitude);
        pubLocation.setLongitude(longitude);

        distance = myloc.distanceTo(pubLocation);
        String dist = "";
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        if(distance >= 1){
            dist = numberFormat.format(distance) + " km";
        }else
            dist = distance * 1000 + " m";

        return dist ;
    }

    public StringBuilder generateOpeningHours(List<ParseObject> pubServerList, final int pubRowNumber) {

        StringBuilder sb = new StringBuilder();

        try {
            sb.append(R.string.OpeningHours);
            sb.append('\n');
            sb.append(R.string.Monday + ": ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Monday").get(0)));
            sb.append(".00");
            sb.append(" - ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Monday").get(1)));
            sb.append(".00");
            sb.append('\n');
            sb.append(R.string.Tuesday + ": ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Tuesday").get(0)));
            sb.append(".00");
            sb.append(" - ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Tuesday").get(1)));
            sb.append(".00");
            sb.append('\n');
            sb.append(R.string.Wednesday + ": ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Wednesday").get(0)));
            sb.append(".00");
            sb.append(" - ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Wednesday").get(1)));
            sb.append(".00");
            sb.append('\n');
            sb.append(R.string.Thursday + ": ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Thursday").get(0)));
            sb.append(".00");
            sb.append(" - ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Thursday").get(1)));
            sb.append(".00");
            sb.append('\n');
            sb.append(R.string.Friday + ": ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Friday").get(0)));
            sb.append(".00");
            sb.append(" - ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Friday").get(1)));
            sb.append(".00");
            sb.append('\n');
            sb.append(R.string.Saturday + ": ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Saturday").get(0)));
            sb.append(".00");
            sb.append(" - ");
            sb.append(String.valueOf(pubServerList.get(pubRowNumber).getJSONArray("Saturday").get(1)));
            sb.append(".00");
            sb.append('\n');
            sb.append(R.string.Sunday + ": ");
            if ((pubServerList.get(pubRowNumber).getJSONArray("Sunday").get(0)).equals(pubServerList.get(pubRowNumber).getJSONArray("Sunday").get(1)))
                sb.append(R.string.Closed);
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
            if (object.getString("Name").equals(identifier)) {
                return pubRowNumber;
            } else {
                pubRowNumber++;
            }
        }
        return 0;
    }

    public int getSubscribedRowNumber(List<ParseObject> subscribedServerList, final String identifier) {
        int subscribedRowNumber = 0;
        for (ParseObject object : subscribedServerList) {
            if (object.getString("Name").equals(identifier))
                return subscribedRowNumber;
            else {
                subscribedRowNumber++;
            }
        }
        return subscribedRowNumber;
    }

}
