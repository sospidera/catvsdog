public class DirectedEdge 
{
    private int startNode;
    private    int endNode;
    
    public DirectedEdge(int startNode, int endNode)
    {
        this.startNode = startNode;
        this.endNode   = endNode;
    }

    public int getStart()
    {
        return this.startNode;
    }
    
    public int getEnd()
    {
        return this.endNode;
    }
    
    @Override
    public boolean equals(Object e)
    {
        if (!(e instanceof DirectedEdge))
            return false;
        
        DirectedEdge _e = (DirectedEdge) e;
        
        return this.startNode == _e.getStart() && this.endNode == _e.getEnd();
    }
    
    @Override
    public String toString()
    {
        return "(" + this.startNode + ", " + this.endNode + ")";
    }
}
