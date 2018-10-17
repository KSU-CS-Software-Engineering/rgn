package ksu.rgn.gui;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 *
 */
public class Window extends Application {

    private static final String STYLE_BACKGROUND_COLOR = "#F4F4F4";
    private static final String STYLE_LOW_CONTRAST_TEXT_COLOR = "#999999";


    private BorderPane border;

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

    private Node createScenarioSelectPanel() {
        Button openB = new Button("", new ImageView(Icons._24.OPEN_FOLDER));
        Button addNewB = new Button("", new ImageView(Icons._24.ADD_NEW));
        openB.setStyle("-fx-background-radius: 3 0 0 3");
        addNewB.setStyle("-fx-background-radius: 0 3 3 0");

        Label selectedL = new Label("");
        Label nameL = new Label("");
        nameL.setStyle("-fx-font-weight: bold");

        openB.setOnAction(e -> {
            selectedL.setText("Selected:");
            nameL.setText("New #" + (int)(Math.random() * 899999 + 100000));
            reloadScenario();
        });
        addNewB.setOnAction(e -> {
            selectedL.setText("Selected:");
            nameL.setText("New #" + (int)(Math.random() * 899999 + 100000));
            reloadScenario();
        });

        VBox vBox = new VBox();
        vBox.getChildren().addAll(selectedL, nameL);
        vBox.setPadding(new Insets(0, 0, 0, 10));
        HBox hBox = new HBox(0);
        hBox.getChildren().addAll(openB, addNewB, vBox);
        return hBox;
    }

    private void reloadScenario() {
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
