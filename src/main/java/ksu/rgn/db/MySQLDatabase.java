package ksu.rgn.db;

import ksu.rgn.scenario.Node;
import ksu.rgn.scenario.Scenario;
import ksu.rgn.scenario.Truck;

import java.util.ArrayList;

/**
 *
 */
public class MySQLDatabase implements DBQueries {

    @Override
    public void open(String uri, String user, String password) {

    }

    @Override
    public void close() {

    }

    @Override
    public ArrayList<Scenario> getAllScenarios() {
        return null;
    }

    @Override
    public void persistScenario(Scenario s) {

    }

    @Override
    public void persistNode(Node n) {

    }

    @Override
    public void persistTruck(Truck t) {

    }
}
