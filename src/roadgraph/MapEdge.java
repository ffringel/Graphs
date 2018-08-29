package roadgraph;

import geography.GeographicPoint;

public class MapEdge {
    GeographicPoint start;
    GeographicPoint end;
    String roadName;
    String roadType;
    double edgeLength;

    private static double DEFAULT_LENGTH = 0.01;

    public MapEdge(GeographicPoint start, GeographicPoint end,
                   String roadName, String roadType, double edgeLength) {
        this.start = start;
        this.end = end;
        this.roadName = roadName;
        this.roadType = roadType;
        this.edgeLength = edgeLength;
    }

    public GeographicPoint getStart() {
        return start;
    }

    public GeographicPoint getEnd() {
        return end;
    }

    public String getRoadName() {
        return roadName;
    }

    public String getRoadType() {
        return roadType;
    }

    public double getEdgeLength() {
        return edgeLength;
    }
}
