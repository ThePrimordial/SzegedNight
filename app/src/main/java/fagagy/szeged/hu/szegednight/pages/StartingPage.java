package fagagy.szeged.hu.szegednight.pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pubRescources.PubBrowser;
import fagagy.szeged.hu.szegednight.restaurantRescources.RestaurantBrowser;


public class StartingPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page);
    }


    public void onClick(View v){
        int id = v.getId();
        Intent i = new Intent();
        switch(id){
            case R.id.btnPubs:
                i.setClass(this, PubBrowser.class);
                startActivity(i);
                overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_in_top);
                break;
            case R.id.btnRestaurants:
                i.setClass(this, RestaurantBrowser.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

}
