package GameLogic.weapon;

import GameLogic.*;

/**
 * A class of objects representing plasma blaster weapons attached to some ship.
 * @author Ivan Panchev
 */
public class Plasma extends Weapon {
    private static final Bullet BULLET          = null;
    private static final int    AMMO_MAX        = 30;
    private static final int    AMMO_INIT       = 30;
    private static final int    DAMAGE_SHIELDS  = 5;
    private static final int    DAMAGE_SHIP     = 10;
    private static final double COOLDOWN        = 0.01;
    
    public Plasma(Ship parent) {
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
