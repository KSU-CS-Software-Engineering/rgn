package ksu.rgn.scenario;

/**
 *
 */
public class MapLocation {

    public final double lat, lon;

    // TODO store address and stuff

    public MapLocation(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        return String.format("Location { lat = %f, lon = %f }", lat, lon);
    }
}
