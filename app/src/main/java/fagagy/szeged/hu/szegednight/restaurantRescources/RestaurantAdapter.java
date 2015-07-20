package fagagy.szeged.hu.szegednight.restaurantRescources;

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
 * Created by TheSorrow on 15/07/20.
 */
public class RestaurantAdapter extends BaseAdapter {

    final List<Restaurant> restaurantsList;

    public RestaurantAdapter(final Context context, final ArrayList<Restaurant> restaurantsList){
        this.restaurantsList = restaurantsList;
    }

    public void addRestaurant(Restaurant restaurant){
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
        View resView = inflater.inflate(R.layout.restaurantfragmentrow, null);

        TextView resListOpenText = (TextView) resView.findViewById(R.id.RestaurantName);
        TextView resListDistanceText = (TextView) resView.findViewById(R.id.RestaurantDistance);
        TextView resListNameText = (TextView) resView.findViewById(R.id.RestaurantOpen);

        if (restaurant.isOpen()){
            resListOpenText.setText("Nyitva!");
            resListOpenText.setTextColor(Color.YELLOW);
        }else if(!restaurant.isOpen()) {
            resListOpenText.setText("Zarva! :( ");
            resListOpenText.setTextColor(Color.CYAN);
        }
        resListDistanceText.setText(restaurant.getDistance() + " km");
        resListNameText.setText(restaurant.getName());


        return resView;
    }

}
