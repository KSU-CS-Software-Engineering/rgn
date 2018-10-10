package ksu.rgn.scenario;

import ksu.rgn.utils.Tuple2;

import java.util.ArrayList;

/**
 *
 */
public class Journey {

    public final Truck truck;

    public Journey(Truck truck) {
        this.truck = truck;
    }

    private final ArrayList<Tuple2<DirectRoute, Integer>> legs = new ArrayList<>();

    public void addLeg(DirectRoute route, int load) {
        legs.add(new Tuple2<>(route, load));
    }

}
