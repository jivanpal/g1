package GameLogic.resource;

import GameLogic.*;

/**
 * Class which represents the shields of the ship.
 * @author Ivan Panchev
 */
public class Shields extends Resource {
    private static final int MAXIMUM = 100;
    private static final int INITIAL = 50;
    
    public Shields() {
        super(MAXIMUM, INITIAL);
    }
}
