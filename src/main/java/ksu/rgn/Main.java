package ksu.rgn;

import ksu.rgn.computation.RandomC;
import ksu.rgn.scenario.*;

/**
 *
 */
public class Main {

    public static void main(String[] args) {

        final Scenario scenario = new Scenario();

        final Node warehouse = new Node(new MapLocation(0, 0), Integer.MIN_VALUE);
        scenario.add(warehouse);

        for (int i = 0; i < 10; i++) {
            Node n = new Node(new MapLocation(Math.random() * 40 - 20, Math.random() * 40 - 20), (int)(Math.random() * 200 + 50));
            scenario.add(n);
        }

        scenario.add(new Truck(warehouse, warehouse, 350));

        final Journey[] truckJourneys = new RandomC().computeScenario(scenario);

        // sout truckJourneys
    }

}
