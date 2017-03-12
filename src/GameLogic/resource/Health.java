package GameLogic.resource;

import GameLogic.*;

/**
 * Class which represents the health of the ship.
 * @author Ivan Panchev
 */
public class Health extends Resource {
    private static final int MAXIMUM = 100;
    private static final int INITIAL = 50;
    
    public Health() {
        super(MAXIMUM, INITIAL);
    }
}
