package ksu.rgn.gui;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import ksu.rgn.Main;
import ksu.rgn.db.MockDatabase;
import ksu.rgn.db.MySQLDatabase;
import ksu.rgn.scenario.Scenario;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static ksu.rgn.gui.Toolboxes.*;

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
        onDisconnectedFromDB();
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

        disconnectB.setOnAction(e -> {
            Main.db.close(() -> Platform.runLater(() -> {
                Main.db = null;
                onDisconnectedFromDB();
            }));

            border.setCenter(createConnectToDBBody());
            border.setLeft(null);

            connectionP.getChildren().clear();
            final Label l = new Label("Finishing up ");
            l.setPadding(new Insets(3));
            connectionP.getChildren().addAll(l, createSpinner());
            Pane hGrow = new Pane();
            HBox.setHgrow(hGrow, Priority.ALWAYS);
            topBarP.getChildren().clear();
            topBarP.getChildren().addAll(hGrow, connectionP);
        });

        connectionP.getChildren().clear();
        connectionP.getChildren().addAll(iv, ls, disconnectB);
        Pane hGrow = new Pane();
        HBox.setHgrow(hGrow, Priority.ALWAYS);
        topBarP.getChildren().clear();
        topBarP.getChildren().addAll(createScenarioSelectPanel(), hGrow, connectionP);
    }
    public void onDisconnectedFromDB() {
        Button reconnectB = new Button("Connect to database", new ImageView(Icons._24.DISCONNECTED));
        reconnectB.setOnAction(e -> {
            if (Main.db != null) {
                Main.db.close(null);
            }
            showDbInfoWindow();
        });

        border.setCenter(createConnectToDBBody());
        border.setLeft(null);

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

        scenarioB.setStyle("-fx-background-radius: 3 0 0 3");
        nodesB.setStyle("-fx-background-radius: 0");
        trucksB.setStyle("-fx-background-radius: 0");
        routesB.setStyle("-fx-background-radius: 0 3 3 0");

        scenarioB.setOnAction(e -> border.setLeft(scenarioB.isSelected() ? createToolbox(createScenarioToolbox(selectedScenario, border.getScene().getWindow())) : null));
        nodesB.setOnAction(e -> border.setLeft(nodesB.isSelected() ? createToolbox(createNodesToolbox(selectedScenario, border.getScene().getWindow())) : null));
        trucksB.setOnAction(e -> border.setLeft(trucksB.isSelected() ? createToolbox(createTrucksToolbox(selectedScenario, border.getScene().getWindow())) : null));
        routesB.setOnAction(e -> border.setLeft(routesB.isSelected() ? createToolbox(createRoutesToolbox(selectedScenario, border.getScene().getWindow())) : null));

        scenarioB.setSelected(true);
        border.setLeft(createToolbox(createScenarioToolbox(selectedScenario, border.getScene().getWindow())));

        HBox hBox = new HBox(0);
        hBox.getChildren().addAll(scenarioB, nodesB, trucksB, routesB);
        if (topBarP.getChildren().size() == 4) {
            topBarP.getChildren().remove(0, 1);
        }
        topBarP.getChildren().add(0, hBox);

        border.setCenter(createMapView(selectedScenario, border.getScene().getWindow()));
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
        }));
    }

    private void showDbInfoWindow() {
        Stage stage = new Stage();

        BorderPane root = new BorderPane();

        stage.setScene(new Scene(root));
        stage.setTitle("Database connection");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(border.getScene().getWindow());

        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(10));

        Label title = new Label("Database login credentials");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 1.2em; -fx-padding: 0 0 25px 0;");
        vbox.getChildren().add(title);

        final TextField usernameF = new TextField();
        final TextField passwordF = new PasswordField();
        final TextField databaseF = new TextField();
        final TextField urlF = new TextField();

        try {

            BufferedReader br = new BufferedReader(new FileReader(new File("dbcredentials.last")));
            usernameF.setText(br.readLine());
            passwordF.setText(br.readLine());
            databaseF.setText(br.readLine());
            urlF.setText(br.readLine());
            br.close();
        } catch (IOException ignored) {}


        vbox.getChildren().add(new Label("Username:"));
        vbox.getChildren().add(usernameF);
        vbox.getChildren().add(new Label("Password:"));
        vbox.getChildren().add(passwordF);
        vbox.getChildren().add(new Label("Database name:"));
        vbox.getChildren().add(databaseF);
        vbox.getChildren().add(new Label("Server url:"));
        vbox.getChildren().add(urlF);

        HBox hbox = new HBox(5);
        hbox.setPadding(new Insets(10));

        Pane stretch = new Pane();
        HBox.setHgrow(stretch, Priority.ALWAYS);
        Button connectB = new Button("Connect", new ImageView(Icons._24.CHECK));
        connectB.setDefaultButton(true);

        Button cancelB = new Button("Cancel", new ImageView(Icons._24.CROSS));
        cancelB.setOnAction(ae -> stage.close());

        HBox statusBox = new HBox(5);
        statusBox.setPadding(new Insets(10));

        connectB.setOnAction(ae -> {
            String url = urlF.getText();
            String dbName = databaseF.getText();
            String user = usernameF.getText();
            String password = passwordF.getText();

            try {
                FileOutputStream fos = new FileOutputStream(new File("dbcredentials.last"));
                String s = user + System.lineSeparator() +
                        password + System.lineSeparator() +
                        dbName + System.lineSeparator() +
                        url + System.lineSeparator();
                fos.write(s.getBytes(StandardCharsets.UTF_8));
                fos.close();
            } catch (IOException ignored) {}

            statusBox.getChildren().clear();
            statusBox.getChildren().add(new Label(" Connecting...", createSpinner()));

            if (url.equals("!mock")) {
                Main.db = new MockDatabase();
                stage.close();
                onConnectedToDB("!mock: " + user + "@" + dbName);
            } else {
                MySQLDatabase db = new MySQLDatabase();
                Main.db = db;

                db.onOpenSuccess(() -> Platform.runLater(() -> {
                    stage.close();
                    onConnectedToDB(user + "@" + url + "/" + dbName);
                }));
                db.onOpenFail(() -> Platform.runLater(() -> {
                    statusBox.getChildren().clear();
                    statusBox.getChildren().add(new Label(" Connection failed", new ImageView(Icons._24.ERROR)));
                }));
                db.onSyncStarted(() -> Platform.runLater(() -> {
                    connectionP.getChildren().remove(0);
                    connectionP.getChildren().add(0, new ImageView(Icons._32.get("connected-sync")));
                }));
                db.onSyncFinished(() -> Platform.runLater(() -> {
                    connectionP.getChildren().remove(0);
                    connectionP.getChildren().add(0, new ImageView(Icons._32.get("connected-ready")));
                }));
            }

            Main.db.open(url, dbName, user, password);
        });

        hbox.getChildren().addAll(stretch, connectB, cancelB);

        VBox bottomBox = new VBox(5);
        bottomBox.getChildren().addAll(statusBox, hbox);
        root.setBottom(bottomBox);
        root.setCenter(vbox);

        root.setPrefSize(300, 500);
        stage.show();

    }

    private Node createToolbox(Node toolbox) {
        final ScrollPane sp = new ScrollPane(toolbox);
        sp.setFitToWidth(true);
        sp.getStyleClass().add("edge-to-edge");

        return sp;
    }

    private Scenario addNewScenario() {
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

    private Node createConnectToDBBody() {
        BorderPane p = new BorderPane();
        p.setStyle("-fx-background-color:" + STYLE_BACKGROUND_COLOR + ";");
        Label l = new Label("Connect to database and select scenario");
        l.setStyle("-fx-text-fill:" + STYLE_LOW_CONTRAST_TEXT_COLOR + ";");
        p.setCenter(l);

        return p;
    }

    static ImageView createSpinner() {
        ImageView iv = new ImageView(Icons._24.PROGRESS_INDICATOR);
        final Timeline timeLine = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(iv.rotateProperty(), 0.0)),
                new KeyFrame(new Duration(2500), new KeyValue(iv.rotateProperty(), 360.0))
        );
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
        return iv;
    }

}
