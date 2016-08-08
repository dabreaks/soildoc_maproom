package org.soildoc.maproom.maprooms;

import java.util.LinkedList;

public class Maproom {

    String url = null;
    LinkedList<MaproomParameterNode> parameters = new LinkedList<>();
    boolean validLocation = true;

    public String getAdress() {

        String address = this.url;

        for(MaproomParameterNode node : this.parameters) {
            address += "&" + node.getParameter() + "=" + node.getValue();
        }

        return address;
    }

    public boolean validLocation() {
        return validLocation;
    }
}
