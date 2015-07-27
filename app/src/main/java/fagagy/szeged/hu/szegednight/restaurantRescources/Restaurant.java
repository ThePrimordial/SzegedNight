package fagagy.szeged.hu.szegednight.restaurantRescources;

/**
 * Created by TheSorrow on 15/07/20.
 */
public class Restaurant {

    private String name;
    private double distance;
    private boolean open;
    private double latitude;
    private double longitude;

    public Restaurant(String name, boolean open, double distance) {
        this.name = name;
        this.open = open;
        this.distance = distance;
    }

    public String getName() {
        return name;
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

    public boolean isOpen() {
        return open;
    }

    public double getDistance() {
        return distance;
    }
}
