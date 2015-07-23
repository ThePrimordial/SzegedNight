package fagagy.szeged.hu.szegednight.atmRescources;

import android.content.Context;
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
 * Created by TheSorrow on 15/07/23.
 */
public class AtmAdapter extends BaseAdapter {

    final List<Atm> atmList;

    public AtmAdapter(final Context context, final ArrayList<Atm> atmList){
        this.atmList = atmList;
    }

    public void addAtm(Atm atm){
        atmList.add(atm);
    }

    @Override
    public int getCount() {
        return atmList.size();
    }

    @Override
    public Object getItem(int position) {
        return atmList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Atm atm = atmList.get(position);
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View pubView = inflater.inflate(R.layout.atmfragmentrow, null);

        TextView atmListNameText = (TextView) pubView.findViewById(R.id.AtmName);
        TextView atmistDistanceText = (TextView) pubView.findViewById(R.id.AtmDistance);
        DecimalFormat numberFormat = new DecimalFormat("#.00");
        /**
         * TODO Kép megoldása
         */
        if(atm.getDistance() > 1){
            atmistDistanceText.setText(numberFormat.format(atm.getDistance()) + " km");
        }else{
            double dist = atm.getDistance()*1000;
            int intDistance = (int) dist;
            atmistDistanceText.setText(intDistance + " m");
        }
        atmListNameText.setText(atm.getName());

        return pubView;
    }
}

