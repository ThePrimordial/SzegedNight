package fagagy.szeged.hu.szegednight.partyResources;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fagagy.szeged.hu.szegednight.R;

/**
 * Created by TheSorrow on 15/07/28.
 */
public class PartyAdapter extends BaseAdapter {

    final List<Party> partyList;
    private final Context context;

    public PartyAdapter(final Context context, final ArrayList<Party> partyList) {
        this.partyList = partyList;
        this.context = context;
    }

    public void addParty(Party party) {
        partyList.add(party);
    }

    @Override
    public int getCount() {
        return partyList.size();
    }

    @Override
    public Object getItem(int position) {
        return partyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Party party = partyList.get(position);
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View partyView = inflater.inflate(R.layout.partyfragmentrow, null);

        TextView partyPlaceText = (TextView) partyView.findViewById(R.id.PartyPlace);
        TextView partyEventText = (TextView) partyView.findViewById(R.id.PartyEvent);
        TextView partyDateText = (TextView) partyView.findViewById(R.id.PartyDate);
        TextView partyDistaceText = (TextView) partyView.findViewById(R.id.PartyDistance);
        Date now = new Date(System.currentTimeMillis());
        DecimalFormat numberFormat = new DecimalFormat("#.00");

        //Dokk, stb esemény
        if (party.getDate() != null) {
            DateFormat df2 = new SimpleDateFormat("MM-dd HH:mm");
            String formattedDate = df2.format(party.getDate());
            partyDateText.setText(formattedDate);

            if(party.getDistance() == 0){
                partyDistaceText.setText("ismeretlen");
            }

            if (party.getDistance() > 1) {
                partyDistaceText.setText(numberFormat.format(party.getDistance()) + " km");
            } else if (party.getDistance() == 0 && party.getDate() == null) {
                partyDistaceText.setText(null);
            } else {
                double dist = party.getDistance() * 1000;
                int intDistance = (int) dist;
                partyDistaceText.setText(intDistance + " m");
            }
            partyPlaceText.setText(party.getPlace());
            partyEventText.setText(party.getEvent());

            if (position % 2 == 0) {
                partyView.setBackgroundResource(R.drawable.border_ui1);
            } else {
                partyView.setBackgroundResource(R.drawable.border_ui2);
            }

            if(now.after(party.getDate())){
                partyDateText.setTextColor(Color.RED);
            }else{
                partyDateText.setTextColor(Color.GREEN);
            }
            return partyView;

            //Koncert esemény
        } else {
            String eventTime = party.getFrom() + " - " + party.getTo();
            partyDateText.setText(eventTime);
            partyDistaceText.setText("Augusztus " + party.getDay()+ ".");

            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int currday = calendar.get(Calendar.DAY_OF_MONTH);
            int currHour = calendar.get(Calendar.HOUR_OF_DAY);

            if (currday == party.getDay() && currHour < Double.parseDouble(party.getFrom())) {
                partyDateText.setTextColor(Color.GREEN);
                partyPlaceText.setTextColor(Color.GREEN);
            } else if (currday == party.getDay() && currHour > Double.parseDouble(party.getFrom())) {
                partyDateText.setTextColor(Color.RED);
                partyPlaceText.setTextColor(Color.RED);
            }

            if(party.getPlace().length() > 18) {
                String s = party.getPlace();
                partyPlaceText.setTextSize(18);
            }

            partyPlaceText.setText(party.getPlace());
            partyEventText.setText(party.getEvent());
            eventColorSetter(party.getEvent(), partyEventText);

            if (position % 2 == 0) {
                partyView.setBackgroundResource(R.drawable.border_ui1);
            } else {
                partyView.setBackgroundResource(R.drawable.border_ui2);
            }

            return partyView;
        }
    }

    private void eventColorSetter(String event, TextView eventView) {

        switch (event){
            case "SZIN Nagyszínpad": eventView.setTextColor(context.getResources().getColor(R.color.fuchsia));break;
            case "Live Arena": eventView.setTextColor(context.getResources().getColor(R.color.Tomato));break;
            case "Mizo Színpad": eventView.setTextColor(context.getResources().getColor(R.color.Turquoise));break;
            case "WakeUpSzeged PartyAréna": eventView.setTextColor(context.getResources().getColor(R.color.LightSteelBlue));break;
            default:break;
        }
    }
}
