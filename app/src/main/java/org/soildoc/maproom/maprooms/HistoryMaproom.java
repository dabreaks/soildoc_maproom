package org.soildoc.maproom.maprooms;

import android.location.Location;

import org.soildoc.maproom.validmaproom.ValidLocation;
import org.soildoc.maproom.validmaproom.inTanzania;

public class HistoryMaproom extends Maproom {

    public HistoryMaproom(Location location) {
        this.url = "http://maproom.meteo.go.tz/maproom/Climatology/Climate_Analysis/monthly.html?";
        this.parameters.add(new MaproomParameterNode("bbox", new BoundingBox(location, 0.0375, 50).returnParameter()));
        this.parameters.add(new MaproomParameterNode("region", new Box(location, 0.0375).returnParameter()));
        ValidLocation validlocation = new inTanzania();
        this.validLocation = validlocation.isValid(location);
    }


}
