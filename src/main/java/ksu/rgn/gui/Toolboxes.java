package ksu.rgn.gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Callback;
import ksu.rgn.Main;
import ksu.rgn.scenario.MapLocation;
import ksu.rgn.scenario.Scenario;
import ksu.rgn.scenario.Truck;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 *
 */
public class Toolboxes {

    private static VBox createPane(String text) {
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
            tf.textProperty().addListener((observableValue, s, t1) -> onChange.accept(tf.getText()));
            onChange.accept(tf.getText());
        }

        if (!name.isEmpty()) to.getChildren().add(l);
        to.getChildren().add(tf);
    }
    private static void addNumberF(Pane to, String name, String units, int initialValue, Consumer<Integer> onChange) {
        final Label l = new Label(name);
        l.setPadding(new Insets(0, 0, -7, 0));
        final TextField tf = new TextField(Integer.toString(initialValue));
        final ChangeListener<? super String> listener = (observable, oldValue, newValue) -> {
            if (!newValue.matches("[\\d ]*") || newValue.isEmpty()) {
                tf.setStyle("-fx-control-inner-background: #ff9696;");
                if (onChange != null) onChange.accept(null);
            } else {
                tf.setStyle("");
                if (onChange != null) onChange.accept(Integer.parseInt(tf.getText().replaceAll(" ", "")));
            }
        };
        tf.textProperty().addListener(listener);
        listener.changed(null, null, tf.getText());

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

        final ChangeListener<? super String> latListener = (observable, oldValue, newValue) -> {
            if (!newValue.matches("[+\\-]?[\\d]*\\.?[\\d]+") || newValue.isEmpty()) {
                latTf.setStyle("-fx-control-inner-background: #ff9696;");
                if (onChange != null) onChange.accept(null);
            } else {
                latTf.setStyle("");
                if (onChange != null) onChange.accept(new MapLocation(Double.parseDouble(latTf.getText()), Double.parseDouble(lonTf.getText())));
            }
        };
        latTf.textProperty().addListener(latListener);
        latListener.changed(null, null, latTf.getText());

        final ChangeListener<? super String> lonListener = (observable, oldValue, newValue) -> {
            if (!newValue.matches("[+\\-]?[\\d]*\\.?[\\d]+") || newValue.isEmpty()) {
                lonTf.setStyle("-fx-control-inner-background: #ff9696;");
                if (onChange != null) onChange.accept(null);
            } else {
                lonTf.setStyle("");
                if (onChange != null) onChange.accept(new MapLocation(Double.parseDouble(latTf.getText()), Double.parseDouble(lonTf.getText())));
            }
        };
        lonTf.textProperty().addListener(lonListener);
        lonListener.changed(null, null, lonTf.getText());

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
            onChange.accept(c.isSelected());
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
    private static Label addButtonWithLabel(Pane to, String name, Runnable onClick) {
        final Button b = new Button(name);
        if (onClick != null) {
            b.setOnAction(ae -> onClick.run());
        }

        final Label l = new Label();

        final HBox hBox = new HBox(3);
        hBox.getChildren().addAll(b, l);

        to.getChildren().add(hBox);

        return l;
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


    private static <T> ListView<T> addList(Pane to, Callback<ListView<T>, ListCell<T>> renderer) {
        final ListView<T> list = new ListView<>();

        list.setCellFactory(renderer);
        list.setEditable(false);
        list.setFixedCellSize(34 + 8);

        to.getChildren().add(list);

        return list;
    }


    public static Node createScenarioToolbox(Scenario s) {
        final VBox pane = createPane("Scenario");

        addTextF(pane, "Name", s.name, null);
        addTextAreaF(pane, "Description", s.description, t -> {
            s.description = t;
            Main.db.persistScenario(s);
        });

        return pane;
    }

    public static Node createNodesToolbox(Scenario s) {
        final VBox pane = createPane("Nodes");

        final Pane addNodeForm = addForm(pane);
        addTextF(addNodeForm, "Name", "", null);
        addGpsF(addNodeForm, "Position", 0, 0, null);
        addButton(addNodeForm, "Add node", null);

        addButton(pane, "Remove all nodes", null);

        return pane;
    }

    public static Node createTrucksToolbox(Scenario s) {
        final VBox pane = createPane("Trucks");

        final Pane addTruckForm = addForm(pane);
        final FormValues truckFormV = new FormValues();

        addTextF(addTruckForm, "Name", "", truckFormV.listenF("Name"));
        addNumberF(addTruckForm, "Capacity", "pounds", 0, truckFormV.listenF("Capacity"));
        addCheckboxF(addTruckForm, "Refrigerated", false, truckFormV.listenF("Refrigerated"));

        addVSpace(addTruckForm, 10);

        final Pane startingLocationForm = addForm(addTruckForm);
        addCheckboxF(startingLocationForm, "Given start location", false, truckFormV.listenF("Start enable"));
        addGpsF(startingLocationForm, "", 0, 0, truckFormV.listenF("Start location"));

        final Pane endingLocationForm = addForm(addTruckForm);
        addCheckboxF(endingLocationForm, "Given end location", false, truckFormV.listenF("End enable"));
        addGpsF(endingLocationForm, "", 0, 0, truckFormV.listenF("End location"));

        @SuppressWarnings("unchecked") final ListView<Truck> truckListEncapsulation[] = new ListView[1];
        final Label addTruckL = addButtonWithLabel(addTruckForm, "Add truck", () -> {
            if (truckFormV.isValid) {

                final Truck t = new Truck();

                t.name = (String) truckFormV.vars.get("Name");
                t.capacity = (Integer) truckFormV.vars.get("Capacity");
                t.refrigerated = (Boolean) truckFormV.vars.get("Refrigerated");

                // TODO start/end location

                s.add(t);
                Main.db.persistScenario(s);
                Platform.runLater(() -> truckListEncapsulation[0].setItems(FXCollections.observableList(s.getTrucks())));
            }
        });
        addTruckL.setStyle("-fx-text-fill: red; -fx-padding: 5px 0px 0px 5px;");
        truckFormV.onIsValidChange = isValid -> {
            if (isValid) {
                addTruckL.setText("");
            } else {
                addTruckL.setText("Invalid input");
            }
        };


        addVSpace(pane, 10);

        truckListEncapsulation[0] = addList(pane, listView -> new ListCell<Truck>() {
            @Override
            public void updateItem(Truck t, boolean empty) {
                super.updateItem(t, empty);
                setGraphic(null);

                if (t != null) {
                    final HBox row = new HBox(5);

                    final Button goB = new Button("", new ImageView(Icons._24.TRASH));
                    goB.setPadding(new Insets(5, 8, 5, 8));
                    goB.setOnAction(e -> {
                        s.getTrucks().remove(t);
                        Main.db.persistScenario(s);
                        Main.db.dropTruck(t);
                        Platform.runLater(() -> truckListEncapsulation[0].setItems(FXCollections.observableList(s.getTrucks())));
                    });

                    final Pane hGrow = new Pane();
                    HBox.setHgrow(hGrow, Priority.ALWAYS);

                    final Label nameL = new Label("Truck - " + t.name);
                    nameL.setStyle("-fx-font-weight: bold;");

                    final Label descL = new Label((t.refrigerated ? "Refrigerated, " : "") + t.capacity + " pounds");

                    final VBox labelsG = new VBox(0);
                    labelsG.getChildren().addAll(nameL, descL);

                    row.getChildren().addAll(
                            labelsG,
                            hGrow,
                            goB
                    );
                    setGraphic(row);
                }
            }


        });
        truckListEncapsulation[0].setItems(FXCollections.observableList(s.getTrucks()));

        return pane;
    }

    public static Node createRoutesToolbox(Scenario s) {
        final VBox pane = createPane("Routes");

        addButton(pane, "Calculate", null);

        return pane;
    }

    public static Node createMapView(Scenario s) {
        BorderPane p = new BorderPane();
        p.setStyle("-fx-background-color:white;");
        Label l = new Label("This is a map");
        l.setStyle("-fx-text-fill:black;");
        p.setCenter(l);

        return p;
    }



    private static class FormValues {

        final HashMap<String, Object> vars = new HashMap<>();
        boolean isValid = true;
        Consumer<Boolean> onIsValidChange = null;


        public <T> Consumer<T> listenF(String varName) {
            return o -> {
                vars.put(varName, o);
                if (o == null) {
                    if (onIsValidChange != null && isValid) onIsValidChange.accept(false);
                    isValid = false;
                } else {
                    boolean newIsValid = !vars.values().contains(null);
                    if (onIsValidChange != null && (isValid != newIsValid)) onIsValidChange.accept(newIsValid);
                    isValid = newIsValid;
                }
            };
        }

    }

}
