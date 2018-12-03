package ksu.rgn.gui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import ksu.rgn.Main;
import ksu.rgn.arcgis.GISBridge;
import ksu.rgn.arcgis.GISFuture;
import ksu.rgn.scenario.MapLocation;
import ksu.rgn.scenario.Scenario;
import ksu.rgn.scenario.MapNode;
import ksu.rgn.scenario.Truck;
import ksu.rgn.utils.Tuple2;
import ksu.rgn.utils.Units;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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

    private static void addHeader(Pane to, String text) {
        final Label l = new Label(text);
        l.setStyle("-fx-font-size: 1.3em;");
        l.setPadding(new Insets(0, 0, 5, 0));
        to.getChildren().add(l);
    }

    private static void addTextAreaF(Pane to, String name, String initialValue, Consumer<String> onChange) {
        final Label l = new Label(name);
        l.setPadding(new Insets(0, 0, -7, 0));
        final TextArea tf = new TextArea(initialValue);
        tf.setWrapText(true);
        tf.setPrefRowCount(5);
        if (onChange != null) {
            tf.textProperty().addListener((observableValue, s, t1) -> onChange.accept(tf.getText()));
            onChange.accept(tf.getText());
        }

        if (!name.isEmpty()) to.getChildren().add(l);
        to.getChildren().add(tf);
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


    private static VBox addList(Pane to) {
        final VBox list = new VBox(10);
        list.setStyle("-fx-border-color: #ddd; -fx-border-style: solid; -fx-border-width: 0 0 0 1px");
        list.setPadding(new Insets(0, 0, 0, 5));
        list.setMaxHeight(Double.MAX_VALUE);

        to.getChildren().add(list);

        return list;
    }


    public static Node createScenarioToolbox(Scenario s) {
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
            GISFuture f = new GISFuture();
            f.fail(o -> {
                Platform.runLater(() -> {
                    lEnc[0].setText(o.toString());
                    lEnc[0].setStyle("-fx-padding: 5px 0px 0px 5px; -fx-text-fill: red;");
                });
                b.close();
            });
            f.success(o -> {
                Platform.runLater(() -> {
                    lEnc[0].setText("Success");
                    lEnc[0].setStyle("-fx-padding: 5px 0px 0px 5px; -fx-text-fill: green;");
                });
                b.close();
            });
            Platform.runLater(() -> {
                lEnc[0].setText("Testing...");
                lEnc[0].setStyle("-fx-padding: 5px 0px 0px 5px;");
            });
            b.testConnection(f);
        });

        return pane;
    }

    public static Node createNodesToolbox(Scenario s) {
        final Function<Tuple2<Object, Button>, HBox> nodeRenderer = o -> {
            final MapNode n = (MapNode) o._1;

            final HBox row = new HBox(5);
            row.setStyle("-fx-background-color: white; -fx-padding: 5px;-fx-border-color: #ddd; -fx-border-style: solid; -fx-border-width: 1px");

            final Pane hGrow = new Pane();
            HBox.setHgrow(hGrow, Priority.ALWAYS);


            final Label nameL = new Label("Node - " + n.name);
            nameL.setStyle("-fx-font-weight: bold;");

            final Label descL = new Label(Units.toPounds(n.demand) + " / " + Units.toPounds(n.supply) + " pounds");

            final VBox labelsG = new VBox(0);
            labelsG.getChildren().addAll(nameL, descL);

            row.getChildren().addAll(
                    labelsG,
                    hGrow,
                    o._2
            );

            return row;
        };


        final VBox pane = createPane("Nodes");

        final Pane addNodeForm = addForm(pane);
        final FormValues nodeFormV = new FormValues();

        addTextF(addNodeForm, "Name", "", nodeFormV.listenF("Name"));
        addGpsF(addNodeForm, "Location", 0, 0, nodeFormV.listenF("Location"));
        addVSpace(addNodeForm, 5);
        addNumberF(addNodeForm, "Demand", "pounds", 0, nodeFormV.listenF("Demand"));
        addNumberF(addNodeForm, "Supply", "pounds", 0, nodeFormV.listenF("Supply"));
        addVSpace(addNodeForm, 10);
        addVSpace(pane, 10);

        final VBox nodeList = addList(pane);
        final Label addNodeL = addButtonWithLabel(addNodeForm, "Add node", () -> {
            if (nodeFormV.isValid) {

                final MapNode n = new MapNode();

                n.name = (String) nodeFormV.vars.get("Name");
                n.location = (MapLocation) nodeFormV.vars.get("Location");

                n.demand = Units.toKilograms((Integer) nodeFormV.vars.get("Demand"));
                n.supply = Units.toKilograms((Integer) nodeFormV.vars.get("Supply"));

                Main.db.persist(() -> s.add(n), s).finished(() -> Platform.runLater(() -> updateList(nodeList, s, null, nodeRenderer, s::getNodes)));
                Platform.runLater(() -> {
                    final MapNode[] ns = s.getNodes().toArray(new MapNode[s.getNodes().size() + 1]);
                    ns[ns.length - 1] = n;
                    updateList(nodeList, s, ns, nodeRenderer, s::getNodes);
                });
            }
        });
        addNodeL.setStyle("-fx-text-fill: red; -fx-padding: 5px 0px 0px 5px;");
        nodeFormV.onIsValidChange = isValid -> {
            if (isValid) {
                addNodeL.setText("");
            } else {
                addNodeL.setText("Invalid input");
            }
        };

        updateList(nodeList, s, null, nodeRenderer, s::getNodes);

        addButton(pane, "Remove all nodes", () -> {
            Main.db.persist(() -> s.getNodes().clear(), s).finished(() -> Platform.runLater(() -> updateList(nodeList, s, null, nodeRenderer, s::getNodes)));
            Platform.runLater(() -> updateList(nodeList, s, new MapNode[]{}, nodeRenderer, s::getNodes));
        });

        return pane;
    }

    private static void updateList(VBox list, Scenario s, Object[] future, Function<Tuple2<Object, Button>, HBox> renderer, Supplier<List> getUpToDateData) {
        list.getChildren().clear();

        Object[] ts = future == null ? getUpToDateData.get().toArray(new Object[0]) : future;

        for (Object t : ts) {
            final Button goB = new Button("", new ImageView(Icons._24.TRASH));
            goB.setPadding(new Insets(5, 8, 5, 8));
            goB.setOnAction(e -> {
                Main.db.persist(() -> getUpToDateData.get().remove(t), s).finished(() -> Platform.runLater(() -> updateList(list, s, null, renderer, getUpToDateData)));
                Platform.runLater(() -> {
                    final Object[] deleted = new Object[ts.length - 1];
                    int skip = 0;
                    for (int i = 0; i < deleted.length; i++) {
                        if (ts[i + skip] == t) skip++;
                        deleted[i] = ts[i + skip];
                    }

                    updateList(list, s, deleted, renderer, getUpToDateData);
                });
            });

            list.getChildren().add(renderer.apply(new Tuple2<>(t, goB)));
        }
    }

    public static Node createTrucksToolbox(Scenario s) {
        final Function<Tuple2<Object, Button>, HBox> truckRenderer = o -> {
            final Truck t = (Truck) o._1;

            final HBox row = new HBox(5);
            row.setStyle("-fx-background-color: white; -fx-padding: 5px;-fx-border-color: #ddd; -fx-border-style: solid; -fx-border-width: 1px");

            final Pane hGrow = new Pane();
            HBox.setHgrow(hGrow, Priority.ALWAYS);


            final Label nameL = new Label("Truck - " + t.name);
            nameL.setStyle("-fx-font-weight: bold;");

            final Label descL = new Label((t.refrigerated ? "Refrigerated, " : "") + Units.toPounds(t.capacity) + " pounds");

            final VBox labelsG = new VBox(0);
            labelsG.getChildren().addAll(nameL, descL);

            row.getChildren().addAll(
                    labelsG,
                    hGrow,
                    o._2
            );

            return row;
        };


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

        addVSpace(pane, 10);
        final VBox truckList = addList(pane);
        final Label addTruckL = addButtonWithLabel(addTruckForm, "Add truck", () -> {
            if (truckFormV.isValid) {

                final Truck t = new Truck();

                t.name = (String) truckFormV.vars.get("Name");
                t.capacity = Units.toKilograms((Integer) truckFormV.vars.get("Capacity"));
                t.refrigerated = (Boolean) truckFormV.vars.get("Refrigerated");

                // TODO start/end location

                Main.db.persist(() -> s.add(t), s).finished(() -> Platform.runLater(() -> updateList(truckList, s, null, truckRenderer, s::getTrucks)));
                Platform.runLater(() -> {
                    final Truck[] ts = s.getTrucks().toArray(new Truck[s.getTrucks().size() + 1]);
                    ts[ts.length - 1] = t;
                    updateList(truckList, s, ts, truckRenderer, s::getTrucks);
                });
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

        updateList(truckList, s, null, truckRenderer, s::getTrucks);

        addVSpace(pane, 10);

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
