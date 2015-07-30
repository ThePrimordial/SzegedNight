package fagagy.szeged.hu.szegednight.partyResources;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fagagy.szeged.hu.szegednight.R;

/**
 * Created by TheSorrow on 15/07/28.
 */
public class PartyAdapter extends BaseAdapter {

    final List<Party> partyList;

    public PartyAdapter(final Context context, final ArrayList<Party> partyList){
        this.partyList = partyList;
    }

    public void addParty(Party party){
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
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        Date now = new Date(System.currentTimeMillis());
        DateFormat df2 = new SimpleDateFormat("MM-dd HH:mm");
        String formattedDate = df2.format(party.getDate());

        if (party.getDistance() > 1) {
            partyDistaceText.setText(numberFormat.format(party.getDistance()) + " km");
        } else {
            double dist = party.getDistance() * 1000;
            int intDistance = (int) dist;
            partyDistaceText.setText(intDistance + " m");
        }
        partyPlaceText.setText(party.getPlace());
        partyEventText.setText(party.getEvent());
        partyDateText.setText(formattedDate);
        if (now.after(party.getDate())) {
            partyDateText.setTextColor(Color.RED);
        } else
            partyDateText.setTextColor(Color.GREEN);

        if(position % 2 == 0){
            partyView.setBackground(ContextCompat.getDrawable(partyView.getContext(), R.drawable.border_ui1));
        }else {
            partyView.setBackground(ContextCompat.getDrawable(partyView.getContext(), R.drawable.border_ui2));
        }

        return partyView;
    }
}
