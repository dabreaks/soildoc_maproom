package org.soildoc.maproom.maprooms;

import android.location.Location;

public class ForecastMaproom extends Maproom {

    public ForecastMaproom(Location location) {
        this.url = "http://iridl.ldeo.columbia.edu/maproom/Global/Forecasts/Flexible_Forecasts/precipitation.html?";
        this.parameters.add(new MaproomParameterNode("bbox", new BoundingBox(location, 2.5, 3).returnParameter()));
        this.parameters.add(new MaproomParameterNode("region", new Box(location, 2.5).returnParameter()));
    }
}
