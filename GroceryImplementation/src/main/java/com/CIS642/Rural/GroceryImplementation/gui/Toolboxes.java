package com.CIS642.Rural.GroceryImplementation.gui;

import com.CIS642.Rural.GroceryImplementation.GroceryImplementationApplication;
import com.CIS642.Rural.GroceryImplementation.scenario.Scenario;
import com.CIS642.Rural.GroceryImplementation.utils.Tuple2;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 *
 */
public class Toolboxes {

    static VBox createPane(String text) {
        final VBox list = new VBox(10);
        list.setPadding(new Insets(10, 10, 10, 10));
        list.setPrefWidth(300);

        final Label l = new Label(text);
        l.setStyle("-fx-font-size: 2em;");
        l.setPadding(new Insets(0, 0, 10, 0));
        list.getChildren().add(l);

        return list;
    }

    static void addHeader(Pane to, String text) {
        final Label l = new Label(text);
        l.setStyle("-fx-font-size: 1.3em;");
        l.setPadding(new Insets(0, 0, 5, 0));
        to.getChildren().add(l);
    }

    static void addTextAreaF(Pane to, String name, String initialValue, Consumer<String> onChange) {
        final Label l = new Label(name);
        l.setPadding(new Insets(0, 0, -7, 0));
        final TextArea tf = new TextArea(initialValue);
        tf.setWrapText(true);
        tf.setPrefRowCount(5);
        if (onChange != null) {
            tf.textProperty().addListener((observableValue, s, t1) -> onChange.accept(tf.getText()));
        }

        if (!name.isEmpty()) to.getChildren().add(l);
        to.getChildren().add(tf);
    }

    static void addTextF(Pane to, String name, String initialValue, Consumer<String> onChange) {
        final Label l = new Label(name);
        l.setPadding(new Insets(0, 0, -7, 0));
        final TextField tf = new TextField(initialValue);
        if (onChange != null) {
            tf.textProperty().addListener((observableValue, s, t1) -> onChange.accept(tf.getText()));
        }

        if (!name.isEmpty()) to.getChildren().add(l);
        to.getChildren().add(tf);
    }

    static void addNumberF(Pane to, String name, String units, int initialValue, Consumer<Integer> onChange) {
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

    static void addGpsF(Pane to, String name, double initialLat, double initialLon, Consumer<Tuple2<Double, Double>> onChange) {
        final Label l = new Label(name);
        l.setPadding(new Insets(0, 0, -7, 0));
        final TextField latTf = new TextField(Double.toString(initialLat));
        final TextField lonTf = new TextField(Double.toString(initialLon));

        final ChangeListener<? super String> latListener = (observable, oldValue, newValue) -> {
            if (!newValue.matches("[+\\-]?[\\d]*\\.?[\\d]+") || newValue.isEmpty()) {
                latTf.setStyle("-fx-control-inner-background: #ff9696;");
                MapView.current().selectPoint(null);
                if (onChange != null) onChange.accept(null);
            } else {
                latTf.setStyle("");
                MapView.current().updatePointSelection(Double.parseDouble(latTf.getText()), Double.parseDouble(lonTf.getText()));
                if (onChange != null)
                    onChange.accept(new Tuple2<>(Double.parseDouble(latTf.getText()), Double.parseDouble(lonTf.getText())));
            }
        };
        latTf.textProperty().addListener(latListener);

        final ChangeListener<? super String> lonListener = (observable, oldValue, newValue) -> {
            if (!newValue.matches("[+\\-]?[\\d]*\\.?[\\d]+") || newValue.isEmpty()) {
                lonTf.setStyle("-fx-control-inner-background: #ff9696;");
                MapView.current().selectPoint(null);
                if (onChange != null) onChange.accept(null);
            } else {
                lonTf.setStyle("");
                MapView.current().updatePointSelection(Double.parseDouble(latTf.getText()), Double.parseDouble(lonTf.getText()));
                if (onChange != null)
                    onChange.accept(new Tuple2<>(Double.parseDouble(latTf.getText()), Double.parseDouble(lonTf.getText())));
            }
        };
        lonTf.textProperty().addListener(lonListener);

        final ToggleButton pickOnMapB = new ToggleButton("", new ImageView(Icons._16.LOCATION));
        pickOnMapB.setOnAction(ae -> MapView.current().selectPoint(pos -> {
            latTf.setText("" + pos._1);
            lonTf.setText("" + pos._2);
            if (onChange != null) onChange.accept(pos);
            pickOnMapB.setSelected(false);
        }));

        final HBox loc = new HBox(3);
        loc.getChildren().addAll(latTf, lonTf, pickOnMapB);

        if (!name.isEmpty()) to.getChildren().add(l);
        to.getChildren().add(loc);
    }

    static void addCheckboxF(Pane to, String name, boolean initialValue, Consumer<Boolean> onChange) {
        final CheckBox c = new CheckBox(name);
        c.setSelected(initialValue);
        if (onChange != null) {
            c.selectedProperty().addListener(ae -> onChange.accept(c.isSelected()));
        }
        to.getChildren().add(c);
    }

    static Button addButton(Pane to, String name, Runnable onClick) {
        final Button b = new Button(name);
        if (onClick != null) {
            b.setOnAction(ae -> onClick.run());
        }
        to.getChildren().add(b);

        return b;
    }

    static Label addButtonWithLabel(Pane to, String name, Runnable onClick) {
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

    static Pane addForm(Pane to) {
        final VBox form = new VBox(10);
        form.setStyle("-fx-border-color: #ddd; -fx-border-style: solid; -fx-border-width: 0 0 0 4px");
        form.setPadding(new Insets(0, 0, 0, 12));

        to.getChildren().add(form);

        return form;
    }

    static void addVSpace(Pane to, double space) {
        final Pane spacer = new Pane();
        spacer.setPrefHeight(space);
        to.getChildren().add(spacer);
    }


    static UpdatableList addUpdatableList(Pane to, Function<Tuple2<Object, Button>, HBox> renderer) {
        final VBox list = new VBox(10);
        list.setStyle("-fx-border-color: #ddd; -fx-border-style: solid; -fx-border-width: 0 0 0 1px");
        list.setPadding(new Insets(0, 0, 0, 5));
        list.setMaxHeight(Double.MAX_VALUE);

        to.getChildren().add(list);

        return new UpdatableList(list, renderer);
    }

    static class UpdatableList {

        private final VBox list;
        private final Function<Tuple2<Object, Button>, HBox> renderer;

        static Function<Tuple2<Object, Button>, HBox> createTwoLineRenderer(Function<Object, String> nameLine, Function<Object, String> descLine) {
            return o -> {
                final HBox row = new HBox(5);
                row.setStyle("-fx-background-color: white; -fx-padding: 5px;-fx-border-color: #ddd; -fx-border-style: solid; -fx-border-width: 1px");

                final Pane hGrow = new Pane();
                HBox.setHgrow(hGrow, Priority.ALWAYS);

                final Label nameL = new Label(nameLine.apply(o._1));
                nameL.setStyle("-fx-font-weight: bold;");
                final Label descL = new Label(descLine.apply(o._1));

                final VBox labelsG = new VBox(0);
                labelsG.getChildren().addAll(nameL, descL);

                row.getChildren().addAll(
                        labelsG,
                        hGrow,
                        o._2
                );

                return row;
            };
        }

        private UpdatableList(VBox list, Function<Tuple2<Object, Button>, HBox> renderer) {
            this.list = list;
            this.renderer = renderer;
        }

        void updateList(Scenario s, Object[] future, Supplier<Set> getUpToDateData) {
            list.getChildren().clear();

            final Node spinner = com.CIS642.Rural.GroceryImplementation.gui.Window.createSpinner();
            addVSpace(list, 5);
            final HBox spinnerPad = new HBox(spinner);
            spinnerPad.setPadding(new Insets(0, 0, 0, 20));
            list.getChildren().add(spinnerPad);
            addVSpace(list, 5);

            if (onChanged != null) onChanged.run();

            if (future == null) {
                GroceryImplementationApplication.db.preCache(() -> {
                    final Object[] data = getUpToDateData.get().toArray(new Object[0]);

                    Platform.runLater(() -> updateList(s, data, getUpToDateData));
                });
            } else {
                list.getChildren().clear();
                for (Object t : future) {
                    final Button goB = new Button("", new ImageView(Icons._24.TRASH));
                    goB.setPadding(new Insets(5, 8, 5, 8));
                    goB.setOnAction(e -> {
                        GroceryImplementationApplication.db.persist(() -> getUpToDateData.get().remove(t), s).onFinish(o -> Platform.runLater(() -> updateList(s, null, getUpToDateData)));
                        Platform.runLater(() -> {
                            final Object[] deleted = new Object[future.length - 1];
                            int skip = 0;
                            for (int i = 0; i < deleted.length; i++) {
                                if (future[i + skip] == t) skip++;
                                deleted[i] = future[i + skip];
                            }

                            updateList(s, deleted, getUpToDateData);
                        });
                    });

                    list.getChildren().add(renderer.apply(new Tuple2<>(t, goB)));
                }
            }

        }

        private Runnable onChanged;

        void addOnChangedListener(Runnable onChanged) {
            this.onChanged = onChanged;
        }

    }


    static class FormValues {

        final HashMap<String, Object> vars = new HashMap<>();
        boolean isValid = true;
        Consumer<Boolean> onIsValidChange = null;
        Label isValidLabel = null;

        public <T> Consumer<T> listenF(String varName, T initialValue) {
            final Consumer<T> cb = o -> {
                vars.put(varName, o);
                if (o == null) {
                    if (isValidLabel != null) isValidLabel.setText("Invalid input");
                    if (onIsValidChange != null && isValid) onIsValidChange.accept(false);
                    isValid = false;
                } else {
                    boolean newIsValid = !vars.values().contains(null);
                    if (isValidLabel != null) isValidLabel.setText(newIsValid ? "" : "Invalid input");
                    if (onIsValidChange != null && (isValid != newIsValid)) onIsValidChange.accept(newIsValid);
                    isValid = newIsValid;
                }
            };
            cb.accept(initialValue);
            return cb;
        }

    }

}
