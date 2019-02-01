package ksu.rgn.scenario;

import ksu.rgn.utils.GPS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class DirectRoute {

    private static final Logger LOG = LoggerFactory.getLogger(DirectRoute.class);

    public final MapNode from, to;

    public DirectRoute(MapNode from, MapNode to) {
        this.from = from;
        this.to = to;
    }


    public double heuristics() {
        return GPS.distanceInKmBetween(from, to);
    }


    private double actualCost = Double.NaN;
    public double actualCost() {
        if (Double.isNaN(actualCost)) {
            LOG.warn("Query of actual cost is not implemented yet");
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
