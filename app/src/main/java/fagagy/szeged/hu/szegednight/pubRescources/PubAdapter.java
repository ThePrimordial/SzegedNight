package fagagy.szeged.hu.szegednight.pubRescources;

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

    final List <Pub> pubList;

    public PubAdapter(final Context context, final ArrayList<Pub> pubList){
        this.pubList = pubList;
    }

    public void addPub(Pub pub){
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
        View pubView = inflater.inflate(R.layout.pubfragmentrow, null);

        TextView pubListOpenText = (TextView) pubView.findViewById(R.id.PubOpen);
        TextView pubListDistanceText = (TextView) pubView.findViewById(R.id.PubDistance);
        TextView pubListNameText = (TextView) pubView.findViewById(R.id.PubName);
        DecimalFormat numberFormat = new DecimalFormat("#.00");

        if (pub.isOpen()){
            pubListOpenText.setText("Nyitva! " + pub.getOpenUntil() + ".00-ig");
            pubListOpenText.setTextColor(Color.GREEN);
        }else if(!pub.isOpen()) {
            pubListOpenText.setText("Zarva! :( ");
            pubListOpenText.setTextColor(Color.RED);
        }
        if(pub.getDistance() > 1){
            pubListDistanceText.setText(numberFormat.format(pub.getDistance()) + " km");
        }else{
            double dist = pub.getDistance()*1000;
            int intDistance = (int) dist;
            pubListDistanceText.setText(intDistance + " m");
        }
        pubListNameText.setText(pub.getName());


        return pubView;
    }
}
