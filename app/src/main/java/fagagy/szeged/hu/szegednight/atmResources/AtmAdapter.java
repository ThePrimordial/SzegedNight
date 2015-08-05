package fagagy.szeged.hu.szegednight.atmResources;

import android.content.Context;
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
        View atmView = inflater.inflate(R.layout.atmfragmentrow, null);

        TextView atmListNameText = (TextView) atmView.findViewById(R.id.AtmName);
        TextView atmistDistanceText = (TextView) atmView.findViewById(R.id.AtmDistance);
        DecimalFormat numberFormat = new DecimalFormat("#.00");

        ImageView logo = (ImageView) atmView.findViewById(R.id.typeImage);
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
            atmistDistanceText.setText("ismeretlen");
        }else if (atm.getDistance() > 1) {
            atmistDistanceText.setText(numberFormat.format(atm.getDistance()) + " km");
        } else {
            double dist = atm.getDistance() * 1000;
            int intDistance = (int) dist;
            atmistDistanceText.setText(intDistance + " m");
        }
        atmListNameText.setText(atm.getName());

        if(position % 2 == 0){
            atmView.setBackgroundResource(R.drawable.border_ui1);
        }else {
            atmView.setBackgroundResource(R.drawable.border_ui2);
        }

        return atmView;
    }
}
