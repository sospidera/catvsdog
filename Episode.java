public class Episode
{
    private int numCats;
    private int numDogs;
        
    /*
     *  Represents an 'episode' in the catvsdog reality show.
     *  Used to put votes in the context of a certain number
     *  of cats and dogs, and validate that the cat/dog IDs
     *  are valid.
     */
    public Episode(int numCats, int numDogs)
    {
        this.numCats = numCats;
        this.numDogs = numDogs;
    }
                
    public int getNumCats()
    {
        return numCats;
    }
    
    public int getNumDogs()
    {
        return numDogs;
    }
    
//    /*
//     *  Main method for testing only
//     */
//    public static void main(String[] args)
//    {
//        
//    }
}
