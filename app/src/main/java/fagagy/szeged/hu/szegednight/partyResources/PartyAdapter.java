package fagagy.szeged.hu.szegednight.partyResources;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
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
    private final Context context;

    public PartyAdapter(final Context context, final ArrayList<Party> partyList) {
        this.partyList = partyList;
        this.context = context;
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
        CardView cView = (CardView) inflater.inflate(R.layout.fragmentrow, parent, false);

        TextView partyPlaceDateText = (TextView) cView.findViewById(R.id.tw_Name);
        TextView partyEventText = (TextView) cView.findViewById(R.id.tw_Open);
        TextView partyDistanceText = (TextView) cView.findViewById(R.id.tw_Distance);
        Date now = new Date(System.currentTimeMillis());
        DecimalFormat numberFormat = new DecimalFormat("#.00");

        DateFormat df2 = new SimpleDateFormat("MM-dd HH:mm");
        String formattedDate = df2.format(party.getDate());

        if (party.getDistance() == 0) {
            partyDistanceText.setText(R.string.Unknown);
        }

        if (party.getDistance() > 1) {
            partyDistanceText.setText(numberFormat.format(party.getDistance()) + " km");
        } else if (party.getDistance() == 0 && party.getDate() == null) {
            partyDistanceText.setText(null);
        } else {
            double dist = party.getDistance() * 1000;
            int intDistance = (int) dist;
            partyDistanceText.setText(intDistance + " m");
        }
        partyPlaceDateText.setText(party.getPlace()+"  "+formattedDate);
        partyEventText.setText(party.getEvent());

        if (now.after(party.getDate())) {
            partyPlaceDateText.setTextColor(Color.parseColor("#f44336"));
        } else {
            partyPlaceDateText.setTextColor(Color.parseColor("#43a047"));
        }
        return cView;

    }
}


