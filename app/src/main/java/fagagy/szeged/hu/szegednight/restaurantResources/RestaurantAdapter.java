package fagagy.szeged.hu.szegednight.restaurantResources;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
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
 * Created by TheSorrow on 15/07/20.
 */
public class RestaurantAdapter extends BaseAdapter {

    final List<Restaurant> restaurantsList;

    public RestaurantAdapter(final Context context, final ArrayList<Restaurant> restaurantsList) {
        this.restaurantsList = restaurantsList;
    }

    public void addRestaurant(Restaurant restaurant) {
        restaurantsList.add(restaurant);
    }

    @Override
    public int getCount() {
        return restaurantsList.size();
    }

    @Override
    public Object getItem(int position) {
        return restaurantsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Restaurant restaurant = restaurantsList.get(position);
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CardView cView = (CardView) inflater.inflate(R.layout.pubfragmentrow, parent, false);

        TextView resListOpenText = (TextView) cView.findViewById(R.id.tw_Open);
        TextView resListDistanceText = (TextView) cView.findViewById(R.id.tw_Distance);
        TextView resListNameText = (TextView) cView.findViewById(R.id.tw_Name);
        DecimalFormat numberFormat = new DecimalFormat("#.00");

        if (restaurant.isOpen()) {
            resListOpenText.setTextColor(Color.parseColor("#43a047"));
            if ((restaurant.getOpenUntil()).equals("0")) {
                resListOpenText.setText(R.string.OpenMidnight);
            } else {
                StringBuilder sb = new StringBuilder();
                sb
                        .append(cView.getContext().getResources().getString(R.string.Open))
                        .append(" ")
                        .append(cView.getContext().getResources().getString(R.string.Closing))
                        .append(" ")
                        .append(restaurant.getOpenUntil()).append(".00");
                resListOpenText.setText(sb);
            }
        } else {
            resListOpenText.setText(R.string.Closed);
            resListOpenText.setTextColor(Color.parseColor("#f44336"));
        }

        if (restaurant.getDistance() == 0) {
            resListDistanceText.setText(R.string.Unknown);
        } else if (restaurant.getDistance() > 1) {
            resListDistanceText.setText(numberFormat.format(restaurant.getDistance()) + " km");
        } else {
            double dist = restaurant.getDistance() * 1000;
            int intDistance = (int) dist;
            resListDistanceText.setText(intDistance + " m");
        }
        resListNameText.setText(restaurant.getName());

        return cView;
    }

}
