package Physics;

/**
 * These values are needed to restrict the player's max speed,
 * as well as changes in speed resulting from collisions and similar.
 * These values will likely need to be changed during playtesting, as they
 * may either by far too low, or far too high.
 */
public class UpperBound {
    public static final double MASS, VELOCITY;
    
    MASS = 50000000;    // 50 000 000 kilograms = 50 megatonnes
    VELOCITY = 10000;   // 10 000 m/s           = 10 km/s ≈ 22 400 mph
    
}