package fagagy.szeged.hu.szegednight.tobaccoResources;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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
 * Created by TheSorrow on 15/07/27.
 */
public class TobaccoAdapter extends BaseAdapter {

    final List<Tobacco> tobaccoList;

    public TobaccoAdapter(final Context context, final ArrayList<Tobacco> tobaccoList){
        this.tobaccoList = tobaccoList;
    }

    public void addTobacco(Tobacco tobacco){
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
        View tobaccoView = inflater.inflate(R.layout.tobaccofragmentrow, null);

        TextView tobaccoListOpenText = (TextView) tobaccoView.findViewById(R.id.TobaccoOpen);
        TextView tobaccoListDistanceText = (TextView) tobaccoView.findViewById(R.id.TobaccoDistance);
        TextView tobaccoListNameText = (TextView) tobaccoView.findViewById(R.id.TobaccoName);
        DecimalFormat numberFormat = new DecimalFormat("#.00");

        if (tobacco.isOpen()){
            tobaccoListOpenText.setText("Nyitva! " + tobacco.getOpenUntil() + ".00-ig");
            tobaccoListOpenText.setTextColor(Color.GREEN);
        }else if(!tobacco.isOpen()) {
            tobaccoListOpenText.setText("Zarva! :( ");
            tobaccoListOpenText.setTextColor(Color.RED);
        }
        if(tobacco.getDistance() > 1){
            tobaccoListDistanceText.setText(numberFormat.format(tobacco.getDistance()) + " km");
        }else{
            double dist = tobacco.getDistance()*1000;
            int intDistance = (int) dist;
            tobaccoListDistanceText.setText(intDistance + " m");
        }
        tobaccoListNameText.setText(tobacco.getName());
        if(position % 2 == 0){
            tobaccoView.setBackground(ContextCompat.getDrawable(tobaccoView.getContext(), R.drawable.border_ui1));
        }else {
            tobaccoView.setBackground(ContextCompat.getDrawable(tobaccoView.getContext(), R.drawable.border_ui2));
        }

        return tobaccoView;
    }

}
