package fagagy.szeged.hu.szegednight.restaurantRescources;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fagagy.szeged.hu.szegednight.R;

public class RestaurantFragmentList extends ListFragment implements OnItemClickListener {

    private RestaurantAdapter adapter;
    ArrayList<Restaurant> resList = new ArrayList<Restaurant>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = View.inflate(getActivity(), R.layout.restaurantfragmentrow, null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        resList.add(new Restaurant("Subway", true, 3.2));
        resList.add(new Restaurant("Inferno", false, 0.4));
        resList.add(new Restaurant("Budi", true, 32));

        Collections.sort(resList, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant r1, Restaurant r2) {
                return Double.compare(r1.getDistance(), r2.getDistance());
            }
        });

        RestaurantAdapter adapter = new RestaurantAdapter(getActivity(), resList);
        setListAdapter(adapter);
        registerForContextMenu(getListView());
        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getActivity(), "Működik", Toast.LENGTH_SHORT)
                .show();
    }
}
