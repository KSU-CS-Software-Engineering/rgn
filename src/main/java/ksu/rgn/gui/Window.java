package ksu.rgn.gui;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 */
public class Window extends Application {

    private static final String STYLE_BACKGROUND_COLOR = "#CCCCCC";
    private static final String STYLE_LOW_CONTRAST_TEXT_COLOR = "#999999";
    private static final String STYLE_NAVBAR_BACKGROUND_COLOR = "#B8DFF4";


    @Override
    public void start(Stage stage) {
        BorderPane border = new BorderPane();

        border.setTop(createTopBar());
//        border.setLeft(addVBox());

//        addStackPane(hbox);

        border.setCenter(createConnectToDBBody());

        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setTitle("Rural grocery network");
        stage.setWidth(960);
        stage.setHeight(720);
        stage.setMinWidth(400);
        stage.setMinHeight(300);
        stage.show();
    }

    private Node createTopBar() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 6, 10, 6));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color:" +  STYLE_NAVBAR_BACKGROUND_COLOR + ";");

        Button buttonCurrent = new Button("New");

        Button buttonProjected = new Button("Save");

        hbox.getChildren().addAll(buttonCurrent, buttonProjected);

        return hbox;
    }

    private Node createConnectToDBBody() {
        Pane p = new Pane();
        p.setStyle("-fx-background-color:" + STYLE_BACKGROUND_COLOR + ";");
        Label l = new Label("Connect to database and select scenario");
        l.setStyle("-fx-text-fill:" + STYLE_LOW_CONTRAST_TEXT_COLOR + ";");
        p.getChildren().add(l);

        return l;
    }

}
