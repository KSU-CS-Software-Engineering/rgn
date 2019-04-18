package ksu.rgn.gui;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ksu.rgn.Main;
import ksu.rgn.arcgis.GISBridge;
import ksu.rgn.arcgis.jobs.RequestTmpTokenJ;
import ksu.rgn.scenario.Scenario;

import java.util.Optional;

import static ksu.rgn.gui.Toolboxes.*;

/**
 *
 */
public class ScenarioToolbox {

    public static Node create(Scenario s, Window owner) {
        final VBox pane = createPane("Scenario");

        addTextF(pane, "Name", s.name, t -> Main.db.persist(() -> s.name = t, s));
        addTextAreaF(pane, "Description", s.description, t -> Main.db.persist(() -> s.description = t, s));

        addVSpace(pane, 30);
        final Pane arcGis = addForm(pane);
        addHeader(arcGis, "ArcGIS software link");

        final String[] params = new String[] {s.arcGisClientID, s.arcGisSecret, s.arcGisLicense};

        final Label[] lEnc = new Label[1];
        addTextF(arcGis, "Client ID", params[0], t -> {
            params[0] = t;
            if (lEnc[0] != null) lEnc[0].setText("");
            Main.db.persist(() -> {
                s.arcGisClientID = t;
                s.arcGisTmpToken = null;
                s.arcGisTmpTokenExpires = 0;
            }, s);
        });
        addTextF(arcGis, "Client secret", params[1], t -> {
            params[1] = t;
            if (lEnc[0] != null) lEnc[0].setText("");
            Main.db.persist(() -> {
                s.arcGisSecret = t;
                s.arcGisTmpToken = null;
                s.arcGisTmpTokenExpires = 0;
            }, s);
        });
        addTextF(arcGis, "License key", params[2], t -> {
            params[2] = t;
            if (lEnc[0] != null) lEnc[0].setText("");
            Main.db.persist(() -> {
                s.arcGisLicense = t;
                s.arcGisTmpToken = null;
                s.arcGisTmpTokenExpires = 0;
            }, s);
        });
        lEnc[0] = addButtonWithLabel(arcGis, "Validate", () -> {
            final GISBridge b = new GISBridge(null);
            final long expires = System.currentTimeMillis() + RequestTmpTokenJ.EXPIRATION;
            Platform.runLater(() -> {
                lEnc[0].setText("Testing...");
                lEnc[0].setStyle("-fx-padding: 5px 0px 0px 5px;");
            });
            b.addJob(new RequestTmpTokenJ(params[0], params[1]))
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
                    Main.db.persist(() -> {
                        s.arcGisTmpToken = (String) o;
                        s.arcGisTmpTokenExpires = expires;
                    }, s);
                    b.close();
                });
            MapView.current().w.border.setCenter(MapView.create(s, s.arcGisLicense, MapView.current().w));
        });
        if (s.arcGisTmpToken != null) {
            lEnc[0].setText("Success");
            lEnc[0].setStyle("-fx-padding: 5px 0px 0px 5px; -fx-text-fill: green;");
        }

        addVSpace(pane, 50);
        final Pane dangerZone = addForm(pane);
        addHeader(dangerZone, "Danger zone");
        addButton(dangerZone, "Delete scenario", () -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete scenario?");
            alert.setHeaderText("You are about to delete a scenario");
            alert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Main.db.dropScenario(s);
                owner.closeScenario();
            }
        });
        addVSpace(pane, 50);

        return pane;
    }

}
