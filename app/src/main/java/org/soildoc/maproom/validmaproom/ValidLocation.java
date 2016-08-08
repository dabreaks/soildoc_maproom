package org.soildoc.maproom.validmaproom;

import android.location.Location;

public class ValidLocation {
    double northLimit = 0;
    double eastLimit = 0;
    double southLimit = 0;
    double westLimit = 0;

    public boolean isValid(Location location) {

        return ((southLimit < location.getLatitude()) &&
                (location.getLatitude() < northLimit ) &&
                (westLimit < location.getLongitude()) &&
                (location.getLongitude() < eastLimit ));

        }

    }
