package GameLogic;

import java.io.Serializable;

/**
 * A simple container for a bounded integer.
 * Used to represent resources that can take integral
 * values between 0 and some upper bound, such as ammo
 * levels or health.
 * 
 * @author Ivan Panchev
 * @author jivan
 */
public class Resource implements Serializable {
/// FIELDS
    private int level;
    private int max;
    
/// CONSTRUCTORS
    
    /**
     * Create a resource with given maximum and initial levels.
     * @param max the maximum level for this instance.
     * @param level the initial level of this resource. Negative values or
     *      values above the specified maximum will result in the initial
     *      level being set to the maximum. 
     */
    public Resource (int max, int level) {
        setMax(max);
        set(level < 0 ? 0 : level);
    }
    
    /**
     * Create a resource with a given maximum level and initial level set to 0.
     * @param max the maximum level for this instance.
     */
    public Resource (int max) {
        this(max, 0);
    }
    
/// INSTANCE METHODS
    
// Getters
    
    /**
     * Get the current level of this instance.
     */
    public int get() {
        return level;
    }
    
    /**
     * Get the maximum level of this instance.
     */
    public int getMax() {
        return max;
    }
    
    /**
     * Get a number representing how full this instance is.
     * @return  a double between 0.0 and 1.0 (inclusive) representing how full this instance is.
     */
    public double getFraction() {
        return (double)level/max;
    }
    
// Setters
    
    /**
     * Set the level of this instance.
     * @param   level   The level to set this instance to. Values below 0 will set
     *      the level to 0, and values above the maximum will set it to the maximum.
     */
    public void set(int level) {
        this.level =
            level < 0   ?   0:
            level > max ?   max:
                            level;
    }
    
    /**
     * Change the level of this instance by the given amount.
     * @param delta the amount to change this instance's level by.
     *      Positive values increase the level, and negative values decrease it.
     */
    public void alter(int delta) {
        set(level + delta);
    }
    
    /**
     * Increase this instance's level by 1.
     */
    public void increase() {
        this.alter(1);
    }
    
    /**
     * Decrease this instance's level by 1.
     */
    public void decrease() {
        this.alter(-1);
    }
    
    /**
     * Set/change the maximum level of this instance.
     * @param   max The new maximum level.
     */
    public void setMax(int max) {
        this.max = max;
    }
    
/// PREDEFINED RESOURCE TYPES
    public enum Type {
        ENGINES, SHIELDS, HEALTH;
    }
}
