package fagagy.szeged.hu.szegednight.pubResources;

public class Pub {

    private String name;
    private double distance;
    private boolean open;
    private double latitude;
    private double longitude;
    private String openUntil;
    private boolean subscribed;

    public String getObjectId() {
        return objectId;
    }

    private String objectId;

    public boolean isSubscribed() {
        return subscribed;
    }

    public String getOpenUntil() {
        return openUntil;
    }


    public Pub(String name, boolean subscribed, boolean open, double distance, String objectId) {
        this.objectId = objectId;
        this.subscribed = subscribed;
        this.name = name;
        this.open = open;
        this.distance = distance;
    }

    public Pub(String name, boolean subscribed, boolean open, double distance, String openUntil,String objectId ) {
        this.objectId = objectId;
        this.subscribed = subscribed;
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
