package GameLogic.weapon;

import GameLogic.*;
import Physics.Body;

/**
 * A class of objects representing laser blaster weapons attached to some ship.
 * @author Ivan Panchev
 */
public class Laser extends Weapon {
    private static final Bullet BULLET          = new Bullet(2, 2, new Body(10, 2));
    private static final int    AMMO_MAX        = 100;
    private static final int    AMMO_INIT       = 100;
    private static final int    DAMAGE_SHIELDS  = 5;
    private static final int    DAMAGE_SHIP     = 10;
    private static final double COOLDOWN        = 0.15d;
    
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
