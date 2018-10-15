package ksu.rgn.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 *
 */
public class Scenario {

    private static final Logger LOG = LoggerFactory.getLogger(Scenario.class);


    private final ArrayList<Node> nodes = new ArrayList<>();
    private final ArrayList<Truck> trucks = new ArrayList<>();

    private final String name, description;

    public Scenario(String name, String description) {
        if (name == null || name.isEmpty()) throw new NullPointerException("'name' argument cannot be null or empty");

        this.name = name;
        this.description = description;

        LOG.debug("Creating scenario '{}'{}", this.name, this.description == null ? "" : (" - '" + this.description + "'"));
    }

    public void add(Node n) {
        LOG.debug("{}: Adding {}", name, n);
        nodes.add(n);
    }

    public void add(Truck t) {
        LOG.debug("{}: Adding {}", name, t);
        trucks.add(t);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Truck> getTrucks() {
        return trucks;
    }

}
