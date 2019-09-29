package com.CIS642.Rural.GroceryImplementation;

import com.CIS642.Rural.GroceryImplementation.db.DBQueries;
import com.CIS642.Rural.GroceryImplementation.gui.Window;
import javafx.application.Application;

public class GroceryImplementationApplication {

    public static void main(String[] args) {
        //SpringApplication.run(GroceryImplementationApplication.class, args);
/*
        // Setup logging
        TPLogger.DEBUG();
        TPLogger.setLogFunction(
                new LogFunctionMultiplexer(
                        SimpleLogFunction.CONSOLE_LOG_FUNCTION, // Log to console
                        new FileLogFunction(new File("logs")) // & Log to file in "logs" directory
                ));
        TPLogger.attachUnhandledExceptionLogger();
*/
        /*
        LOG.debug("Generating scenario");

        final Scenario scenario = new Scenario("Random places", "Randomly generated scenario, somewhere in Gulf of Guinea (around gps 0N 0E)");

        final Node warehouse = new Node(new MapLocation(0, 0), Integer.MIN_VALUE);
        scenario.add(warehouse);

        for (int i = 0; i < 10; i++) {
            Node n = new Node(new MapLocation(Math.random() * 40 - 20, Math.random() * 40 - 20), (int)(Math.random() * 200 + 50));
            scenario.add(n);
        }

        scenario.add(new Truck(warehouse, warehouse, 350));

        LOG.debug("Scenario ready, computing");

        final Journey[] truckJourneys = new RandomC().computeScenario(scenario);

        final StringBuilder journeyStr = new StringBuilder();
        for (Journey j : truckJourneys) {
            journeyStr.append(j).append(System.lineSeparator());
        }

        LOG.info("Optimal journey of trucks: {} truck(s){}{}", truckJourneys.length, System.lineSeparator(), journeyStr.toString());
        */


        //SpringApplication.run(Window.class, args);
        Application.launch(Window.class);
    }

    public static DBQueries db;
}
