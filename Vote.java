public class Vote 
{
    private Episode  ep;
    private int      keepThisGuy;
    private int      kickThisGuy;
    private VoteType forCatOrDog;

    private int      catChoice;
    private int      dogChoice;
    
    public Vote(Episode ep, int keepThisGuy, int kickThisGuy, VoteType forCatOrDog)
    {
        this.ep          = ep;
        this.keepThisGuy = keepThisGuy;
        this.kickThisGuy = kickThisGuy;
        this.forCatOrDog = forCatOrDog; 

        this.catChoice = forCatOrDog == VoteType.forCat? keepThisGuy : kickThisGuy;
        this.dogChoice = forCatOrDog == VoteType.forDog? keepThisGuy : kickThisGuy;
        
        validateVote();
    }
    
    public Vote(int keepThisGuy, int kickThisGuy)
    {
        this.keepThisGuy = keepThisGuy;
        this.kickThisGuy = kickThisGuy;
    }

    public int getVoteToKeep()
    {
        return keepThisGuy;
    }
    
    public int getVoteToKick()
    {
        return kickThisGuy;
    }

    public int getCatChoice()
    {
        return this.catChoice;
    }
    
    public int getDogChoice()
    {
        return this.dogChoice;
    }

    public VoteType getVoteType()
    {
        return forCatOrDog;
    }
    
    @Override
    public String toString()
    {
        return "(" + (forCatOrDog == VoteType.forCat? "C" : "D") + (this.keepThisGuy + 1) + ", "
                   + (forCatOrDog == VoteType.forCat? "D" : "C") + (this.kickThisGuy + 1) + ")";
    }    
    
    /*
     *  checks to see if the cat/dog IDs exist in the associated voting round, and
     *  throws an exception if they don't.
     */
    private void validateVote()
    {
        int catVote = this.getVoteType() == VoteType.forCat? this.getVoteToKeep() : this.getVoteToKick();
        int dogVote = this.getVoteType() == VoteType.forDog? this.getVoteToKeep() : this.getVoteToKick();

        if (catVote < 0 || catVote >= ep.getNumCats())
        {
            throw new IllegalArgumentException("Invalid vote: " + this + " - cat \'C" + catVote + "\' does not exist");
        }
        
        if (dogVote < 0 || dogVote >= ep.getNumDogs())
        {
            throw new IllegalArgumentException("Invalid vote: " + this + " - dog \'D" + dogVote + "\' does not exist");
        }
    }
    
//    /*
//     *  Main method for testing only.
//     */
//    public static void main(String[] args)
//    {
//        Episode ep = new Episode(234, 234);
//        Vote v = new Vote(ep, 3, 5, VoteType.forDog);
//        
//        System.out.println(v);
//    }
}
