package ksu.rgn.db;

import ksu.rgn.scenario.MapNode;
import ksu.rgn.scenario.Scenario;
import ksu.rgn.scenario.Truck;
import ksu.rgn.utils.Future;

import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
public interface DBQueries {

    void getAllScenarios(Consumer<List<Scenario>> cb);

    Future persist(Runnable action, Object o);

    Future dropScenario(Scenario s);
    Future dropNode(MapNode n);
    Future dropTruck(Truck t);

    void open(String url, String dbName, String user, String password);

    default void close(Runnable onClosed) {
        if (onClosed != null) onClosed.run();
    }

    Future preCache(Runnable job);
}
