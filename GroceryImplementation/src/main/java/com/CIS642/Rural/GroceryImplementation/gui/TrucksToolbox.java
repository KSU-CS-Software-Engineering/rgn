package com.CIS642.Rural.GroceryImplementation.gui;

import com.CIS642.Rural.GroceryImplementation.GroceryImplementationApplication;
import com.CIS642.Rural.GroceryImplementation.scenario.Scenario;
import com.CIS642.Rural.GroceryImplementation.scenario.Truck;
import com.CIS642.Rural.GroceryImplementation.utils.Tuple2;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import static com.CIS642.Rural.GroceryImplementation.gui.Toolboxes.*;

/**
 *
 */
public class TrucksToolbox {


    public static Node create(Scenario s, Window owner) {
        final VBox pane = createPane("Trucks");

        final Pane addTruckForm = addForm(pane);
        final Toolboxes.FormValues truckFormV = new Toolboxes.FormValues();

        addTextF(addTruckForm, "Name", "", truckFormV.listenF("Name", ""));
        addNumberF(addTruckForm, "Capacity", "palettes", 0, truckFormV.listenF("Capacity", 0));
        addCheckboxF(addTruckForm, "Refrigerated", true, truckFormV.listenF("Refrigerated", true));

        addVSpace(addTruckForm, 10);

        final Pane startingLocationForm = addForm(addTruckForm);
        addCheckboxF(startingLocationForm, "Given start location", false, truckFormV.listenF("Start enable", false));
        addGpsF(startingLocationForm, "", 0, 0, truckFormV.listenF("Start location", new Tuple2<>(0.0, 0.0)));

        final Pane endingLocationForm = addForm(addTruckForm);
        addCheckboxF(endingLocationForm, "Given end location", false, truckFormV.listenF("End enable", false));
        addGpsF(endingLocationForm, "", 0, 0, truckFormV.listenF("End location", new Tuple2<>(0.0, 0.0)));

        addVSpace(pane, 10);
        final UpdatableList truckList = addUpdatableList(pane, UpdatableList.createTwoLineRenderer(
                t -> "Truck - " + ((Truck) t).name,
                t -> (((Truck) t).refrigerated ? "Refrigerated, " : "") + ((Truck) t).capacity + " palettes"
        ));
        final Label addTruckL = addButtonWithLabel(addTruckForm, "Add truck", () -> {
            if (truckFormV.isValid) {

                final Truck t = new Truck();

                t.name = (String) truckFormV.vars.get("Name");
                t.capacity = (Integer) truckFormV.vars.get("Capacity");
                t.refrigerated = (Boolean) truckFormV.vars.get("Refrigerated");

                // TODO start/end location

                GroceryImplementationApplication.db.persist(() -> s.add(t), s).onFinish(o -> Platform.runLater(() -> truckList.updateList(s, null, s::getTrucks)));
                Platform.runLater(() -> {
                    final Truck[] ts = s.getTrucks().toArray(new Truck[s.getTrucks().size() + 1]);
                    ts[ts.length - 1] = t;
                    truckList.updateList(s, ts, s::getTrucks);
                });
            }
        });
        addTruckL.setStyle("-fx-text-fill: red; -fx-padding: 5px 0px 0px 5px;");
        truckFormV.isValidLabel = addTruckL;

        addVSpace(pane, 10);

        truckList.updateList(s, null, s::getTrucks);

        return pane;
    }

}