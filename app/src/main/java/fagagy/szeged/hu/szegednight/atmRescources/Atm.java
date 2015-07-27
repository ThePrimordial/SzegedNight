package fagagy.szeged.hu.szegednight.atmRescources;

import android.location.Location;

/**
 * Created by TheSorrow on 15/07/23.
 */
public class Atm {

    private String name;
    private String type;
    private double distance;
    private double latitude;
    private double longitude;

    public Atm(String name,String type, double distance) {
        this.name = name;
        this.type = type;
        this.distance = distance;
    }

    public String getType() {
        return type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getDistance(){
        return distance;
    }


}