package org.soildoc.maproom.maprooms;

import android.location.Location;

import org.soildoc.maproom.validmaproom.ValidLocation;
import org.soildoc.maproom.validmaproom.inTanzania;

public class MonitorMaproom extends Maproom {

    public MonitorMaproom(Location location) {
        this.url = "http://maproom.meteo.go.tz/maproom/Climatology/Climate_Monitoring/index.html?";
        this.parameters.add(new MaproomParameterNode("bbox", new BoundingBox(location, 0.0375, 25).returnParameter()));
        this.parameters.add(new MaproomParameterNode("region", new Box(location, 0.0375).returnParameter()));
        ValidLocation validlocation = new inTanzania();
        this.validLocation = validlocation.isValid(location);
    }
}
