package ksu.rgn.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ksu.rgn.Main;
import ksu.rgn.scenario.Scenario;

import java.util.Optional;

import static ksu.rgn.gui.Window.STYLE_BACKGROUND_COLOR;
import static ksu.rgn.gui.Window.createSpinner;

/**
 *
 */
public class ScenarioList {


    static Node createScenarioSelectPanel(Window w) {
        Button openB = new Button("", new ImageView(Icons._24.OPEN_FOLDER));
        Button addNewB = new Button("", new ImageView(Icons._24.ADD_NEW));
        openB.setStyle("-fx-background-radius: 3 0 0 3");
        addNewB.setStyle("-fx-background-radius: 0 3 3 0");

        w.selectedScenarioL.setText("");
        w.nameSelectedScenarioL.setText("");
        w.nameSelectedScenarioL.setStyle("-fx-font-weight: bold");

        openB.setOnAction(e -> showScenarioList(w));
        addNewB.setOnAction(e -> {
            final Scenario sc = addNewScenario();
            if (sc != null) reloadScenario(sc, w);
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(w.selectedScenarioL, w.nameSelectedScenarioL);
        vBox.setPadding(new Insets(0, 0, 0, 10));
        HBox hBox = new HBox(0);
        hBox.getChildren().addAll(openB, addNewB, vBox);
        return hBox;
    }

    private static void showScenarioList(Window w) {
        Stage stage = new Stage();
        stage.setMinWidth(200);
        stage.setMinHeight(300);
        stage.setWidth(400);
        stage.setHeight(600);
        BorderPane layout = new BorderPane();

        final HBox bottomBarP = new HBox();
        bottomBarP.setPadding(new Insets(6, 15, 6, 15));
        bottomBarP.setSpacing(10);
        bottomBarP.setStyle("-fx-background-color:" +  STYLE_BACKGROUND_COLOR + ";");

        final Button addNewB = new Button("Create", new ImageView(Icons._24.ADD_NEW));
        final Button cancelB = new Button("Cancel", new ImageView(Icons._24.CROSS));

        cancelB.setOnAction(e -> stage.close());
        addNewB.setOnAction(e -> {
            final Scenario sc = addNewScenario();
            if (sc != null) refreshScenarioList(stage, w, layout, sc);
        });

        Pane hGrow = new Pane();
        HBox.setHgrow(hGrow, Priority.ALWAYS);
        bottomBarP.getChildren().addAll(hGrow, addNewB, cancelB);

        layout.setBottom(bottomBarP);

        stage.setScene(new Scene(layout));
        stage.setTitle("Select scenario");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(w.nameSelectedScenarioL.getScene().getWindow());
        stage.show();

        refreshScenarioList(stage, w, layout, null);
    }
    private static void refreshScenarioList(Stage stage, Window w, BorderPane layout, Scenario ss) {
        layout.setCenter(createSpinner());
        Main.db.getAllScenarios(scenarios -> Platform.runLater(() -> {
            if (!scenarios.isEmpty()) {
                final ListView<Scenario> list = new ListView<>();

                list.setCellFactory(l -> new ListCell<Scenario>() {
                    @Override
                    public void updateItem(Scenario s, boolean empty) {
                        super.updateItem(s, empty);
                        final HBox row = new HBox(5);

                        if (s != null) {
                            final Button goB = new Button("", new ImageView(Icons._24.GO_RIGHT));
                            goB.setPadding(new Insets(5, 8, 5, 8));
                            goB.setOnAction(e -> {
                                reloadScenario(s, w);
                                stage.close();
                            });

                            final Pane hGrow = new Pane();
                            HBox.setHgrow(hGrow, Priority.ALWAYS);

                            final Label nameL = new Label(s.name);
                            nameL.setStyle("-fx-font-weight: bold;");

                            final Label descL = new Label("No description");
                            if (s.description != null && !s.description.isEmpty()) {
                                int end = s.description.indexOf('\n');
                                if (end == -1) end = s.description.length();
                                descL.setText(s.description.substring(0, end));
                            } else {
                                descL.setStyle("-fx-font-style: italic;");
                            }
                            descL.setTextOverrun(OverrunStyle.ELLIPSIS);


                            final VBox labelsG = new VBox(0);
                            labelsG.getChildren().addAll(nameL, descL);

                            row.getChildren().addAll(
                                    labelsG,
                                    hGrow,
                                    goB
                            );
                            row.setMaxWidth(layout.getWidth() - 20);
                            setGraphic(row);
                        }
                    }
                });

                list.setItems(FXCollections.observableList(scenarios));
                list.setEditable(false);
                list.setFixedCellSize(34 + 2 * 5 + 1);
                Platform.runLater(() -> {
                    if (ss != null || w.selectedScenario != null) {
                        final int i = scenarios.indexOf(ss != null ? ss : w.selectedScenario);

                        if (i != -1) {
                            list.getSelectionModel().select(i);
                            list.scrollTo(i);
                        }
                    }
                });

                list.setOnMouseClicked(e -> {
                    if (e.getClickCount() == 2) {
                        final Scenario s = list.getSelectionModel().getSelectedItem();
                        if (s != null) {
                            reloadScenario(s, w);
                            stage.close();
                        }
                    }
                });
                list.setOnKeyPressed(e -> {
                    if (e.getCode() == KeyCode.ENTER) {
                        final Scenario s = list.getSelectionModel().getSelectedItem();
                        if (s != null) {
                            reloadScenario(s, w);
                            stage.close();
                        }
                    }
                });

                layout.setCenter(list);
            } else {
                layout.setCenter(new Label("No scenarios found"));
            }
        }));
    }

    private static void reloadScenario(Scenario scenario, Window w) {
        w.selectedScenarioL.setText("Selected:");
        w.nameSelectedScenarioL.setText(scenario.name);
        w.selectedScenario = scenario;

        final ToggleGroup group = new ToggleGroup();
        ToggleButton scenarioB = new ToggleButton("Scenario", new ImageView(Icons._24.WAYPOINT_MAP));
        ToggleButton nodesB = new ToggleButton("Nodes", new ImageView(Icons._24.GROCERY_STORE));
        ToggleButton trucksB = new ToggleButton("Trucks", new ImageView(Icons._24.TRUCK));
        ToggleButton routesB = new ToggleButton("Routes", new ImageView(Icons._24.ROUTE));

        scenarioB.setToggleGroup(group);
        nodesB.setToggleGroup(group);
        trucksB.setToggleGroup(group);
        routesB.setToggleGroup(group);

        scenarioB.setStyle("-fx-background-radius: 3 0 0 3");
        nodesB.setStyle("-fx-background-radius: 0");
        trucksB.setStyle("-fx-background-radius: 0");
        routesB.setStyle("-fx-background-radius: 0 3 3 0");

        scenarioB.setOnAction(e -> {
            MapView.current().selectPoint(null);
            w.border.setLeft(scenarioB.isSelected() ? createToolbox(ScenarioToolbox.create(w.selectedScenario, w.border.getScene().getWindow())) : null);
        });
        nodesB.setOnAction(e -> {
            MapView.current().selectPoint(null);
            w.border.setLeft(nodesB.isSelected() ? createToolbox(NodesToolbox.create(w.selectedScenario, w.border.getScene().getWindow())) : null);
        });
        trucksB.setOnAction(e -> {
            MapView.current().selectPoint(null);
            w.border.setLeft(trucksB.isSelected() ? createToolbox(TrucksToolbox.create(w.selectedScenario, w.border.getScene().getWindow())) : null);
        });
        routesB.setOnAction(e -> {
            MapView.current().selectPoint(null);
            w.border.setLeft(routesB.isSelected() ? createToolbox(RoutesToolbox.create(w.selectedScenario, w.border.getScene().getWindow())) : null);
        });

        scenarioB.setSelected(true);
        w.border.setLeft(createToolbox(ScenarioToolbox.create(w.selectedScenario, w.border.getScene().getWindow())));

        HBox hBox = new HBox(0);
        hBox.getChildren().addAll(scenarioB, nodesB, trucksB, routesB);
        if (w.topBarP.getChildren().size() == 4) {
            w.topBarP.getChildren().remove(0, 1);
        }
        w.topBarP.getChildren().add(0, hBox);

        w.border.setCenter(MapView.create(w.selectedScenario, w.border.getScene().getWindow()));
        MapView.current().selectPoint(null);
    }

    private static Scenario addNewScenario() {
        final TextInputDialog input = new TextInputDialog("New #" + (int) (Math.random() * 899999 + 100000));
        input.setTitle("New scenario");
        input.setContentText("Name:");
        input.setHeaderText("");

        final Optional<String> name = input.showAndWait();

        if (name.isPresent()) {
            final Scenario s = new Scenario(name.get(), null);
            Main.db.persist(null, s);
            return s;
        } else {
            return null;
        }
    }

    private static Node createToolbox(Node toolbox) {
        final ScrollPane sp = new ScrollPane(toolbox);
        sp.setFitToWidth(true);
        sp.getStyleClass().add("edge-to-edge");

        return sp;
    }

}
