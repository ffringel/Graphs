package roadgraph;

import geography.GeographicPoint;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MapNode {
    GeographicPoint location;
    Set<MapEdge> edges;
    List<MapNode> neighbors;

    public MapNode(GeographicPoint location) {
        this.location = location;
        edges = new HashSet<>();
        neighbors = new LinkedList<>();
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MapNode) || (o == null)) {
            return false;
        }

        MapNode node = (MapNode)o;
        return node.location.equals(this.location);
    }
}
