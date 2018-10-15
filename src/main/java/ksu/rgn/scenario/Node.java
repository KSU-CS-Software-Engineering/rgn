package ksu.rgn.scenario;

import java.util.HashMap;

/**
 *
 */


public final class Node {

    private static int counter = 0; // TODO not thread safe!

    public final MapLocation location;
    public final int demand; // In kg
    public final int id;

    public Node(MapLocation location, int demand) {
        this.location = location;
        this.demand = demand;
        this.id = counter++;
    }

    private final HashMap<Node, DirectRoute> edges = new HashMap<>();


    @Override
    public String toString() {
        return String.format("Node #%d { demand = %d, location = %s }", id, demand, location.toString());
    }
}

