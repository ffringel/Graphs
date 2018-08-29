package roadgraph;

import geography.GeographicPoint;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MapNode implements Comparable<MapNode> {
    private GeographicPoint location;
    private Set<MapEdge> edges;
    private List<MapNode> neighbors;
    private double distance;
    private double actualDistance;

    public MapNode(GeographicPoint location) {
        this.location = location;
        edges = new HashSet<>();
        neighbors = new LinkedList<>();
        distance = 0.0;
    }

    public GeographicPoint getLocation() {
        return location;
    }

    public void addEdge(MapEdge edge) {
        edges.add(edge);
    }

    public Set<MapEdge> getEdges() {
        return edges;
    }

    public void addNeighbors(MapNode node) {
        neighbors.add(node);
    }

    public List<MapNode> getNeighbors() {
        return neighbors;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setActualDistance(double distance) {
        this.actualDistance = distance;
    }

    public double getActualDistance() {
        return actualDistance;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MapNode) || (o == null)) {
            return false;
        }

        MapNode node = (MapNode)o;
        return node.location.equals(this.location);
    }

    @Override
    public int compareTo(MapNode o) {
        MapNode node = (MapNode) o;

        return ((Double)this.getDistance()).compareTo((Double)node.getDistance());
    }
}
