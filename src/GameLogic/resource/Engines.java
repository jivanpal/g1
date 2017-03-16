package GameLogic.resource;

import GameLogic.Resource;

/**
 * Class which represents the engines of the ship.
 * @author Ivan Panchev
 */
public class Engines extends Resource {
    private static final int MAXIMUM = 20;
    private static final int INITIAL = 10;
    
    private boolean isWorking; // Whether or not it is working properly 
    
    public Engines() {
        super(MAXIMUM, INITIAL);
        isWorking = true; 
    }
    
    public boolean isWorking(){
        return isWorking;
    }
}
