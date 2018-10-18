package ksu.rgn.db;

import ksu.rgn.scenario.Scenario;

import java.util.ArrayList;

/**
 *
 */
public interface DBQueries {

    ArrayList<Scenario> getAllScenarios();
    void persistScenario(Scenario s);

}
