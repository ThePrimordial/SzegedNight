package fagagy.szeged.hu.szegednight.shopRescources;

/**
 * Created by TheSorrow on 15/07/28.
 */
public class Shop {

    private String name;
    private double distance;
    private boolean open;
    private double latitude;
    private double longitude;
    private String openUntil;

    public String getOpenUntil() {
        return openUntil;
    }


    public Shop(String name, boolean open, double distance) {
        this.name = name;
        this.open = open;
        this.distance = distance;
    }

    public Shop(String name, boolean open, double distance, String openUntil) {
        this.name = name;
        this.open = open;
        this.distance = distance;
        this.openUntil = openUntil;
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
