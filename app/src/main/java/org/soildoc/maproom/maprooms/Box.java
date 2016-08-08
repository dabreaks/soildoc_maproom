package org.soildoc.maproom.maprooms;

import android.location.Location;

import java.util.Locale;

public class Box {
    double left;
    double right;
    double up;
    double down;

    public Box() {
        this.left = 0;
        this.right = 0;
        this.up = 0;
        this.down = 0;
    }

    public Box(Location location, double increment) {
        double latitude = 90 - increment;
        double longitude = 180 - increment;
        while (latitude > location.getLatitude()) {
            latitude -= increment;
        }
        while (longitude > location.getLongitude()) {
            longitude -= increment;
        }
        this.left = longitude;
        this.down = latitude;
        this.right = longitude + increment;
        this.up = latitude + increment;
    }

    private String strFormat(double coordinate) {

        return String.format(Locale.US, "%.2f",coordinate);

    }

    public String returnParameter() {

        return "bb%3A" + strFormat(left) + "%3A" + strFormat(down) + "%3A" + strFormat(right) + "%3A" + strFormat(up) + "%3Abb";
    }

}