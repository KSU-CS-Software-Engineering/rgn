package ksu.rgn.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */

@Entity
@Table(name="scenario")
public class Scenario {

    private static final Logger LOG = LoggerFactory.getLogger(Scenario.class);


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int ID;

    @Column(nullable = false)
    public String name;
    public String description;

    @Column(nullable = false)
    public String arcGisToken;
    @Column(nullable = false)
    public String arcGisUrl;
    @Column(nullable = false)
    public String arcGisClientID;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private final List<MapNode> nodes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private final List<Truck> trucks = new ArrayList<>();

    public Scenario() {
        this.arcGisToken = "";
        this.arcGisUrl = "";
        this.arcGisClientID = "";
    }

    public Scenario(String name, String description) {
        this.name = name;
        this.description = description;
        this.arcGisToken = "";
        this.arcGisUrl = "";
        this.arcGisClientID = "";

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

    public List<MapNode> getNodes() {
        return nodes;
    }

    public List<Truck> getTrucks() {
        return trucks;
    }

}
