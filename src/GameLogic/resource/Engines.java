package GameLogic.resource;

import GameLogic.Resource;

/**
 * Class which represents the engines of the ship.
 * @author Ivan Panchev
 */
public class Engines extends Resource {
    private static final int MAXIMUM = 1000;
    private static final int INITIAL = 800;
    
    private boolean isWorking; // Whether or not it is working properly 
    
    public Engines() {
        super(MAXIMUM, INITIAL);
        isWorking = true; 
    }

    @Override
    public void increase() {
        this.alter(10);
    }

    public boolean isWorking(){
        return isWorking;
    }
}