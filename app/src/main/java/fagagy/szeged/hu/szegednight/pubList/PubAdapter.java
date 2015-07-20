package fagagy.szeged.hu.szegednight.pubList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fagagy.szeged.hu.szegednight.R;

/**
 * Created by Ádám on 15/07/19.
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
        View pubView = inflater.inflate(R.layout.fragmentrow, null);

        TextView pubListOpenText = (TextView) pubView.findViewById(R.id.PubOpen);
        TextView pubListDistanceText = (TextView) pubView.findViewById(R.id.PubDistance);
        TextView pubListNameText = (TextView) pubView.findViewById(R.id.PubName);

        if (pub.isOpen()){
            pubListOpenText.setText("Nyitva!");
            pubListOpenText.setTextColor(Color.GREEN);
        }else if(!pub.isOpen()) {
            pubListOpenText.setText("Zarva! :( ");
            pubListOpenText.setTextColor(Color.RED);
        }
        pubListDistanceText.setText(pub.getDistance() + " km");
        pubListNameText.setText(pub.getName());


        return pubView;
    }
}
