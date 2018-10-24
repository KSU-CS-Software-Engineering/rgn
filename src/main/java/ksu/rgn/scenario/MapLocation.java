package ksu.rgn.scenario;

import javax.persistence.*;

/**
 *
 */

@Entity
@Table(name="location")
public class MapLocation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int ID;

    @Column(nullable = false)
    public double lat, lon;

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
