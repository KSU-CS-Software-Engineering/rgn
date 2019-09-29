package com.CIS642.Rural.GroceryImplementation.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */

@Entity
@Table(name = "scenario")
public class Scenario {

    private static final Logger LOG = LoggerFactory.getLogger(Scenario.class);


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int ID;

    @Column(nullable = false)
    public String name;
    public String description;

    @Column(nullable = false)
    public String arcGisClientID;
    @Column(nullable = false)
    public String arcGisSecret;
    @Column(nullable = false)
    public String arcGisLicense;

    public String arcGisTmpToken;
    public long arcGisTmpTokenExpires;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<MapNode> nodes = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Truck> trucks = new HashSet<>();

    public Scenario() {
        this.arcGisClientID = "";
        this.arcGisSecret = "";
        this.arcGisLicense = "";
    }

    public Scenario(String name, String description) {
        this.name = name;
        this.description = description;
        this.arcGisClientID = "";
        this.arcGisSecret = "";
        this.arcGisLicense = "";

        LOG.debug("Creating scenario '{}'{}", this.name, this.description == null ? "" : (" - '" + this.description + "'"));
    }

    public void add(MapNode n) {
        LOG.debug("{}: Adding {}", name, n);
        nodes.add(n);
    }

    public void add(Truck t) {
        LOG.debug("{}: Adding {}", name, t);
        trucks.add(t);
    }

    public Set<MapNode> getNodes() {
        return nodes;
    }

    public Set<Truck> getTrucks() {
        return trucks;
    }

}
