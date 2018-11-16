package ksu.rgn.db;

import ksu.rgn.scenario.Node;
import ksu.rgn.scenario.Scenario;
import ksu.rgn.scenario.Truck;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 */
public class MockDatabase implements DBQueries {

    @Override
    public void open(String url, String dbName, String user, String password) {}

    private final ArrayList<Scenario> scenarios = new ArrayList<>();

    @Override
    public void getAllScenarios(Consumer<List<Scenario>> cb) {
        if (cb != null) cb.accept(scenarios);
    }


    @Override
    public DBFuture persist(Runnable action, Object o) {
        if (action != null) action.run();
        if (o instanceof Scenario && !scenarios.contains(o)) scenarios.add((Scenario) o);
        return new DBFuture();
    }

    @Override
    public DBFuture dropScenario(Scenario s) {
        return new DBFuture();
    }

    @Override
    public DBFuture dropNode(Node n) {
        return new DBFuture();
    }

    @Override
    public DBFuture dropTruck(Truck t) {
        scenarios.forEach(s -> s.getTrucks().remove(t));
        return new DBFuture();
    }
}
