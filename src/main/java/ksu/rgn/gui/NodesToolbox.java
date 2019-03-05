package ksu.rgn.gui;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import ksu.rgn.Main;
import ksu.rgn.scenario.MapNode;
import ksu.rgn.scenario.Scenario;
import ksu.rgn.utils.Tuple2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ksu.rgn.gui.Toolboxes.*;

/**
 *
 */
public class NodesToolbox {

    public static Node create(Scenario s, Window owner) {

        final VBox pane = createPane("Nodes");

        final Pane addNodeForm = addForm(pane);
        final Toolboxes.FormValues nodeFormV = new Toolboxes.FormValues();

        addTextF(addNodeForm, "Name", "", nodeFormV.listenF("Name", ""));
        addGpsF(addNodeForm, "Location", 0, 0, nodeFormV.listenF("Location", new Tuple2<>(0.0, 0.0)));
        addVSpace(addNodeForm, 5);
        addNumberF(addNodeForm, "Demand", "palettes", 0, nodeFormV.listenF("Demand", 0));
        addNumberF(addNodeForm, "Supply", "palettes", 0, nodeFormV.listenF("Supply", 0));
        addVSpace(addNodeForm, 10);

        final UpdatableList[] nodeListEnc = new UpdatableList[] { null };
        addButton(pane, "Import nodes from .csv", () -> displayImportDialog(s, owner, nodeListEnc[0]));

        addVSpace(pane, 10);

        final UpdatableList nodeList = addUpdatableList(pane, UpdatableList.createTwoLineRenderer(
                n -> "Node - " + ((MapNode)n).name,
                n -> ((MapNode)n).demand + " / " + ((MapNode)n).supply + " palettes"
        ));
        nodeListEnc[0] = nodeList;
        final Label addNodeL = addButtonWithLabel(addNodeForm, "Add node", () -> {
            if (nodeFormV.isValid) {

                final MapNode n = new MapNode();

                n.name = (String) nodeFormV.vars.get("Name");
                final Tuple2<Double, Double> loc = (Tuple2<Double, Double>) nodeFormV.vars.get("Location");
                n.gpsLat = loc._1;
                n.gpsLon = loc._2;

                n.demand = (Integer) nodeFormV.vars.get("Demand");
                n.supply = (Integer) nodeFormV.vars.get("Supply");

                Main.db.persist(() -> s.add(n), s).onFinish(o -> Platform.runLater(() -> nodeList.updateList(s, null, s::getNodes)));
                Platform.runLater(() -> {
                    final MapNode[] ns = s.getNodes().toArray(new MapNode[s.getNodes().size() + 1]);
                    ns[ns.length - 1] = n;
                    nodeList.updateList(s, ns, s::getNodes);
                });

            }
        });
        addNodeL.setStyle("-fx-text-fill: red; -fx-padding: 5px 0px 0px 5px;");
        nodeFormV.isValidLabel = addNodeL;

        addButton(pane, "Remove all nodes", () -> {
            Main.db.persist(() -> s.getNodes().clear(), s).onFinish(o -> Platform.runLater(() -> nodeList.updateList(s, null, s::getNodes)));
            Platform.runLater(() -> nodeList.updateList(s, new MapNode[]{}, s::getNodes));
        });

        nodeList.updateList(s, null, s::getNodes);
        nodeList.addOnChangedListener(() -> MapView.current().updateMarkers(s.getNodes()));
        MapView.current().updateMarkers(s.getNodes());

        return pane;
    }

    private static void displayImportDialog(Scenario s, Window owner, UpdatableList nodeList) {
        final FileChooser fc = new FileChooser();
        fc.setTitle("Import nodes from .csv");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(".csv files", "*.csv"));
        final File f = fc.showOpenDialog(owner);
        if (f != null) {
            try {
                final String[] lines = Files.lines(Paths.get(f.toURI())).limit(5).toArray(String[]::new);
                final Label errorMessageL;

                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.WINDOW_MODAL);

                final VBox dialog = createPane("Import from .csv");
                dialog.setPrefWidth(600);
                final Label l = new Label(f.getAbsolutePath());
                l.setPadding(new Insets(0, 0, 5, 0));
                dialog.getChildren().add(l);
                addVSpace(dialog, 5);
                final boolean[] firstLineHeader = new boolean[]{true};
                addCheckboxF(dialog, "First line is a header", true, b -> firstLineHeader[0] = b);
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
                        "Name", "GPS Latitude", "GPS Longitude", "Demand [palettes]", "Supply [palettes]"
                };
                final int[] propertyCols = new int[properties.length];

                final Label importErrorLabel = new Label();
                importErrorLabel.setStyle("-fx-text-fill: red; -fx-padding: 5px 0px 0px 5px;");
                final Button importButton = new Button("Ok, import");
                importButton.setDefaultButton(true);
                importButton.setOnAction(ae -> {
                    try {
                        final String[] csvRecords = Files.lines(Paths.get(f.toURI())).skip(firstLineHeader[0] ? 1 : 0).toArray(String[]::new);
                        final ArrayList<MapNode> toAdd = new ArrayList<>();
                        int validCounter = 0, invalidCounter = 0;

                        for (String record : csvRecords) {
                            final String[] fields = record.split(",");

                            final MapNode node = new MapNode();
                            boolean invalidRecord = false;

                            // Name
                            if (propertyCols[0] == -1 || propertyCols[0] >= fields.length) {
                                invalidRecord = true;
                            } else {
                                node.name = fields[propertyCols[0]];
                            }

                            // GPS Locataion
                            if (invalidRecord || propertyCols[1] == -1 || propertyCols[1] >= fields.length || propertyCols[2] == -1 || propertyCols[2] >= fields.length) {
                                invalidRecord = true;
                            } else {
                                try {
                                    node.gpsLat = Double.parseDouble(fields[propertyCols[1]]);
                                    node.gpsLon = Double.parseDouble(fields[propertyCols[2]]);

                                } catch (NumberFormatException nfe) {
                                    invalidRecord = true;
                                }
                            }

                            // Demand
                            if (invalidRecord || propertyCols[3] == -1 || propertyCols[3] >= fields.length) {
                                invalidRecord = true;
                            } else {
                                try {
                                    node.demand = Integer.parseInt(fields[propertyCols[3]]);
                                } catch (NumberFormatException nfe) {
                                    invalidRecord = true;
                                }

                            }

                            // Supply
                            if (invalidRecord || propertyCols[4] == -1 || propertyCols[4] >= fields.length) {
                                invalidRecord = true;
                            } else {
                                try {
                                    node.supply = Integer.parseInt(fields[propertyCols[4]]);
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
                            Main.db.persist(() -> s.getNodes().addAll(toAdd), s).onFinish(o -> Platform.runLater(() -> {
                                nodeList.updateList(s, null, s::getNodes);
                                MapView.current().updateMarkers(s.getNodes());
                            }));
                            Platform.runLater(() -> {
                                final MapNode[] ns = s.getNodes().toArray(new MapNode[s.getNodes().size() + toAdd.size()]);
                                for (int i = 0; i < toAdd.size(); i++) {
                                    ns[s.getNodes().size() + i] = toAdd.get(i);
                                }
                                nodeList.updateList(s, ns, s::getNodes);
                                MapView.current().updateMarkers(s.getNodes());
                            });
                        }

                        final Alert alert = new Alert(invalidCounter > 0 ? Alert.AlertType.WARNING : Alert.AlertType.INFORMATION, "Corrupted records: " + invalidCounter + "\nImported records: " + validCounter, ButtonType.OK);
                        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                        alert.show();

                    } catch (IOException e) {
                        showIOEDialog(f);
                    }

                    dialogStage.close();
                });
                final HBox importBox = new HBox(3);
                importBox.getChildren().addAll(importButton, importErrorLabel);

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
                                button.setSelected(true);
                            }
                            checkValidColumnSelection(propertyCols, dialog, importErrorLabel);
                        });
                        line.getChildren().add(button);
                    }
                    selectedId++;

                    dialog.getChildren().add(line);
                }

                dialog.getChildren().add(importBox);

                dialogStage.setScene(new Scene(dialog));
                dialogStage.show();
            } catch (IOException e) {
                showIOEDialog(f);
            }
        }

    }

    private static void checkValidColumnSelection(int [] propertyCols, VBox dialog, Label errorLabel) {
        boolean valid = true;
        int i = 0;
        int [] comparePropertyCols = Arrays.copyOf(propertyCols, propertyCols.length);
        while (valid && i < propertyCols.length) {
            int j = 0;
            while (valid && j < comparePropertyCols.length) {
                if (    (i != j) &&
                        (propertyCols[i] == comparePropertyCols[j])) {
                    valid = false;
                }
                j++;
            }
            i++;
        }

        ObservableList<Node> nodeList = dialog.getChildren();
        for(Node node : nodeList) {
            if(node instanceof HBox) {
                ObservableList<Node> hNodeList = ((HBox) node).getChildren();
                for(Node hNode : hNodeList) {
                    if(hNode instanceof Button) {
                        Button b = (Button)hNode;
                        if(b.getText() == "Ok, import") {
                            if (valid) {
                                b.setDisable(false);
                                errorLabel.setText("");
                            } else {
                                b.setDisable(true);
                                errorLabel.setText("Duplicate columns selected for same properties.");
                            }
                        }
                    }
                }
            }
        }
    }

    private static void showIOEDialog(File f) {
        final Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot read selected file\n" + f.getAbsolutePath(), ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }
}
