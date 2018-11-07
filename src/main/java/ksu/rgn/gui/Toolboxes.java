package ksu.rgn.gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import ksu.rgn.Main;
import ksu.rgn.scenario.MapLocation;
import ksu.rgn.scenario.Scenario;

import java.util.function.Consumer;

/**
 *
 */
public class Toolboxes {

    private static VBox createList(String text) {
        final VBox list = new VBox(10);
        list.setPadding(new Insets(10, 10, 10, 10));
        list.setPrefWidth(300);

        final Label l = new Label(text);
        l.setStyle("-fx-font-size: 2em;");
        l.setPadding(new Insets(0, 0, 10, 0));
        list.getChildren().add(l);

        return list;
    }


    private static void addTextAreaF(Pane to, String name, String initialValue, Consumer<String> onChange) {
        addTextF(to, name, initialValue, onChange);
    }
    private static void addTextF(Pane to, String name, String initialValue, Consumer<String> onChange) {
        final Label l = new Label(name);
        l.setPadding(new Insets(0, 0, -7, 0));
        final TextField tf = new TextField(initialValue);
        if (onChange != null) {
            tf.textProperty().addListener(ae -> onChange.accept(tf.getText()));
        }

        if (!name.isEmpty()) to.getChildren().add(l);
        to.getChildren().add(tf);
    }
    private static void addNumberF(Pane to, String name, String units, int initialValue, Consumer<Integer> onChange) {
        final Label l = new Label(name);
        l.setPadding(new Insets(0, 0, -7, 0));
        final TextField tf = new TextField(Integer.toString(initialValue));
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[\\d ]*")) {
                tf.setStyle("-fx-control-inner-background: #ff9696;");
            } else {
                tf.setStyle("");
                if (onChange != null) onChange.accept(Integer.parseInt(tf.getText().replaceAll(" ", "")));
            }
        });
        tf.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(tf, Priority.ALWAYS);
        final HBox hBox = new HBox(5);
        hBox.setMaxWidth(Double.MAX_VALUE);
        final Label uL = new Label(units);
        uL.setPadding(new Insets(2, 0, 2, 0));
        hBox.getChildren().addAll(tf, uL);

        if (!name.isEmpty()) to.getChildren().add(l);
        to.getChildren().add(hBox);
    }

    private static void addGpsF(Pane to, String name, double initialLat, double initialLon, Consumer<MapLocation> onChange) {
        final Label l = new Label(name);
        l.setPadding(new Insets(0, 0, -7, 0));
        final TextField latTf = new TextField(Double.toString(initialLat));
        final TextField lonTf = new TextField(Double.toString(initialLon));
        latTf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[+\\-]?[\\d]*\\.?[\\d]+")) {
                latTf.setStyle("-fx-control-inner-background: #ff9696;");
            } else {
                latTf.setStyle("");
                if (onChange != null) onChange.accept(new MapLocation(Double.parseDouble(latTf.getText()), Double.parseDouble(lonTf.getText())));
            }
        });
        lonTf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[+\\-]?[\\d]*\\.?[\\d]+")) {
                lonTf.setStyle("-fx-control-inner-background: #ff9696;");
            } else {
                lonTf.setStyle("");
                if (onChange != null) onChange.accept(new MapLocation(Double.parseDouble(latTf.getText()), Double.parseDouble(lonTf.getText())));
            }
        });

        final Button pickOnMapB = new Button("", new ImageView(Icons._16.LOCATION));
        pickOnMapB.setOnAction(ae -> {
            latTf.setText("0.0");
            lonTf.setText("0.0");
        });

        final HBox loc = new HBox(3);
        loc.getChildren().addAll(latTf, lonTf, pickOnMapB);

        if (!name.isEmpty()) to.getChildren().add(l);
        to.getChildren().add(loc);
    }

    private static void addCheckboxF(Pane to, String name, boolean initialValue, Consumer<Boolean> onChange) {
        final CheckBox c = new CheckBox(name);
        c.setSelected(initialValue);
        if (onChange != null) {
            c.selectedProperty().addListener(ae -> onChange.accept(c.isSelected()));
        }
        to.getChildren().add(c);
    }

    private static void addButton(Pane to, String name, Runnable onClick) {
        final Button b = new Button(name);
        if (onClick != null) {
            b.setOnAction(ae -> onClick.run());
        }
        to.getChildren().add(b);
    }

    private static Pane addForm(Pane to) {
        final VBox form = new VBox(10);
        form.setStyle("-fx-border-color: #ddd; -fx-border-style: solid; -fx-border-width: 0 0 0 4px");
        form.setPadding(new Insets(0, 0, 0, 12));

        to.getChildren().add(form);

        return form;
    }
    private static void addVSpace(Pane to, double space) {
        final Pane spacer = new Pane();
        spacer.setPrefHeight(space);
        to.getChildren().add(spacer);
    }


    public static Node createScenarioToolbox(Scenario s) {
        final VBox list = createList("Scenario");

        addTextF(list, "Name", s.name, null);
        addTextAreaF(list, "Description", s.description, t -> {
            s.description = t;
            Main.db.persistScenario(s);
        });

        return list;
    }

    public static Node createNodesToolbox(Scenario s) {
        final VBox list = createList("Nodes");

        final Pane addNodeForm = addForm(list);
        addTextF(addNodeForm, "Name", "", null);
        addGpsF(addNodeForm, "Position", 0, 0, null);
        addButton(addNodeForm, "Add node", null);

        addButton(list, "Remove all nodes", null);

        return list;
    }

    public static Node createTrucksToolbox(Scenario s) {
        final VBox list = createList("Trucks");

        final Pane addTruckForm = addForm(list);
        addNumberF(addTruckForm, "Capacity", "pounds", 0, null);
        addCheckboxF(addTruckForm, "Refrigerated", false, null);

        addVSpace(addTruckForm, 10);

        final Pane startingLocationForm = addForm(addTruckForm);
        addCheckboxF(startingLocationForm, "Given start location", false, null);
        addGpsF(startingLocationForm, "", 0, 0, null);

        final Pane endingLocationForm = addForm(addTruckForm);
        addCheckboxF(endingLocationForm, "Given end location", false, null);
        addGpsF(endingLocationForm, "", 0, 0, null);

        addButton(addTruckForm, "Add truck", null);

        addButton(list, "Remove all trucks", null);

        return list;
    }

    public static Node createRoutesToolbox(Scenario s) {
        final VBox list = createList("Routes");

        addButton(list, "Calculate", null);

        return list;
    }

    public static Node createMapView(Scenario s) {
        BorderPane p = new BorderPane();
        p.setStyle("-fx-background-color:white;");
        Label l = new Label("This is a map");
        l.setStyle("-fx-text-fill:black;");
        p.setCenter(l);

        return p;
    }

}
