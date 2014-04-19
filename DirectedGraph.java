import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/*
 * Adjacency List implementation of a directed graph. Nodes are represented
 * implicitly by consecutive integer values in the interval [0, nodeCount).
 */
public class DirectedGraph
{
    private int                 nodeCount;
    private List<List<Integer>> adjacencyList;
    
    public DirectedGraph(int nodeCount, List<DirectedEdge> edges)
    {
        this.nodeCount     = nodeCount;
        this.adjacencyList = new ArrayList<List<Integer>>();
        
        // Initialize adjacency list
        for (int i = 0; i < nodeCount; i++)
        {
            List<Integer> edgeList = new ArrayList<Integer>();            
            this.adjacencyList.add(edgeList);
        }

        // Populate adjacency list
        if (edges != null)
            for (DirectedEdge e : edges)
            {
                this.addEdge(e);
            }        
    }
    
    public int nodeCount()
    {
        return this.nodeCount;
    }
    
    public boolean hasNode(int node)
    {
        return node >= 0 && node < this.nodeCount;
    }
    
    public void printAdjacencyList()
    {
        for (int i = 0; i < this.adjacencyList.size(); i++)
        {
            System.out.print(i + ": ");
            
            List<Integer> adjacentNodes = this.adjacencyList.get(i);
            
            Iterator<Integer> iter = adjacentNodes.iterator();
            while (iter.hasNext())
            {
                System.out.print(iter.next() + (iter.hasNext()? ", " : ""));
            }
            
            System.out.println();
        }
    }
    
    /*
     *  Returns list of all nodes in the shortest path from departure node to destination node,
     *  in the order they are traversed on the path.
     */
    public List<Integer> findShortestPath(int departure, int destination)
    {
    	// Validate input node values
        int     badNode    = -1;
        boolean hasBadNode = false;
        if (!this.hasNode(departure))
        {
            badNode    = departure;
            hasBadNode = true;
        }
        else if (!this.hasNode(destination))
        {
            badNode    = destination;
            hasBadNode = true;
        }
        
        if (hasBadNode)
        {
            throw new IllegalArgumentException("Cannot find shortest path between nodes " 
                    + departure + " and " + destination + " - graph does not contain node " + badNode);
        }
        
        // Begin breadth-first search
        
        // array indicating whether a given node has been visited 
        boolean[] visited = new boolean[nodeCount];
        
        // instantiate visited array
        for (int i = 0; i < visited.length; i++)
        {
            visited[i] = i == departure;
        }

        boolean          shortestPathFound = false;
        // Set of nodes at current level of BFS
        List<Integer> currentlyVisitedNodes = new LinkedList<Integer>(); 
        // Graph of reversed edges to derive the shortest path via backtracking once it is found.
        DirectedGraph shortestPathTraceGraph = new DirectedGraph(nodeCount, new LinkedList<DirectedEdge>());
        
        currentlyVisitedNodes.add(departure);
        visited[departure] = true;    
        
        // Iterate through BFS procedure
        do 
        {
            Iterator<Integer> currentlyVisitedNodesIter = currentlyVisitedNodes.iterator();
            // Nodes for next level of BFS iteration
            List<Integer>  nextVisitedNodes = new ArrayList<Integer>();
            
            // Add every unvisited node adjacent to the currently visited notes 
            // to the list of nodes to visit next
            while (currentlyVisitedNodesIter.hasNext() && !shortestPathFound)
            {
                int currentNode = currentlyVisitedNodesIter.next();
                                
                if (currentNode == destination)
                {
                    shortestPathFound = true;
                }
                else
                {
                    Iterator<Integer> adjacentNodes = this.adjacencyList.get(currentNode).iterator();
                    while (adjacentNodes.hasNext())
                    {            
                        int adjacentNode = adjacentNodes.next();
                        
                        if (!visited[adjacentNode])
                        {
                            nextVisitedNodes.add(adjacentNode);
                            shortestPathTraceGraph.addEdge(new DirectedEdge(adjacentNode, currentNode));    
                            visited[adjacentNode] = true;                            
                        }

                        if (adjacentNode == destination)
                        {
                            shortestPathFound = true;
                        }
                    }                
                }
            }
            
            currentlyVisitedNodes = nextVisitedNodes;
        }
        while (!shortestPathFound && !currentlyVisitedNodes.isEmpty());        
                
        // backtrack to derive the shortest path
        if (shortestPathFound)
        {
            LinkedList<Integer>  shortestPath = new LinkedList<Integer>();
            int                  currentNode  = destination;    
            
            shortestPath.add(currentNode);    
            
            while (currentNode != departure)
            {
                List<Integer> outgoingEdges = shortestPathTraceGraph.getAdjacentNodes(currentNode);    
                currentNode = outgoingEdges.get(0);
                shortestPath.push(currentNode);
            }
            
            return shortestPath;
        }
        else
        {
            return null;
        }
    }

    /*
     *  Adds an edge to the graph. Throws exception if nodes
     *  in edge are invalid or if edge already exists.
     */
    public void addEdge(DirectedEdge edge)
    {
        addEdge(edge.getStart(), edge.getEnd());
    }

    /*
     *  Adds an edge to the graph. Throws exception if nodes
     *  in edge are invalid or if edge already exists.
     */
    public void addEdge(int start, int end)
    {
        if (!this.hasNode(start) || !this.hasNode(end))
        {
            throw new IllegalArgumentException("Invalid edge: (" + start + ", " + end 
                    + "). Nodes referenced in edges must be numbered betweed 0 and one less than # "
                    + "of nodes in the graph.");
        }
        
        List<Integer> adjacentNodes = this.adjacencyList.get(start);
        if (adjacentNodes.contains(end))
        {
            throw new IllegalArgumentException("Duplicate edge: (" + start + ", " + end 
                    + ") - Graph cannot contain duplicate edges.");                
        }
        this.adjacencyList.get(start).add(end);
    }
    
    /*
     *  Removes the edge if it exists, does nothing otherwise.
     */
    public void removeEdge(DirectedEdge edge)
    {
        removeEdge(edge.getStart(), edge.getEnd());
    }

    /*
     *  Removes the edge if it exists, does nothing otherwise.
     */
    void removeEdge(int start, int end)
    {        
    	if (this.adjacencyList.get(start).contains(end))
    		this.adjacencyList.get(start).remove((Integer) end);
    }

    /*
     *  Returns list of nodes (integers) adjacent to the given node
     */
    public List<Integer> getAdjacentNodes(int node)
    {
        return this.adjacencyList.get(node);
    }
    
//    /*
//     *  Main method for testing
//     */
//    public static void main(String[] args)
//    {
//        List<DirectedEdge> edges = new ArrayList<DirectedEdge>();
//
//        Random r = new Random();
//        for (int i = 0; i < 50; i++)
//        {
//            DirectedEdge newEdge = new DirectedEdge(r.nextInt(20), r.nextInt(20));
//            if (!edges.contains(newEdge))
//                edges.add(newEdge);
//        }
//        
//        DirectedGraph g = new DirectedGraph(20, edges);
//        
//        g.printAdjacencyList();
//        
//        System.out.println();
//        
//        List<Integer> shortestPath = g.findShortestPath(2, 1);
//        System.out.println(shortestPath);
//    }
}
