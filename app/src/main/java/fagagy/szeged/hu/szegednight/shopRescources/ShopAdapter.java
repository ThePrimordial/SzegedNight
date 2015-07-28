package fagagy.szeged.hu.szegednight.shopRescources;

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
 * Created by TheSorrow on 15/07/28.
 */
public class ShopAdapter extends BaseAdapter {
    final List<Shop> shopList;

    public ShopAdapter(final Context context, final ArrayList<Shop> shopList){
        this.shopList = shopList;
    }

    public void addShop(Shop shop){
        shopList.add(shop);
    }

    @Override
    public int getCount() {
        return shopList.size();
    }

    @Override
    public Object getItem(int position) {
        return shopList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final Shop shop = shopList.get(position);
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View shopView = inflater.inflate(R.layout.shopfragmentrow, null);

        TextView shopListOpenText = (TextView) shopView.findViewById(R.id.ShopOpen);
        TextView shopListDistanceText = (TextView) shopView.findViewById(R.id.ShopDistance);
        TextView shopListNameText = (TextView) shopView.findViewById(R.id.ShopName);
        DecimalFormat numberFormat = new DecimalFormat("#.00");

        if (shop.isOpen()){
            shopListOpenText.setText("Nyitva! " + shop.getOpenUntil() + ".00-ig");
            shopListOpenText.setTextColor(Color.GREEN);
        }else if(!shop.isOpen()) {
            shopListOpenText.setText("Zarva! :( ");
            shopListOpenText.setTextColor(Color.RED);
        }
        if(shop.getDistance() > 1){
            shopListDistanceText.setText(numberFormat.format(shop.getDistance()) + " km");
        }else{
            double dist = shop.getDistance()*1000;
            int intDistance = (int) dist;
            shopListDistanceText.setText(intDistance + " m");
        }
        shopListNameText.setText(shop.getName());

        return shopView;
    }


}