 package GameLogic;

import Geometry.*;
import Physics.*;

/**
 * A class of objects representing laser blaster weapons attached to some ship.
 */
public class TorpedoWeapon extends Weapon {
    private static final Bullet     REFERENCE_BULLET = null;
    private static final int        MAX_AMMO        = 30;
    private static final int        INIT_AMMO       = 20;
    private static final int        SHIELD_DAMAGE   = 5;
    private static final int        SHIP_DAMAGE     = 10;
    private static final boolean    AUTO_TARGET     = false;
    private static final boolean    AUTO_RELOAD     = true;
    private static final double     COOLDOWN        = 0.01;
    
    public TorpedoWeapon(
        Ship parent
    ) {
        super(
            parent,
            REFERENCE_BULLET,
            MAX_AMMO,
            INIT_AMMO,
            SHIELD_DAMAGE,
            SHIP_DAMAGE,
            AUTO_RELOAD,
            AUTO_TARGET,
            COOLDOWN
        );
    }
}
