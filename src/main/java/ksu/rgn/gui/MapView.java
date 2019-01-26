package ksu.rgn.gui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;
import ksu.rgn.scenario.Scenario;

/**
 *
 */
public class MapView {

    public static Node create(Scenario s, Window owner) {
        BorderPane p = new BorderPane();
        p.setStyle("-fx-background-color:white;");
        Label l = new Label("This is a map");
        l.setStyle("-fx-text-fill:black;");
        p.setCenter(l);

        return p;
    }

}
