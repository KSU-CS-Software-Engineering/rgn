package ksu.rgn.computation;

import ksu.rgn.scenario.*;
import ksu.rgn.utils.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class RandomC implements ScenarioComputation {

    private static final Logger LOG = LoggerFactory.getLogger(RandomC.class);

    @Override
    public Journey[] computeScenario(Scenario s) {

        LOG.debug("Preparing for scenario computation");

        if (s.getTrucks().isEmpty()) {
            LOG.error("Add some trucks. It is hard to move stuff without any trucks...");
            return null;
        } else if (s.getNodes().size() <= 1) {
            LOG.error("Add few more nodes. This not really a graph problem...");
            return null;
        }

        MapNode supplyNode = null;
        Truck truck = s.getTrucks().iterator().next();
        for (MapNode n : s.getNodes()) {
            if (n.demand < 0) { // Assume it is infinite, for now
                supplyNode = n;
                break;
            }
        }

        if (supplyNode == null) {
            LOG.error("No supply node. We don't have stuff to move");
            return null;
        }

        LOG.debug("Starting actual computation");

        final Journey j = new Journey(truck);
        if (truck.startingNode != null && truck.startingNode != supplyNode) {
            j.addLeg(new DirectRoute(truck.startingNode, supplyNode), 0);
        }

        final MapNode[] nodes = s.getNodes().toArray(new MapNode[0]);
        Collections.shuffleArray(nodes);

        for(MapNode n : nodes) {
            if (n.demand <= 0) continue;

            int toDeliver = n.demand;
            while (toDeliver > 0) {
                j.addLeg(new DirectRoute(supplyNode, n), Math.min(toDeliver, truck.capacity));
                j.addLeg(new DirectRoute(n, supplyNode), 0);
                toDeliver -= Math.min(toDeliver, truck.capacity);
            }
        }

        if (truck.endingNode != null && truck.endingNode != supplyNode) {
            j.addLeg(new DirectRoute(supplyNode, truck.endingNode), 0);
        }

        return new Journey[] { j };
    }

}
