/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.*;
import java.util.function.Consumer;

import geography.GeographicPoint;
import util.GraphLoader;

/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
	HashMap<GeographicPoint, MapNode> vertices;
	MapEdge mapEdge;
	int edgeCount = 0;
	
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph() {
		vertices = new HashMap<>();
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices() {

		return vertices.keySet().size();
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices() {

		return new HashSet<>(vertices.keySet());
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges() {

		return edgeCount;
	}

	
	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location) {

		MapNode mapNode = new MapNode(location);
		if (location == null || vertices.containsKey(location)) {
			return false;
		} else {
			vertices.put(location, mapNode);

			return true;
		}
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {
		if (!vertices.containsKey(from) || !vertices.containsKey(to) || roadName == null
				|| roadType == null || length == 0) {
			throw new IllegalArgumentException("Cannot add an edge.");
		}

		mapEdge = new MapEdge(from, to, roadName, roadType, length);
		vertices.get(from).addEdge(mapEdge);
		vertices.get(from).addNeighbors(vertices.get(to));
		edgeCount++;
	}
	

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
		MapNode startNode = vertices.get(start);
		MapNode goalNode = vertices.get(goal);

		if (startNode == null) {
			System.err.println("Start node " + start + "does not exist");
			return null;
		}

		if (goalNode == null) {
			System.err.println("Goal node " + goal + " does not exist");
			return null;
		}

		Queue<MapNode> queue = new LinkedList<>();
		HashSet<MapNode> visited = new HashSet<>();
		HashMap<MapNode, MapNode> parentMap = new HashMap<>();

		queue.add(startNode);
		visited.add(startNode);

		while (!queue.isEmpty()) {
			MapNode current = queue.poll();
			nodeSearched.accept(current.getLocation());

			if (current.equals(goalNode))
				break;
			
			for (MapNode neighbor : current.getNeighbors()) {
				if (!visited.contains(neighbor)) {
					visited.add(neighbor);
					parentMap.put(neighbor, current);
					queue.add(neighbor);
				}
			}
		}

		LinkedList<GeographicPoint> path = contructPath(parentMap, goalNode);

		return path;
	}

	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
		MapNode startNode = vertices.get(start);
		MapNode goalNode = vertices.get(goal);

		if (startNode == null) {
			System.err.println("Start node " + start + "does not exist");
			return null;
		}

		if (goalNode == null) {
			System.err.println("Goal node " + goal + " does not exist");
			return null;
		}

		PriorityQueue<MapNode> queue = new PriorityQueue<>();
		Set<MapNode> visited = new HashSet<>();
		HashMap<MapNode, MapNode> parentMap = new HashMap<>();

		for (MapNode node : vertices.values())
			node.setDistance(Double.POSITIVE_INFINITY);

		startNode.setDistance(0);
		queue.add(startNode);

		while (!queue.isEmpty()) {
			MapNode current = queue.poll();
			nodeSearched.accept(current.getLocation());

			if (current.equals(goalNode))
				break;

			if (!visited.contains(current)) {
				visited.add(current);
				for (MapNode neighbor : current.getNeighbors()) {
					if (!visited.contains(neighbor)) {
						double distance = mapEdge.getEdgeLength() + current.getDistance();
						if (distance < neighbor.getDistance()) {
							neighbor.setDistance(distance);
							parentMap.put(neighbor, current);
							queue.add(neighbor);
						}
					}
				}
			}
		}

		LinkedList<GeographicPoint> path = contructPath(parentMap, goalNode);

		return path;
	}

	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
		MapNode startNode = vertices.get(start);
		MapNode goalNode = vertices.get(goal);

		if (startNode == null) {
			System.err.println("Start node " + start + "does not exist");
			return null;
		}

		if (goalNode == null) {
			System.err.println("Goal node " + goal + " does not exist");
			return null;
		}

		PriorityQueue<MapNode> queue = new PriorityQueue<>();
		Set<MapNode> visited = new HashSet<>();
		HashMap<MapNode, MapNode> parentMap = new HashMap<>();

		for (MapNode node : vertices.values()) {
			node.setDistance(Double.POSITIVE_INFINITY);
			node.setActualDistance(Double.POSITIVE_INFINITY);
		}

		startNode.setDistance(0);
		startNode.setActualDistance(0);
		queue.add(startNode);

		while (!queue.isEmpty()) {
			MapNode current = queue.poll();
			nodeSearched.accept(current.getLocation());

			if (current.equals(goalNode))
				break;

			if (!visited.contains(current)) {
				visited.add(current);

				for (MapNode neighbor : current.getNeighbors()) {
					if (!visited.contains(neighbor)) {
						double distance = mapEdge.getEdgeLength() + current.getActualDistance();
						double preDist = distance + neighbor.getLocation().distance(goalNode.getLocation());
						if (preDist < neighbor.getDistance()) {
							neighbor.setDistance(preDist);
							neighbor.setActualDistance(distance);
							queue.add(neighbor);
							parentMap.put(neighbor, current);
						}
					}
				}
			}

		}

		LinkedList<GeographicPoint> path = contructPath(parentMap, goalNode);
		
		return path;
	}

	private LinkedList<GeographicPoint> contructPath(HashMap<MapNode, MapNode> parentMap, MapNode goal) {
		LinkedList<GeographicPoint> path = new LinkedList<>();

		MapNode node = goal;
		while (node != null) {
			path.addFirst(node.getLocation());
			node = parentMap.get(node);
		}

		return path;
	}
	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
		MapGraph firstMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
		System.out.println("DONE.");
		
		// You can use this method for testing.  
		
		
		/* Here are some test cases you should try before you attempt 
		 * the Week 3 End of Week Quiz, EVEN IF you score 100% on the 
		 * programming assignment.
		 */
		/*
		MapGraph simpleTestMap = new MapGraph();
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
		
		GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
		GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
		
		System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
		List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
		List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
		
		
		MapGraph testMap = new MapGraph();
		GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
		
		// A very simple test using real data
		testStart = new GeographicPoint(32.869423, -117.220917);
		testEnd = new GeographicPoint(32.869255, -117.216927);
		System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		
		
		// A slightly more complex test using real data
		testStart = new GeographicPoint(32.8674388, -117.2190213);
		testEnd = new GeographicPoint(32.8697828, -117.2244506);
		System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
		testroute = testMap.dijkstra(testStart,testEnd);
		testroute2 = testMap.aStarSearch(testStart,testEnd);
		*/
		
		
		/* Use this code in Week 3 End of Week Quiz */
		/*MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);
		
		
		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);

		*/
		
	}
}
