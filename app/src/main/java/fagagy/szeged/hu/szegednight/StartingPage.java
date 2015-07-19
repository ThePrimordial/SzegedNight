package fagagy.szeged.hu.szegednight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


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
                i.setClass(this, BrowserPage.class);
                startActivity(i);
                break;
            case R.id.btnATM:
                i.setClass(this, AtmPage.class);
                startActivity(i);
                break;
            case R.id.btnParties:
                i.setClass(this, PartiesPage.class);
                startActivity(i);
                break;
            case R.id.btnShops:
                i.setClass(this, ShopsPage.class);
                startActivity(i);
                break;
            case R.id.btnTaxies:
                i.setClass(this, TaxiPage.class);
                startActivity(i);
                break;
            case R.id.btnTobaccoShops:
                i.setClass(this, TobaccoShopsPage.class);
                startActivity(i);
                break;
            case R.id.btnRestaurants:
                i.setClass(this, RestaurantsPage.class);
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
