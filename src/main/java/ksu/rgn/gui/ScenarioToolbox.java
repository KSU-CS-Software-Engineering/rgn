package ksu.rgn.gui;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import ksu.rgn.Main;
import ksu.rgn.arcgis.GISBridge;
import ksu.rgn.arcgis.jobs.TestConnectionJ;
import ksu.rgn.utils.Future;
import ksu.rgn.scenario.Scenario;

import static ksu.rgn.gui.Toolboxes.*;

/**
 *
 */
public class ScenarioToolbox {

    public static Node create(Scenario s, Window owner) {
        final VBox pane = createPane("Scenario");

        addTextF(pane, "Name", s.name, null);
        addTextAreaF(pane, "Description", s.description, t -> Main.db.persist(() -> s.description = t, s));

        addVSpace(pane, 30);
        final Pane arcGis = addForm(pane);
        addHeader(arcGis, "ArcGIS software link");

        final String[] params = new String[] {s.arcGisUrl, s.arcGisClientID, s.arcGisToken};

        addTextF(arcGis, "Server URL", params[0], t -> {
            params[0] = t;
            Main.db.persist(() -> s.arcGisUrl = t, s);
        });
        addTextF(arcGis, "Client ID", params[1], t -> {
            params[1] = t;
            Main.db.persist(() -> s.arcGisClientID = t, s);
        });
        addTextF(arcGis, "Access token", params[2], t -> {
            params[2] = t;
            Main.db.persist(() -> s.arcGisToken= t, s);
        });
        final Label[] lEnc = new Label[1];
        lEnc[0] = addButtonWithLabel(arcGis, "Test connection", () -> {
            final GISBridge b = new GISBridge(params[0], params[1], params[2]);
            Platform.runLater(() -> {
                lEnc[0].setText("Testing...");
                lEnc[0].setStyle("-fx-padding: 5px 0px 0px 5px;");
            });
            b.addJob(new TestConnectionJ())
                .onFail(o -> {
                    Platform.runLater(() -> {
                        lEnc[0].setText(o.toString());
                        lEnc[0].setStyle("-fx-padding: 5px 0px 0px 5px; -fx-text-fill: red;");
                    });
                    b.close();
                })
                .onSuccess(o -> {
                    Platform.runLater(() -> {
                        lEnc[0].setText("Success");
                        lEnc[0].setStyle("-fx-padding: 5px 0px 0px 5px; -fx-text-fill: green;");
                    });
                    b.close();
                });
        });

        return pane;
    }

}
