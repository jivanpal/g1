package GameLogic.weapon;

import GameLogic.*;

/**
 * A class of objects representing laser blaster weapons attached to some ship.
 * @author Ivan Panchev
 */
public class Laser extends Weapon {
    private static final Bullet BULLET          = null;
    private static final int    AMMO_MAX        = 30;
    private static final int    AMMO_INIT       = 20;
    private static final int    DAMAGE_SHIELDS  = 5;
    private static final int    DAMAGE_SHIP     = 10;
    private static final double COOLDOWN        = 0.01;
    
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
