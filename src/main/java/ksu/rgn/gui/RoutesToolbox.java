package ksu.rgn.gui;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import ksu.rgn.arcgis.GISBridge;
import ksu.rgn.arcgis.jobs.PlanRouteJ;
import ksu.rgn.scenario.Scenario;
import ksu.rgn.utils.Future;

import static ksu.rgn.gui.Toolboxes.addButton;
import static ksu.rgn.gui.Toolboxes.createPane;

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
