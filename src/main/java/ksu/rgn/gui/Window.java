package ksu.rgn.gui;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import ksu.rgn.Main;
import ksu.rgn.db.MySQLDBDatabase;
import ksu.rgn.scenario.Scenario;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 *
 */
public class Window extends Application {

    static final String STYLE_BACKGROUND_COLOR = "#F4F4F4";
    static final String STYLE_LOW_CONTRAST_TEXT_COLOR = "#999999";


    BorderPane border;
    Scenario selectedScenario = null;
    HBox connectionP, topBarP;
    final Label selectedScenarioL = new Label("");
    final Label nameSelectedScenarioL = new Label("");

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
        ImageView iv = new ImageView(Icons._32.get("connected-ready"));
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
        topBarP.getChildren().addAll(ScenarioList.createScenarioSelectPanel(this), hGrow, connectionP);
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



    private void showDbInfoWindow() {
        Stage stage = new Stage();

        BorderPane root = new BorderPane();

        stage.setScene(new Scene(root));
        stage.setTitle("Database connection");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(border.getScene().getWindow());

        VBox vbox = new VBox(5);
        vbox.setPadding(new Insets(10));

        Label title = new Label("MySQL/MariaDB login credentials");
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
        vbox.getChildren().add(new Label("Server URL:"));
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


            MySQLDBDatabase db = new MySQLDBDatabase();
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
