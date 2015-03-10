/**
 * 
 */
package main;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jgrapht.*;
import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.graph.*;

/**
 * @author coblebj
 *
 */
public class ProximityWeb {

	private UndirectedGraph<String, DefaultEdge> graph = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Load the hashmap and do some basic testing
		// remove in production code
		runTests();
	}
	
	public static void runTests(){
		ProximityWeb pw = new ProximityWeb();

		pw.addRelationship("pool", "water");
		pw.addRelationship("beach", "water");
		pw.prettyPrinter();
		
		pw.findPath("pool", "beach");
		
		
	}
	
	/**
	 * Constructor
	 */
	public ProximityWeb(){
		this.graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
	}
	
	/**
	 * Returns a path between nodes a and b
	 * TODO - make optimal
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public List<DefaultEdge> findPath(String a, String b){
		BellmanFordShortestPath<String, DefaultEdge> bfsp = 
				new BellmanFordShortestPath<String, DefaultEdge>(this.graph, a);
		
		List<DefaultEdge> path = bfsp.getPathEdgeList(b);
		System.out.println(path);
		return path;
	}
	
	/**
	 * Adds the edge between two vertices.
	 * 
	 * @param a
	 * @param b
	 */
	public void addRelationship(String a, String b){
		
		if (a.equals("") || b.equals("")){
			return;
		}
		if (!this.graph.containsVertex(a)){
			this.graph.addVertex(a);
		}
		if (!this.graph.containsVertex(b)){
			this.graph.addVertex(b);
		}
		if (!this.graph.containsEdge(a, b)){
			this.graph.addEdge(a, b);
		}
	}
	
	
	/**
	 * Function to print out the graph in a more readable format.
	 */
	public void prettyPrinter(){
		Set<String> s = this.graph.vertexSet();
		Iterator<String> iter = s.iterator();
		
		while(iter.hasNext()){
			String key = iter.next();
			System.out.println(key + " : " + this.graph.edgesOf(key));
//			System.out.println(key + " : " + this.graph.edgeSet().toString());
		}
	}
	
	
	
}
