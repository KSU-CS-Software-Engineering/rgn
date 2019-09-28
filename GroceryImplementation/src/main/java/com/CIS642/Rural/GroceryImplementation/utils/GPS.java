package com.CIS642.Rural.GroceryImplementation.utils;

import com.CIS642.Rural.GroceryImplementation.scenario.MapNode;

/**
 *
 */
public class GPS {

    private static final int EARTH_RADIUS_KM = 6378;

    // Based on https://stackoverflow.com/a/365853
    public static double distanceInKmBetween(MapNode from, MapNode to) {

        final double dLat = Math.toRadians(to.gpsLat - from.gpsLat);
        final double dLon = Math.toRadians(to.gpsLon - from.gpsLon);

        final double lat1 = Math.toRadians(from.gpsLat);
        final double lat2 = Math.toRadians(to.gpsLat);

        final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

}
