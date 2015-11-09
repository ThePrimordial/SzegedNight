package fagagy.szeged.hu.szegednight.atmResources;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

    public AtmAdapter(final Context context, final ArrayList<Atm> atmList) {
        this.atmList = atmList;
    }

    public void addAtm(Atm atm) {
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
        CardView cView = (CardView) inflater.inflate(R.layout.pubfragmentrow, parent, false);

        TextView atmName = (TextView) cView.findViewById(R.id.tw_Name);
        TextView atmDistance = (TextView) cView.findViewById(R.id.tw_Distance);
        TextView atmOpen = (TextView) cView.findViewById(R.id.tw_Open);
        DecimalFormat numberFormat = new DecimalFormat("#.00");

        ImageView logo = (ImageView) cView.findViewById(R.id.iw_side_picture);
        switch (atm.getType()) {
            case "bb":logo.setImageResource(R.drawable.bb_logo_w);
                break;
            case "erste":logo.setImageResource(R.drawable.erste_logo_w);
                break;
            case "cib":logo.setImageResource(R.drawable.cib_logo);
                break;
            case "kh":logo.setImageResource(R.drawable.kah_logo_w);
                break;
            case "rf":logo.setImageResource(R.drawable.raiffeisen_logo_w);
                break;
            case "mkb":logo.setImageResource(R.drawable.mkb_logo_w);
                break;
            case "otp":logo.setImageResource(R.drawable.otp_logo_w);
                break;
            default:
                break;
        }
        if(atm.getDistance() == 0){
            atmDistance.setText(R.string.Unknown);
        }else if (atm.getDistance() > 1) {
            atmDistance.setText(numberFormat.format(atm.getDistance()) + " km");
        } else {
            double dist = atm.getDistance() * 1000;
            int intDistance = (int) dist;
            atmDistance.setText(intDistance + " m");
        }
        String name = atm.getName();
        int iend= name.indexOf(" ");
        String subName = name.substring(0, iend);
        atmName.setText(subName);

        subName = name.substring(iend+3, name.length());
        atmOpen.setText(subName);

        return cView;
    }
}
