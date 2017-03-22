package GameLogic.weapon;

import ClientNetworking.GameHost.MapContainer;
import GameLogic.*;
import Geometry.Vector;

/**
 * A class of objects representing plasma blaster weapons attached to some ship.
 * @author Ivan Panchev
 */
public class Plasma extends Weapon {
// Weapon constants
    private static final int    MAX_AMMO        = 50;
    private static final int    INIT_AMMO       = 30;
    private static final double COOLDOWN        = 1.5;  // s
    private static final int 	RELOAD_AMOUNT   = 1;
    
// Bullet constants
    private static final double MASS            = 0.3;  // kg
    private static final double RADIUS          = 1;    // m
    private static final double SPEED           = 250;  // m/s
    private static final int    SHIP_DAMAGE     = 2;
    private static final int    SHIELD_DAMAGE   = 10;
    private static final double MAP_SPAN_FRAC   = 0.3;  // fraction of map to travel across
    private static final double TIME_TO_LIVE    = MapContainer.MAP_SIZE * MAP_SPAN_FRAC / SPEED;
    
    private static final Bullet BULLET = new Bullet(
        MASS,
        RADIUS,
        Vector.J.scale(SPEED),
        Vector.ZERO,
        Weapon.Type.PLASMA,
        SHIP_DAMAGE,
        SHIELD_DAMAGE,
        TIME_TO_LIVE
    );
    
    public Plasma(Ship parent) {
        super(
            parent,
            BULLET,
            MAX_AMMO,
            INIT_AMMO,
            RELOAD_AMOUNT,
            COOLDOWN
        );
    }
}
