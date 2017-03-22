package GameLogic.weapon;

import ClientNetworking.GameHost.MapContainer;
import GameLogic.*;
import Geometry.*;

/**
 * A class of objects representing laser blaster weapons attached to some ship.
 * @author Ivan Panchev
 */
public class Laser extends Weapon {
// Weapon constants
    private static final int    MAX_AMMO        = 100;
    private static final int    INIT_AMMO       = 100;
    private static final double COOLDOWN        = 0.1;  // s
    
// Bullet constants
    private static final double MASS            = 0.1;  // kg
    private static final double RADIUS          = 1;    // m
    private static final double SPEED           = 500;  // m/s
    private static final int    SHIP_DAMAGE     = 10;
    private static final int    SHIELD_DAMAGE   = 5;
    private static final double MAP_SPAN_FRAC   = 0.98; // fraction of map to travel across
    private static final double TIME_TO_LIVE    = MapContainer.MAP_SIZE * MAP_SPAN_FRAC / SPEED; // s (says span the map, less 5 meters)
    
    private static final Bullet BULLET = new Bullet(
        MASS,
        RADIUS,
        Vector.J.scale(SPEED),
        Vector.ZERO,
        SHIP_DAMAGE,
        SHIELD_DAMAGE,
        TIME_TO_LIVE
    );
    
    public Laser(Ship parent) {
        super(
            parent,
            BULLET,
            MAX_AMMO,
            INIT_AMMO,
            COOLDOWN
        );
    }
}
