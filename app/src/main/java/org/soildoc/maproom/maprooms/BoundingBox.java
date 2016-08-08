package org.soildoc.maproom.maprooms;

import android.location.Location;

public class BoundingBox extends Box {

    public BoundingBox(Location location, double increment, double bound) {
        double latitude = 90 - increment;
        double longitude = 180 - increment;
        while (latitude > location.getLatitude()) {
            latitude -= increment;
        }
        while (longitude > location.getLongitude()) {
            longitude -= increment;
        }
        this.left = Math.max(longitude - increment * bound, -90.0);
        this.down = Math.max(latitude - increment * bound, -180);
        this.right = Math.min(longitude + increment + increment * bound, 90);
        this.up = Math.min(latitude + increment + increment * bound, 180);
    }
}
