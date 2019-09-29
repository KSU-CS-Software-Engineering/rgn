package com.CIS642.Rural.GroceryImplementation.scenario;

import javax.persistence.*;

/**
 *
 */


@Entity
@Table(name = "node")
public class MapNode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int ID;

    public double gpsLat, gpsLon;
    public String name;

    @Column(nullable = false)
    public int demand; // In std palettes
    @Column(nullable = false)
    public int supply; // In std palettes


    @Override
    public String toString() {
        return String.format("Node #%d { name = %s, demand = %d, lat = %f, lon = %f }", ID, name, demand, gpsLat, gpsLon);
    }
}
