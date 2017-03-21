package GameLogic.weapon;

import GameLogic.*;
import Geometry.Vector;
import Physics.Body;

/**
 * A class of objects representing laser blaster weapons attached to some ship.
 * @author Ivan Panchev
 */
public class Laser extends Weapon {
// Weapon constants
    private static final int    AMMO_MAX        = 100;
    private static final int    AMMO_INIT       = 100;
    private static final int    DAMAGE_SHIELDS  = 5;
    private static final int    DAMAGE_SHIP     = 10;
    private static final double COOLDOWN        = 0.5;
    
// Bullet constants
    private static final int    SHIP_DAMAGE     = 10;
    private static final int    SHIELD_DAMAGE   = 5;
    private static final double TIME_TO_LIVE    = 0.4;
    private static final double MASS            = 0.1;
    private static final double RADIUS          = 1;
    private static final double SPEED           = 200;
    
    private static final Bullet BULLET = new Bullet(
        SHIP_DAMAGE,
        SHIELD_DAMAGE,
        TIME_TO_LIVE,
        MASS,
        RADIUS,
        Vector.J.scale(SPEED),
        Vector.ZERO
    );
    
    public Laser(Ship parent) {
        super(
            parent,
            BULLET,
            AMMO_MAX,
            AMMO_INIT,
            DAMAGE_SHIELDS,
            DAMAGE_SHIP,
            COOLDOWN
        );
    }
}
