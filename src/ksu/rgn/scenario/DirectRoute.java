package ksu.rgn.scenario;

import ksu.rgn.utils.GPS;

/**
 *
 */
public class DirectRoute {

    public final Node from, to;

    public DirectRoute(Node from, Node to) {
        this.from = from;
        this.to = to;
    }


    public double heuristics() {
        return GPS.distanceInKmBetween(from.location, to.location);
    }


    private double actualCost = Double.NaN;
    public double actualCost() {
        if (Double.isNaN(actualCost)) {
            // TODO: Ask google for route and save it, not just its cost
            actualCost = heuristics() * (Math.random() + 1);
            return actualCost;
        } else {
            return actualCost;
        }
    }

    public boolean isActualCostKnown() {
        return !Double.isNaN(actualCost);
    }


}
