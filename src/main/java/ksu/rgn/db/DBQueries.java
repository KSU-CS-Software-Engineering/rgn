package ksu.rgn.db;

import ksu.rgn.scenario.Node;
import ksu.rgn.scenario.Scenario;
import ksu.rgn.scenario.Truck;

import java.util.ArrayList;

/**
 *
 */
public interface DBQueries {

    ArrayList<Scenario> getAllScenarios();
    void persistScenario(Scenario s);
    void persistNode(Node n);
    void persistTruck(Truck t);

    void open(String url, String dbName, String user, String password);
    default void close() {}

}
