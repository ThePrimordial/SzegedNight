package fagagy.szeged.hu.szegednight.partyRescources;

import java.util.Date;

/**
 * Created by TheSorrow on 15/07/28.
 */
public class Party {

    private String place;
    private String event;
    private double distance;
    private double latitude;
    private double longitude;

    public Party(String place, String event, double distance, Date date) {
        this.place = place;
        this.event = event;
        this.distance = distance;
        this.date = date;
    }

    public double getDistance() {
        return distance;
    }

    public Date getDate() {
        return date;
    }

    public String getEvent() {
        return event;
    }

    public String getPlace() {
        return place;
    }

    private Date date;

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


}
