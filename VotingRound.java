import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
 *  Represents the round of voting in a given catvsdog episode.
 */
public class VotingRound 
{
    private List<Vote> votes;
    private Episode    ep;

    public VotingRound(Episode ep)
    {
        this.ep    = ep;
        this.votes = new ArrayList<Vote>();
    }
    
    public void addVote(Vote v)
    {
        this.votes.add(v);
    }

    public void addVote(int keepThisGuy, int kickThisGuy, VoteType forCatOrDog)
    {
        this.votes.add(new Vote(this.ep, keepThisGuy, kickThisGuy, forCatOrDog));
    }

    public List<Vote> getVotes()
    {
        return this.votes;
    }
    
    public int getNumCats()
    {
        return this.ep.getNumCats();
    }
    
    public int getNumDogs()
    {
        return this.ep.getNumDogs();
    }
}
