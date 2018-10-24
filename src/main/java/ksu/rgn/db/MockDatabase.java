package ksu.rgn.db;

import ksu.rgn.scenario.Node;
import ksu.rgn.scenario.Scenario;
import ksu.rgn.scenario.Truck;

import java.util.ArrayList;

/**
 *
 */
public class MockDatabase implements DBQueries {

    @Override
    public void open(String uri, String user, String password) {}

    private final ArrayList<Scenario> scenarios = new ArrayList<>();

    @Override
    public ArrayList<Scenario> getAllScenarios() {
        return scenarios;
    }

    @Override
    public void persistNode(Node n) {

    }

    @Override
    public void persistTruck(Truck t) {

    }

    @Override
    public void persistScenario(Scenario s) {
        if (!scenarios.contains(s)) scenarios.add(s);
    }
}
