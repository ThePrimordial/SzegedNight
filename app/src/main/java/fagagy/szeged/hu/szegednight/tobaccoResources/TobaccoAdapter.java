package fagagy.szeged.hu.szegednight.tobaccoResources;

import android.content.Context;
import android.graphics.Color;
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
 * Created by TheSorrow on 15/07/27.
 */
public class TobaccoAdapter extends BaseAdapter {

    final List<Tobacco> tobaccoList;

    public TobaccoAdapter(final Context context, final ArrayList<Tobacco> tobaccoList) {
        this.tobaccoList = tobaccoList;
    }

    public void addTobacco(Tobacco tobacco) {
        tobaccoList.add(tobacco);
    }

    @Override
    public int getCount() {
        return tobaccoList.size();
    }

    @Override
    public Object getItem(int position) {
        return tobaccoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final Tobacco tobacco = tobaccoList.get(position);
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CardView cView = (CardView) inflater.inflate(R.layout.pubfragmentrow, parent, false);

        TextView tobaccoListOpenText = (TextView) cView.findViewById(R.id.tw_Open);
        TextView tobaccoListDistanceText = (TextView) cView.findViewById(R.id.tw_Distance);
        TextView tobaccoListNameText = (TextView) cView.findViewById(R.id.tw_Name);
        ImageView tobaccoIcon = (ImageView) cView.findViewById(R.id.iw_side_picture);
        DecimalFormat numberFormat = new DecimalFormat("#.00");

        if (tobacco.isOpen()) {
            tobaccoListOpenText.setTextColor(Color.parseColor("#43a047"));
            if ((tobacco.getOpenUntil()).equals("0")) {
                tobaccoListOpenText.setText(R.string.OpenMidnight);
            } else {
                StringBuilder sb = new StringBuilder();
                sb
                        .append(cView.getContext().getResources().getString(R.string.Open))
                        .append(" ")
                        .append(cView.getContext().getResources().getString(R.string.Closing))
                        .append(" ")
                        .append(tobacco.getOpenUntil()).append(".00");
                tobaccoListOpenText.setText(sb);
            }
        } else {
            tobaccoListOpenText.setText(R.string.Closed);
            tobaccoListOpenText.setTextColor(Color.parseColor("#f44336"));
        }

        if (tobacco.getDistance() == 0) {
            tobaccoListDistanceText.setText(R.string.Unknown);
        } else if (tobacco.getDistance() > 1) {
            tobaccoListDistanceText.setText(numberFormat.format(tobacco.getDistance()) + " km");
        } else {
            double dist = tobacco.getDistance() * 1000;
            int intDistance = (int) dist;
            tobaccoListDistanceText.setText(intDistance + " m");
        }
        tobaccoListNameText.setText(tobacco.getName());
        tobaccoIcon.setImageResource(R.drawable.tobacco_icon_w);


        return cView;
    }

}
