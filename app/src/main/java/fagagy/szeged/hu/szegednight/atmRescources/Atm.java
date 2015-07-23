package fagagy.szeged.hu.szegednight.atmRescources;

/**
 * Created by TheSorrow on 15/07/23.
 */
public class Atm {

    private String name;
    private String type;
    private double distance;

    public Atm(String name,String type, double distance) {
        this.name = name;
        this.type = type;
        this.distance = distance;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public double getDistance(){
        return distance;
    }

}