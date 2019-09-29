package com.CIS642.Rural.GroceryImplementation.gui;

import com.CIS642.Rural.GroceryImplementation.arcgis.GISBridge;
import com.CIS642.Rural.GroceryImplementation.arcgis.jobs.PlanRouteJ;
import com.CIS642.Rural.GroceryImplementation.scenario.Scenario;
import com.CIS642.Rural.GroceryImplementation.utils.Future;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import static com.CIS642.Rural.GroceryImplementation.gui.Toolboxes.addButton;
import static com.CIS642.Rural.GroceryImplementation.gui.Toolboxes.createPane;

/**
 *
 */
public class RoutesToolbox {

    public static Node create(Scenario s, Window owner) {
        final VBox pane = createPane("Routes");

        addButton(pane, "Calculate", () -> {
            final GISBridge b = new GISBridge(s.arcGisTmpToken);
            final Future f = b.addJob(new PlanRouteJ(s.getNodes()));
            f.onFinish(o -> System.out.println("Result: " + o));
        });

        return pane;
    }


}
