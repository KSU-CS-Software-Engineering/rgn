package ksu.rgn.gui;


import javafx.application.Application;
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

import java.util.ArrayList;
import java.util.Optional;

/**
 *
 */
public class Window extends Application {

    private static final String STYLE_BACKGROUND_COLOR = "#F4F4F4";
    private static final String STYLE_LOW_CONTRAST_TEXT_COLOR = "#999999";


    private BorderPane border;
    private Scenario selectedScenario = null;

    @Override
    public void start(Stage stage) {
        border = new BorderPane();

        border.setTop(createTopBar());

        border.setCenter(createConnectToDBBody());

        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setTitle("Rural grocery network optimizer");
        stage.setWidth(960);
        stage.setHeight(720);
        stage.setMinWidth(400);
        stage.setMinHeight(300);
        stage.show();
    }

    private HBox connectionP, topBarP;
    private Node createTopBar() {
        topBarP = new HBox();
        topBarP.setPadding(new Insets(6, 15, 6, 15));
        topBarP.setSpacing(10);
        topBarP.setStyle("-fx-background-color:" +  STYLE_BACKGROUND_COLOR + ";");

        connectionP = new HBox(15);
        Button connectB = new Button("Connect to database", new ImageView(Icons._24.DISCONNECTED));
        connectB.setOnAction(e -> onConnectedToDB("user@mockdb:1234"));

        Pane hGrow = new Pane();
        HBox.setHgrow(hGrow, Priority.ALWAYS);

        connectionP.getChildren().addAll(connectB);

        topBarP.getChildren().addAll(hGrow, connectionP);

        return topBarP;
    }

    public void onConnectedToDB(String uri) {
        ImageView iv = new ImageView(Icons._32.CONNECTED);
        Label connectedL = new Label("Connected");
        Label dbL = new Label(uri);
        dbL.setStyle("-fx-font-weight: bold");
        VBox ls = new VBox();
        ls.getChildren().addAll(connectedL, dbL);
        Button disconnectB = new Button("", new ImageView(Icons._24.CROSS));

        disconnectB.setOnAction(e -> onDisconnectedFromDB());

        connectionP.getChildren().clear();
        connectionP.getChildren().addAll(iv, ls, disconnectB);
        Pane hGrow = new Pane();
        HBox.setHgrow(hGrow, Priority.ALWAYS);
        topBarP.getChildren().clear();
        topBarP.getChildren().addAll(createScenarioSelectPanel(), hGrow, connectionP);
    }
    public void onDisconnectedFromDB() {
        Button reconnectB = new Button("Connect to database", new ImageView(Icons._24.DISCONNECTED));
        reconnectB.setOnAction(e -> onConnectedToDB("user@mockdb:1234"));

        border.setCenter(createConnectToDBBody());

        connectionP.getChildren().clear();
        connectionP.getChildren().addAll(reconnectB);
        Pane hGrow = new Pane();
        HBox.setHgrow(hGrow, Priority.ALWAYS);
        topBarP.getChildren().clear();
        topBarP.getChildren().addAll(hGrow, connectionP);
    }

    private Label selectedScenarioL, nameSelectedScenarioL;
    private Node createScenarioSelectPanel() {
        Button openB = new Button("", new ImageView(Icons._24.OPEN_FOLDER));
        Button addNewB = new Button("", new ImageView(Icons._24.ADD_NEW));
        openB.setStyle("-fx-background-radius: 3 0 0 3");
        addNewB.setStyle("-fx-background-radius: 0 3 3 0");

        selectedScenarioL = new Label("");
        nameSelectedScenarioL = new Label("");
        nameSelectedScenarioL.setStyle("-fx-font-weight: bold");

        openB.setOnAction(e -> showScenarioList());
        addNewB.setOnAction(e -> {
            final Scenario sc = addNewScenario();
            if (sc != null) reloadScenario(sc);
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(selectedScenarioL, nameSelectedScenarioL);
        vBox.setPadding(new Insets(0, 0, 0, 10));
        HBox hBox = new HBox(0);
        hBox.getChildren().addAll(openB, addNewB, vBox);
        return hBox;
    }

    private void reloadScenario(Scenario scenario) {
        selectedScenarioL.setText("Selected:");
        nameSelectedScenarioL.setText(scenario.name);
        selectedScenario = scenario;


        final ToggleGroup group = new ToggleGroup();
        ToggleButton scenarioB = new ToggleButton("Scenario", new ImageView(Icons._24.WAYPOINT_MAP));
        ToggleButton nodesB = new ToggleButton("Nodes", new ImageView(Icons._24.GROCERY_STORE));
        ToggleButton trucksB = new ToggleButton("Trucks", new ImageView(Icons._24.TRUCK));
        ToggleButton routesB = new ToggleButton("Routes", new ImageView(Icons._24.ROUTE));

        scenarioB.setToggleGroup(group);
        nodesB.setToggleGroup(group);
        trucksB.setToggleGroup(group);
        routesB.setToggleGroup(group);

        scenarioB.setSelected(true);

        scenarioB.setStyle("-fx-background-radius: 3 0 0 3");
        nodesB.setStyle("-fx-background-radius: 0");
        trucksB.setStyle("-fx-background-radius: 0");
        routesB.setStyle("-fx-background-radius: 0 3 3 0");

        HBox hBox = new HBox(0);
        hBox.getChildren().addAll(scenarioB, nodesB, trucksB, routesB);
        if (topBarP.getChildren().size() == 4) {
            topBarP.getChildren().remove(0, 1);
        }
        topBarP.getChildren().add(0, hBox);

        border.setCenter(createMapView());
    }

    private void showScenarioList() {
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
            if (sc != null) refreshScenarioList(stage, layout, sc);
        });

        Pane hGrow = new Pane();
        HBox.setHgrow(hGrow, Priority.ALWAYS);
        bottomBarP.getChildren().addAll(hGrow, addNewB, cancelB);

        layout.setBottom(bottomBarP);

        stage.setScene(new Scene(layout));
        stage.setTitle("Select scenario");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(topBarP.getScene().getWindow());
        stage.show();

        refreshScenarioList(stage, layout, null);
    }
    private void refreshScenarioList(Stage stage, BorderPane layout, Scenario ss) {
        final ArrayList<Scenario> scenarios = Main.getDBQueries().getAllScenarios();

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
                            reloadScenario(s);
                            stage.close();
                        });

                        final Pane hGrow = new Pane();
                        HBox.setHgrow(hGrow, Priority.ALWAYS);

                        final Label nameL = new Label(s.name);
                        nameL.setStyle("-fx-font-weight: bold;");

                        final Label descL = new Label("No description");
                        if (s.description != null && !s.description.isEmpty()) {
                            descL.setText(s.description);
                        } else {
                            descL.setStyle("-fx-font-style: italic;");
                        }

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

            list.setItems(FXCollections.observableList(scenarios));
            list.setEditable(false);
            list.setFixedCellSize(34 + 2 * 5 + 1);
            Platform.runLater(() -> {
                if (ss != null) {
                    final int i = scenarios.indexOf(ss);

                    if (i != -1) {
                        list.getSelectionModel().select(i);
                        list.scrollTo(i);
                    }
                } else {
                    if (selectedScenario != null) {
                        final int i = scenarios.indexOf(selectedScenario);

                        if (i != -1) {
                            list.getSelectionModel().select(i);
                            list.scrollTo(i);
                        }
                    }
                }
            });

            list.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    final Scenario s = list.getSelectionModel().getSelectedItem();
                    if (s != null) {
                        reloadScenario(s);
                        stage.close();
                    }
                }
            });
            list.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) {
                    final Scenario s = list.getSelectionModel().getSelectedItem();
                    if (s != null) {
                        reloadScenario(s);
                        stage.close();
                    }
                }
            });

            layout.setCenter(list);
        } else {
            layout.setCenter(new Label("No scenarios found"));
        }
    }

    private Scenario addNewScenario() {
        final TextInputDialog input = new TextInputDialog("New #" + Integer.toString((int)(Math.random() * 899999 + 100000)));
        input.setTitle("New scenario");
        input.setContentText("Name:");
        input.setHeaderText("");

        final Optional<String> name = input.showAndWait();

        if (name.isPresent()) {
            final Scenario s = new Scenario(name.get(), null);
            Main.getDBQueries().persistScenario(s);
            return s;
        } else {
            return null;
        }
    }

    private Node createConnectToDBBody() {
        BorderPane p = new BorderPane();
        p.setStyle("-fx-background-color:" + STYLE_BACKGROUND_COLOR + ";");
        Label l = new Label("Connect to database and select scenario");
        l.setStyle("-fx-text-fill:" + STYLE_LOW_CONTRAST_TEXT_COLOR + ";");
        p.setCenter(l);

        return p;
    }

    private Node createMapView() {
        BorderPane p = new BorderPane();
        p.setStyle("-fx-background-color:white;");
        Label l = new Label("This is a map");
        l.setStyle("-fx-text-fill:black;");
        p.setCenter(l);

        return p;
    }

}
