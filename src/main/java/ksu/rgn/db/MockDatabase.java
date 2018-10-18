package ksu.rgn.db;

import ksu.rgn.scenario.Scenario;

import java.util.ArrayList;

/**
 *
 */
public class MockDatabase implements DBQueries {

    private final ArrayList<Scenario> scenarios = new ArrayList<>();

    @Override
    public ArrayList<Scenario> getAllScenarios() {
        return scenarios;
    }

    @Override
    public void persistScenario(Scenario s) {
        if (!scenarios.contains(s)) scenarios.add(s);
    }
}
