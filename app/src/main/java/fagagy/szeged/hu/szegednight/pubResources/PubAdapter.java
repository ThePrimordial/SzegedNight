package fagagy.szeged.hu.szegednight.pubResources;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import fagagy.szeged.hu.szegednight.R;

/**
 * Created by �d�m on 15/07/19.
 */
public class PubAdapter extends BaseAdapter {

    final List<Pub> pubList;

    public PubAdapter(final Context context, final ArrayList<Pub> pubList) {
        this.pubList = pubList;
    }

    public void addPub(Pub pub) {
        pubList.add(pub);
    }

    @Override
    public int getCount() {
        return pubList.size();
    }

    @Override
    public Object getItem(int position) {
        return pubList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final Pub pub = pubList.get(position);
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View pubView = inflater.inflate(R.layout.fragmentrow, null);

        TextView pubListOpenText = (TextView) pubView.findViewById(R.id.tw_Open);
        TextView pubListDistanceText = (TextView) pubView.findViewById(R.id.tw_Distance);
        TextView pubListNameText = (TextView) pubView.findViewById(R.id.tw_Name);
        DecimalFormat numberFormat = new DecimalFormat("#.00");

        if (pub.isOpen()) {
            pubListOpenText.setTextColor(Color.parseColor("#43a047"));
            if ((pub.getOpenUntil()).equals("0")) {
                pubListOpenText.setText(R.string.OpenMidnight);
            } else {
                StringBuilder sb = new StringBuilder();
                sb
                        .append(pubView.getContext().getResources().getString(R.string.Open))
                        .append(" ")
                        .append(pubView.getContext().getResources().getString(R.string.Closing))
                        .append(" ")
                        .append(pub.getOpenUntil()).append(".00");
                pubListOpenText.setText(sb);
            }
        } else {
            pubListOpenText.setText(R.string.Closed);
            pubListOpenText.setTextColor(Color.parseColor("#f44336"));
        }

        if (pub.getDistance() == 0) {
            pubListDistanceText.setText(R.string.Unknown);
        } else if (pub.getDistance() > 1) {
            pubListDistanceText.setText(numberFormat.format(pub.getDistance()) + " km");
        } else {
            double dist = pub.getDistance() * 1000;
            int intDistance = (int) dist;
            pubListDistanceText.setText(intDistance + " m");
        }
        pubListNameText.setText(pub.getName());
        return pubView;
    }
}
