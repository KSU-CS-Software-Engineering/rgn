package ksu.rgn.scenario;

import java.util.HashMap;

/**
 *
 */


public final class Node {

    public final MapLocation location;
    public final int demand; // In kg

    public Node(MapLocation location, int demand) {
        this.location = location;
        this.demand = demand;
    }

    private final HashMap<Node, DirectRoute> edges = new HashMap<>();


}

