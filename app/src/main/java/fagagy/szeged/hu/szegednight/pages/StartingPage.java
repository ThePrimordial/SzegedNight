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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_starting_page, menu);
        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
