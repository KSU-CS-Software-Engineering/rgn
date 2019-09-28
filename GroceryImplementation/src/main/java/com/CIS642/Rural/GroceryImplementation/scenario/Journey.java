package com.CIS642.Rural.GroceryImplementation.scenario;

import com.CIS642.Rural.GroceryImplementation.utils.Tuple2;

import java.util.ArrayList;

/**
 *
 */
public class Journey {

    public final Truck truck;

    public Journey(Truck truck) {
        this.truck = truck;
    }

    private final ArrayList<Tuple2<DirectRoute, Integer>> legs = new ArrayList<>();

    public void addLeg(DirectRoute route, int load) {
        legs.add(new Tuple2<>(route, load));
    }


    @Override
    public String toString() {
        final String nl = System.lineSeparator();
        final String ind = "   ";
        final StringBuilder sb = new StringBuilder("Journey {").append(nl);
        sb.append(ind).append("truck = ").append(truck).append(nl);


        for (Tuple2<DirectRoute, Integer> leg : legs) {
            sb
                    .append(ind).append("* ")
                    .append("#").append(leg._1.from.ID)
                    .append(" -> ")
                    .append("#").append(leg._1.to.ID)
                    .append(", load = ").append(leg._2)
                    .append(", from = ").append(leg._1.from)
                    .append(", to = ").append(leg._1.from)
                    .append(nl);
        }

        sb.append("}");
        return sb.toString();
    }
}
