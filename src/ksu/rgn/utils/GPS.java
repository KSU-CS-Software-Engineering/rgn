package ksu.rgn.utils;

import ksu.rgn.scenario.MapLocation;

/**
 *
 */
public class GPS {

    private static final int EARTH_RADIUS_KM = 6378;

    // Based on https://stackoverflow.com/a/365853
    public static double distanceInKmBetween(MapLocation from, MapLocation to) {

        double dLat = Math.toRadians(to.lat - from.lat);
        double dLon = Math.toRadians(to.lon - from.lon);

        double lat1 = Math.toRadians(from.lat);
        double lat2 = Math.toRadians(to.lat);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

}
