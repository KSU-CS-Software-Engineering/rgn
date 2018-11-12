package ksu.rgn.db;

import ksu.rgn.scenario.Node;
import ksu.rgn.scenario.Scenario;
import ksu.rgn.scenario.Truck;

import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
public interface DBQueries {

    void getAllScenarios(Consumer<List<Scenario>> cb);

    void persistScenario(Scenario s);
    void persistNode(Node n);
    void persistTruck(Truck t);

    void dropScenario(Scenario s);
    void dropNode(Node n);
    void dropTruck(Truck t);

    void open(String url, String dbName, String user, String password);

    default void close(Runnable onClosed) {
        if (onClosed != null) onClosed.run();
    }

}
