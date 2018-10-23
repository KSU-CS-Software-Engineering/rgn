package ksu.rgn.gui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ksu.rgn.Main;
import ksu.rgn.scenario.Scenario;

import java.util.function.Consumer;

/**
 *
 */
public class Toolboxes {

    private static VBox createList(String text) {
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
            tf.textProperty().addListener(ae -> onChange.accept(tf.getText()));
        }
        to.getChildren().addAll(l, tf);
    }

    private static void addButton(Pane to, String name, Runnable onClick) {
        final Button b = new Button(name);
        if (onClick != null) {
            b.setOnAction(ae -> onClick.run());
        }
        to.getChildren().add(b);
    }

    private static Pane addForm(Pane to) {
        final VBox form = new VBox(10);
        form.setStyle("-fx-border-color: #ddd; -fx-border-style: solid; -fx-border-width: 0 0 0 4px");
        form.setPadding(new Insets(0, 0, 0, 12));

        to.getChildren().add(form);

        return form;
    }


    public static Node createScenarioToolbox(Scenario s) {
        final VBox list = createList("Scenario");

        addTextF(list, "Name", s.name, null);
        addTextAreaF(list, "Description", s.description, t -> {
            s.description = t;
            Main.getDBQueries().persistScenario(s);
        });

        return list;
    }

    public static Node createNodesToolbox(Scenario s) {
        final VBox list = createList("Nodes");

        final Pane addNodeForm = addForm(list);
        addTextF(addNodeForm, "Name", "", null);
        addTextF(addNodeForm, "Lat", "", null);
        addTextF(addNodeForm, "Lon", "", null);
        addButton(addNodeForm, "Add node", null);

        addButton(list, "Remove all nodes", null);

        return list;
    }

    public static Node createTrucksToolbox(Scenario s) {
        final VBox list = createList("Trucks");

        addButton(list, "Add truck", null);
        addButton(list, "Remove truck", null);

        return list;
    }

    public static Node createRoutesToolbox(Scenario s) {
        final VBox list = createList("Routes");

        addButton(list, "Calculate", null);

        return list;
    }

    public static Node createMapView(Scenario s) {
        BorderPane p = new BorderPane();
        p.setStyle("-fx-background-color:white;");
        Label l = new Label("This is a map");
        l.setStyle("-fx-text-fill:black;");
        p.setCenter(l);

        return p;
    }

}
