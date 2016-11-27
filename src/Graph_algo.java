

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *  The {@code Graph_algo} class represents a data type for solving the
 *  single-source shortest paths problem in edge-weighted digraphs
 *  where the edge weights are nonnegative.
 *  <p>
 *  This implementation uses Dijkstra's algorithm with a binary heap.
 *  The constructor takes time proportional to <em>E</em> log <em>V</em>,
 *  where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 *  Afterwards, the {@code distTo()} and {@code hasPathTo()} methods take
 *  constant time and the {@code pathTo()} method takes time proportional to the
 *  number of edges in the shortest path returned.
 *  <p>
 *  For additional documentation,    
 *  see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of    
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne. 
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Graph_algo {
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private DirectedEdge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices
    private Graph graph;

    /**
     * Computes a shortest-paths tree from the source vertex {@code s} to every other
     * vertex in the edge-weighted digraph {@code G}.
     *
     * @param  graph the edge-weighted digraph
     * @param  sourceVertex the source vertex
     * @throws IllegalArgumentException if an edge weight is negative
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public Graph_algo(Graph graph, int sourceVertex) {
    	
    	//check legal weight of graphs edges.
        for (DirectedEdge e : graph.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }
        
        distTo = new double[graph.V()];
        edgeTo = new DirectedEdge[graph.V()];
        this.graph = new Graph(graph);

        validateVertex(sourceVertex);

        for (int v = 0; v < graph.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[sourceVertex] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(graph.V());
        pq.insert(sourceVertex, distTo[sourceVertex]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (DirectedEdge e : graph.adj(v))
                relax(e);
        }

        // check optimality conditions
        assert check(graph, sourceVertex);
    }

    // relax edge e and update pq if changed
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }

    /**
     * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param  v the destination vertex
     * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
     *         {@code Double.POSITIVE_INFINITY} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    private double distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }
    
    public String distAToB(int a, int b) {
        validateVertex(a);
        validateVertex(b);
        Graph_algo tempGraphAlgo = new Graph_algo(graph, a);
        return String.valueOf(tempGraphAlgo.distTo(b));
    }
    
    public String distAToB_WithBlackList(int a, int b, int [] arrBlackList) {
        validateVertex(a);
        validateVertex(b);
        Graph_algo tempGraphAlgo = new Graph_algo(graph, a);
        tempGraphAlgo.graph.setBL(arrBlackList);
        return String.valueOf(tempGraphAlgo.distTo(b));
    }

    /**
     * Returns true if there is a path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return {@code true} if there is a path from the source vertex
     *         {@code s} to vertex {@code v}; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return a shortest path from the source vertex {@code s} to vertex {@code v}
     *         as an iterable of edges, and {@code null} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    private Iterable<DirectedEdge> pathTo(int v) {
        validateVertex(v);
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }
        return path;
    }
    
    public void pathAToB(int a, int b) {
        validateVertex(a);
        validateVertex(b);
        Graph_algo tempGraphAlgo = new Graph_algo(graph, a);
        StdOut.println(tempGraphAlgo.pathTo(b));
    }
    
    public void PathAToB_WithBlackList(int a, int b, int [] arrBlackList) {
        validateVertex(a);
        validateVertex(b);
        Graph_algo tempGraphAlgo = new Graph_algo(graph, a);
        graph.setBL(arrBlackList);
        StdOut.println(tempGraphAlgo.pathTo(b));
        graph.RetBL(arrBlackList);
    }
    
	public String getInfo() {
		// TODO Auto-generated method stub
		return null;
	}




    // check optimality conditions:
    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
    private boolean check(Graph G, int s) {

        // check that edge weights are nonnegative
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (int v = 0; v < G.V(); v++) {
            for (DirectedEdge e : G.adj(v)) {
                int w = e.to();
                if (distTo[v] + e.weight() < distTo[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            DirectedEdge e = edgeTo[w];
            int v = e.from();
            if (w != e.to()) return false;
            if (distTo[v] + e.weight() != distTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Unit tests the {@code Graph_algo} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
    	File f1= new File("c:\\G0.txt");
        //In in = new In(f1);
        Graph G = new Graph(f1);
        int s = 1;

        // compute shortest paths
        Graph_algo sp = new Graph_algo(G, s);
        int arrBL [] ={4};
        sp.distAToB_WithBlackList(1, 5, arrBL);
        sp.PathAToB_WithBlackList(1, 5, arrBL);

    }
}


/******************************************************************************
 *  Copyright 2002-2016, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
