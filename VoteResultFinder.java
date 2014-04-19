import java.util.LinkedList;
import java.util.List;

/*
 *  Main puzzle solver class. Generates maximum satisfied voters using the
 *  following procedure:
 *  
 *  - Constructs a bipartite graph where the first set of nodes represents
 *    all voters who voted for cats, and the second set represents all those
 *    who voted for dogs. an edge from node A to node B indicates that one voter
 *    in the edge voted for an animal that the other voter voted against.
 *  - To prep for a maximum-matching algorithm, the edges are directed from the
 *    voter who voted for a cat to the one who voted for the dog, so all edges
 *    flow from one of the bipartite graph's node sets to the other.
 *  - Since two voters can be satisfied simultaneously if and only if they are
 *    not connected in this graph, the maximum satisfied voter count is the 
 *    graph's maximum independent set. While there is no general polynomial-time 
 *    algorithm known to find this, there is a workaround for bipartite graphs:
 *    number of nodes in the max independent set of a bipartite graph is equal to
 *    the number nodes in the graph minus the number of nodes in its minimum 
 *    vertex cover, and the latter is in turn equal to the number of edges in the
 *    maximal matching of the (undirected) graph, since it is bipartite (Konig's
 *    theorem). Since the maximum matching problem can be solved for bipartite
 *    graphs in polynomial time, a version of the augmented-path algorithm given
 *    on page 4-3 of http://www.columbia.edu/~cs2035/courses/ieor8100.F12/lec4.pdf
 *    is used to find the number of edges in a maximum matching for this graph, and
 *    return (<number of nodes in graph> - <number of edges in maximum matching>)
 *    as the result.
 */

public class VoteResultFinder 
{    
    DirectedGraph voterGraph;  // Graph of voters described above.
    int           sourceNode;  // Nodes added to voter graph for 
    int           sinkNode;    // maximum matching algorithm.
    VotingRound   votingRound; 
    
    public VoteResultFinder(VotingRound votingRound)
    {    	
        List<Vote> votes     = votingRound.getVotes();
        int        voteCount = votes.size();
        
        this.voterGraph  = new DirectedGraph(2 + voteCount, null); // one node per voter + source & sink nodes
        this.sourceNode  = this.voterGraph.nodeCount() - 1;
        this.sinkNode    = this.voterGraph.nodeCount() - 2;
        this.votingRound = votingRound;
        
        // Populate voter adjacency graph
        for (int i = 0; i < voteCount; i++)
        {
            Vote v1 = votes.get(i);
            for (int j = 0; j < voteCount; j++)
            {
                Vote v2 = votes.get(j);
                if (v1.getVoteType() != v2.getVoteType() && v1.getVoteToKick() == v2.getVoteToKeep() && i != j)
                {
                    DirectedEdge newEdge;
                    if (v1.getVoteType() == VoteType.forCat)
                        newEdge = new DirectedEdge(i, j); 
                    else // v1.getVoteType() == VoteType.forDog)
                        newEdge = new DirectedEdge(j, i);    
                    
                    // conditional addresses the case where two voters have opposing votes
                    if (!this.voterGraph.getAdjacentNodes(newEdge.getStart()).contains(newEdge.getEnd()))
                        this.voterGraph.addEdge(newEdge);
                }
            }
        }
        
        // connect source and sink nodes for maximum matching alorithm
        for (int i = 0; i < voteCount; i++)
        {
            if (votes.get(i).getVoteType() == VoteType.forCat)
                this.voterGraph.addEdge(new DirectedEdge(sourceNode, i));
            else // votes.get(i).getVoteType() == VoteType.forDog)
                this.voterGraph.addEdge(new DirectedEdge(i, sinkNode));
        }        
    }
    
    /*
     *  Solves the maximum voter satisfaction problem for the given voter set
     *  using the process described at the top of this file.
     */
    public int determineMaxHappyVoterCount()
    {            
        List<Integer>    shortestPath;
        // Nodes connected to edges in the current matching. Will ultimately be the set of
        // nodes connected to edges in the maximum matching (Note that the actual edges
        // need not be recorded)
        List<Integer> maximumMatchingNodes = new LinkedList<Integer>(); 
        
        shortestPath = this.voterGraph.findShortestPath(sourceNode, sinkNode);
        
        // find augmenting paths and add associated endpoint nodes to the max-matching node set
        while (shortestPath != null)
        {            
            for (int i = 1; i < shortestPath.size() - 2; i++)
            {
                DirectedEdge shortestPathEdge      = new DirectedEdge(shortestPath.get(i), shortestPath.get(i + 1));
                int          shortestPathEdgeStart = shortestPathEdge.getStart();
                int          shortestPathEdgeEnd   = shortestPathEdge.getEnd();                
                DirectedEdge reversedEdge          = new DirectedEdge(shortestPathEdgeEnd, shortestPathEdgeStart);
                
                this.voterGraph.removeEdge(shortestPathEdge);
                this.voterGraph.addEdge(reversedEdge);
                
                // update matching node set and remove corresponding source/sink edges
                if (i == 1)
                {
                    maximumMatchingNodes.add(shortestPathEdgeStart);
                    this.voterGraph.removeEdge(new DirectedEdge(this.sourceNode, shortestPathEdgeStart));
                }
                if (i == shortestPath.size() - 3)
                {
                    maximumMatchingNodes.add(shortestPathEdgeEnd);
                    this.voterGraph.removeEdge(new DirectedEdge(shortestPathEdgeEnd, this.sinkNode));
                }
            }
            
            shortestPath = this.voterGraph.findShortestPath(sourceNode, sinkNode);
        }
        
        // The number of edges in the maximum matching is simply half the number of
        // nodes that appear in the edges of the matching, so return the following
        // value, explained in the comments at the top of the file to be equal to
        // the maximum number of satisfiable voters.
        return this.votingRound.getVotes().size() - maximumMatchingNodes.size() / 2;
    }
    
//    /*
//     *  Main method for testing only
//     */
//    public static void main(String[] args)
//    {
//        Episode ep = new Episode(6, 6);
//
//        VotingRound votes = new VotingRound(ep);
//
//        votes.addVote(1, 3, VoteType.forCat);
//        votes.addVote(1, 3, VoteType.forCat);
//        votes.addVote(1, 3, VoteType.forCat);
//        votes.addVote(1, 3, VoteType.forCat);
//        votes.addVote(1, 3, VoteType.forCat);
//        votes.addVote(1, 3, VoteType.forCat);
//        votes.addVote(5, 4, VoteType.forDog);
//        votes.addVote(5, 4, VoteType.forDog);
//        votes.addVote(5, 4, VoteType.forDog);
//        votes.addVote(5, 4, VoteType.forDog);
//        votes.addVote(5, 4, VoteType.forDog);
//        votes.addVote(5, 4, VoteType.forDog);
//        
//        VoteResultFinder solver = new VoteResultFinder(votes);
//        
//        solver.determineMaxHappyVoterCount();
//    }
}
