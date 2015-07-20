package fagagy.szeged.hu.szegednight.pubRescources;

/**
 * Created by Ádám on 15/07/19.
 */
public class Pub {

    private String name;
    private double distance;
    private boolean open;

    public Pub(String name, boolean open, double distance) {
        this.name = name;
        this.open = open;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public boolean isOpen() {
        return open;
    }

    public double getDistance() {
        return distance;
    }
}
