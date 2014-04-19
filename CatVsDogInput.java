import java.util.Random;
import java.util.Scanner;

public class CatVsDogInput 
{
    public static void main(String[] args)
    {        
        runInCommandLineMode();
//        runTestCase();
    }
    
    // retrieve input as specified on labs.spotify.com/puzzles and run
    // puzzle solver on input.
    private static void runInCommandLineMode()
    {
        Scanner s = new Scanner(System.in);
        
        // get number of voting rounds
        int episodes = s.nextInt();

        VotingRound[] rounds = new VotingRound[episodes];
        
        while (episodes > 0)
        {
        	// get number of cats/dogs/voters in episode
            int numCats   = s.nextInt();
            int numDogs   = s.nextInt();
            int numVoters = s.nextInt();
            
            Episode ep = new Episode(numCats, numDogs);
            
            // make sure to populate the voting rounds array in the correct order
            rounds[rounds.length - episodes] = new VotingRound(ep);
            
            // populate voting round with audience votes
            while (numVoters > 0)
            {                
                String keep = s.next();
                String kick = s.next();
                
                VoteType type = keep.startsWith("C")? VoteType.forCat : VoteType.forDog;
                rounds[rounds.length - episodes].addVote(Integer.parseInt(keep.substring(1)) - 1, Integer.parseInt(kick.substring(1)) - 1, type);
                
                numVoters--;
            }
            
            episodes--;
        }        
        
        // run puzzle solver on input and output result.
        for (int i = 0; i < rounds.length; i++)
        {
            VoteResultFinder resultFinder = new VoteResultFinder(rounds[i]);
            int result = resultFinder.determineMaxHappyVoterCount();
            System.out.println(result);
        }
    }
    
    // Method for testing puzzle solver
    private static void runTestCase()
    {
        /* it's been such a */ long time /* I think I should be goin' */ = System.currentTimeMillis();
        long compilationTime = 0;
        long solveTime = 0;
        
    	for (int trials = 0; trials < 50; trials++)
    	{
	        int count = 100;
	        Episode epp = new Episode(count, count);
	        
	        VotingRound v = new VotingRound(epp);
	        
	        Random r = new Random();
	        
	        for (int i = 0; i < 500; i++)
	        {
	            v.addVote(r.nextInt(count), r.nextInt(count), r.nextBoolean()? VoteType.forCat : VoteType.forDog);
	//            System.out.println(i + ": " + v.getVotes().get(i));
	        }

	    	long start = System.currentTimeMillis();
	        VoteResultFinder vrf = new VoteResultFinder(v);
	        long end = System.currentTimeMillis();
	        compilationTime += end - start;
	        

	    	start = System.currentTimeMillis();
	        vrf.determineMaxHappyVoterCount();
	        end = System.currentTimeMillis();
	        solveTime += end - start;
    	}
    	
        long elapsed = System.currentTimeMillis() - time;  
        
        System.out.println("time: " + elapsed + " ms, compilationTime: " + compilationTime + ", solveTime: " + solveTime);
    }
}
