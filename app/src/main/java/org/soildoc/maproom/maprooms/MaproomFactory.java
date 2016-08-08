package org.soildoc.maproom.maprooms;

import android.location.Location;

public class MaproomFactory {

    private Location location = null;

    public MaproomFactory(Location location) {
        this.location = location;
    }

    public Maproom makeMaproom(int newMaproomType) {

        switch (newMaproomType) {
            case 1:
                return new ForecastMaproom(this.location);
            case 2:
                return new HistoryMaproom(this.location);
            case 3:
                return new MonitorMaproom(this.location);
            default:
                return null;
        }
    }

}
