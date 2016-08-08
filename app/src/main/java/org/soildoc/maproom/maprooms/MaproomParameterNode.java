package org.soildoc.maproom.maprooms;

public class MaproomParameterNode {

    String parameter;
    String value;

    public MaproomParameterNode(String p, String v) {
        parameter = p;
        value = v;
    }

    public String getParameter() {
        return parameter;
    }

    public String getValue() {
        return value;
    }
}
