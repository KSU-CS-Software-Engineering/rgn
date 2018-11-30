package ksu.rgn.db;

import ksu.rgn.scenario.MapNode;
import ksu.rgn.scenario.Scenario;
import ksu.rgn.scenario.Truck;

import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
public interface DBQueries {

    void getAllScenarios(Consumer<List<Scenario>> cb);

    DBFuture persist(Runnable action, Object o);

    DBFuture dropScenario(Scenario s);
    DBFuture dropNode(MapNode n);
    DBFuture dropTruck(Truck t);

    void open(String url, String dbName, String user, String password);

    default void close(Runnable onClosed) {
        if (onClosed != null) onClosed.run();
    }

}
