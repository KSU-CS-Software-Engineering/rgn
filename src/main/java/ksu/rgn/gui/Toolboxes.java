package ksu.rgn.gui;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import ksu.rgn.Main;
import ksu.rgn.arcgis.GISBridge;
import ksu.rgn.arcgis.GISFuture;
import ksu.rgn.scenario.MapLocation;
import ksu.rgn.scenario.MapNode;
import ksu.rgn.scenario.Scenario;
import ksu.rgn.scenario.Truck;
import ksu.rgn.utils.Tuple2;
import ksu.rgn.utils.Units;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    private static Button addButton(Pane to, String name, Runnable onClick) {
        final Button b = new Button(name);
        if (onClick != null) {
            b.setOnAction(ae -> onClick.run());
        }
        to.getChildren().add(b);

        return b;
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


    public static Node createScenarioToolbox(Scenario s, Window owner) {
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

    public static Node createNodesToolbox(Scenario s, Window owner) {
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

        final VBox[] nodeListEnc = new VBox[1];

        addButton(pane, "Import nodes from .csv", () -> {
            final FileChooser fc = new FileChooser();
            fc.setTitle("Import nodes from .csv");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(".csv files", "*.csv"));
            final File f = fc.showOpenDialog(owner);
            if (f != null) {
                try {
                    final String[] lines = Files.lines(Paths.get(f.toURI())).limit(5).toArray(String[]::new);

                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.WINDOW_MODAL);

                    final VBox dialog = createPane("Import from .csv");
                    dialog.setPrefWidth(600);
                    final Label l = new Label(f.getAbsolutePath());
                    l.setPadding(new Insets(0, 0, 5, 0));
                    dialog.getChildren().add(l);
                    addVSpace(dialog, 5);
                    final boolean[] firstLineHeader = new boolean[]{true};
                    addCheckboxF(dialog, "First line is a header", true, b -> {
                        firstLineHeader[0] = b;
                    });
                    addVSpace(dialog, 5);

                    int cols = 0;
                    for (String line : lines) {
                        cols = Math.max(cols, line.split(",").length);
                    }

                    final TableView<List<String>> table = new TableView<>();
                    table.setEditable(false);
                    table.setPrefHeight(150);
                    for (int i = 0; i < cols; i++) {
                        final TableColumn<List<String>, String> col = new TableColumn<>(Integer.toString(i + 1));
                        final int colIndex = i ;
                        col.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(colIndex)));
                        table.getColumns().add(col);
                    }

                    final List<List<String>> data = new ArrayList<>();
                    for (String line : lines) {
                        final String[] fields = line.split(",");
                        final List<String> row = new ArrayList<>();
                        Collections.addAll(row, fields);
                        for (int j = fields.length; j < cols; j++) {
                            row.add("");
                        }
                        data.add(row);
                    }

                    table.setItems(FXCollections.observableArrayList(data));
                    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                    dialog.getChildren().add(table);
                    addVSpace(dialog, 10);

                    final String[] properties = new String[] {
                            "Name", "GPS Latitude", "GPS Longitude", "Demand [pounds]", "Supply [pounds]"
                    };
                    final int[] propertyCols = new int[properties.length];

                    int selectedId = 0;
                    for (int i = 0; i < properties.length; i++) {
                        final int propertyI = i;
                        final String property = properties[i];

                        final HBox line = new HBox(0);
                        final Text pText = new Text(property);
                        pText.setStyle("-fx-font-weight: bold;");
                        line.getChildren().add(new TextFlow(new Text("Column for - "), pText, new Text(": ")));
                        final Pane hSpacer = new Pane();
                        HBox.setHgrow(hSpacer, Priority.ALWAYS);
                        line.getChildren().add(hSpacer);

                        final ToggleGroup group = new ToggleGroup();

                        for (int j = 0; j < cols; j++) {
                            final int colI = j;
                            ToggleButton button = new ToggleButton(Integer.toString(j + 1));
                            button.setToggleGroup(group);

                            button.setStyle("-fx-background-radius: 0; -fx-padding: 4px 10px 4px 10px;");
                            if (j == 0) {
                                button.setStyle("-fx-background-radius: 3 0 0 3; -fx-padding: 4px 10px 4px 10px;");
                            } else if (j == cols - 1) {
                                button.setStyle("-fx-background-radius: 0 3 3 0; -fx-padding: 4px 10px 4px 10px;");
                            }
                            if (selectedId == j) {
                                button.setSelected(true);
                                propertyCols[i] = selectedId;
                            }
                            button.setOnAction(ae -> {
                                if (button.isSelected()) {
                                    propertyCols[propertyI] = colI;
                                } else {
                                    propertyCols[propertyI] = -1;
                                }
                            });
                            line.getChildren().add(button);
                        }
                        selectedId++;

                        dialog.getChildren().add(line);
                    }

                    addButton(dialog, "Ok, import", () -> {
                        try {
                            final String[] csvRecords = Files.lines(Paths.get(f.toURI())).skip(firstLineHeader[0] ? 1 : 0).toArray(String[]::new);
                            final ArrayList<MapNode> toAdd = new ArrayList<>();
                            int validCounter = 0, invalidCounter = 0;

                            for (String record : csvRecords) {
                                final String[] fields = record.split(",");

                                final MapNode node = new MapNode();
                                boolean invalidRecord = false;

                                // Name
                                if (invalidRecord || propertyCols[0] == -1 || propertyCols[0] >= fields.length) {
                                    invalidRecord = true;
                                } else {
                                    node.name = fields[propertyCols[0]];
                                }

                                // GPS Locataion
                                if (invalidRecord || propertyCols[1] == -1 || propertyCols[1] >= fields.length || propertyCols[2] == -1 || propertyCols[2] >= fields.length) {
                                    invalidRecord = true;
                                } else {
                                    try {
                                        node.location = new MapLocation(
                                                Double.parseDouble(fields[propertyCols[1]]),
                                                Double.parseDouble(fields[propertyCols[2]])
                                        );
                                    } catch (NumberFormatException nfe) {
                                        invalidRecord = true;
                                    }
                                }

                                // Demand
                                if (invalidRecord || propertyCols[3] == -1 || propertyCols[3] >= fields.length) {
                                    invalidRecord = true;
                                } else {
                                    try {
                                        node.demand = Units.toKilograms(Integer.parseInt(fields[propertyCols[3]]));
                                    } catch (NumberFormatException nfe) {
                                        invalidRecord = true;
                                    }

                                }

                                // Supply
                                if (invalidRecord || propertyCols[4] == -1 || propertyCols[4] >= fields.length) {
                                    invalidRecord = true;
                                } else {
                                    try {
                                        node.supply = Units.toKilograms(Integer.parseInt(fields[propertyCols[4]]));
                                    } catch (NumberFormatException nfe) {
                                        invalidRecord = true;
                                    }
                                }

                                if (!invalidRecord) {
                                    toAdd.add(node);
                                    validCounter ++;
                                } else {
                                    invalidCounter ++;
                                }
                            }

                            if (validCounter > 0) {
                                Main.db.persist(() -> s.getNodes().addAll(toAdd), s).finished(() -> Platform.runLater(() -> updateList(nodeListEnc[0], s, null, nodeRenderer, s::getNodes)));
                                Platform.runLater(() -> {
                                    final MapNode[] ns = s.getNodes().toArray(new MapNode[s.getNodes().size() + toAdd.size()]);
                                    for (int i = 0; i < toAdd.size(); i++) {
                                        ns[s.getNodes().size() + i] = toAdd.get(i);
                                    }
                                    updateList(nodeListEnc[0], s, ns, nodeRenderer, s::getNodes);
                                });
                            }

                            final Alert alert = new Alert(invalidCounter > 0 ? Alert.AlertType.WARNING : Alert.AlertType.INFORMATION, "Corrupted records: " + invalidCounter + "\nImported records: " + validCounter, ButtonType.OK);
                            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                            alert.show();

                        } catch (IOException e) {
                            final Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot read selected file\n" + f.getAbsolutePath(), ButtonType.OK);
                            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                            alert.show();
                        }

                        dialogStage.close();

                    }).setDefaultButton(true);

                    dialogStage.setScene(new Scene(dialog));
                    dialogStage.show();
                } catch (IOException e) {

                    final Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot read selected file\n" + f.getAbsolutePath(), ButtonType.OK);
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    alert.show();
                }
            }
        });

        addVSpace(pane, 10);

        nodeListEnc[0] = addList(pane);
        final Label addNodeL = addButtonWithLabel(addNodeForm, "Add node", () -> {
            if (nodeFormV.isValid) {

                final MapNode n = new MapNode();

                n.name = (String) nodeFormV.vars.get("Name");
                n.location = (MapLocation) nodeFormV.vars.get("Location");

                n.demand = Units.toKilograms((Integer) nodeFormV.vars.get("Demand"));
                n.supply = Units.toKilograms((Integer) nodeFormV.vars.get("Supply"));

                Main.db.persist(() -> s.add(n), s).finished(() -> Platform.runLater(() -> updateList(nodeListEnc[0], s, null, nodeRenderer, s::getNodes)));
                Platform.runLater(() -> {
                    final MapNode[] ns = s.getNodes().toArray(new MapNode[s.getNodes().size() + 1]);
                    ns[ns.length - 1] = n;
                    updateList(nodeListEnc[0], s, ns, nodeRenderer, s::getNodes);
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

        updateList(nodeListEnc[0], s, null, nodeRenderer, s::getNodes);

        addButton(pane, "Remove all nodes", () -> {
            Main.db.persist(() -> s.getNodes().clear(), s).finished(() -> Platform.runLater(() -> updateList(nodeListEnc[0], s, null, nodeRenderer, s::getNodes)));
            Platform.runLater(() -> updateList(nodeListEnc[0], s, new MapNode[]{}, nodeRenderer, s::getNodes));
        });

        return pane;
    }

    private static void updateList(VBox list, Scenario s, Object[] future, Function<Tuple2<Object, Button>, HBox> renderer, Supplier<List> getUpToDateData) {
        list.getChildren().clear();

        final Node spinner = ksu.rgn.gui.Window.createSpinner();
        addVSpace(list, 5);
        list.getChildren().add(spinner);
        addVSpace(list, 5);

        if (future == null) {
            Main.db.preCache(() -> {
                final Object[] data = getUpToDateData.get().toArray(new Object[0]);

                Platform.runLater(() -> updateList(list, s, data, renderer, getUpToDateData));
            });
        } else {

            list.getChildren().clear();
            for (Object t : future) {
                final Button goB = new Button("", new ImageView(Icons._24.TRASH));
                goB.setPadding(new Insets(5, 8, 5, 8));
                goB.setOnAction(e -> {
                    Main.db.persist(() -> getUpToDateData.get().remove(t), s).finished(() -> Platform.runLater(() -> updateList(list, s, null, renderer, getUpToDateData)));
                    Platform.runLater(() -> {
                        final Object[] deleted = new Object[future.length - 1];
                        int skip = 0;
                        for (int i = 0; i < deleted.length; i++) {
                            if (future[i + skip] == t) skip++;
                            deleted[i] = future[i + skip];
                        }

                        updateList(list, s, deleted, renderer, getUpToDateData);
                    });
                });

                list.getChildren().add(renderer.apply(new Tuple2<>(t, goB)));
            }
        }

    }

    public static Node createTrucksToolbox(Scenario s, Window owner) {
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

    public static Node createRoutesToolbox(Scenario s, Window owner) {
        final VBox pane = createPane("Routes");

        addButton(pane, "Calculate", null);

        return pane;
    }

    public static Node createMapView(Scenario s, Window owner) {
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
