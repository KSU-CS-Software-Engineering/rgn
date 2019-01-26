package ksu.rgn.gui;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import ksu.rgn.scenario.Scenario;

import static ksu.rgn.gui.Toolboxes.addButton;
import static ksu.rgn.gui.Toolboxes.createPane;

/**
 *
 */
public class RoutesToolbox {

    public static Node create(Scenario s, Window owner) {
        final VBox pane = createPane("Routes");

        addButton(pane, "Calculate", null);

        return pane;
    }


}
