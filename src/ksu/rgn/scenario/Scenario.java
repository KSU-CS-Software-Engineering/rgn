package ksu.rgn.scenario;

import java.util.ArrayList;

/**
 *
 */
public class Scenario {

    private final ArrayList<Node> nodes = new ArrayList<>();
    private final ArrayList<Truck> trucks = new ArrayList<>();

    public void add(Node n) {
        nodes.add(n);
    }

    public void add(Truck t) {
        trucks.add(t);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Truck> getTrucks() {
        return trucks;
    }

}
