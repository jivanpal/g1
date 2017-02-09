package GameLogic;

/**
 * A simple container for a bounded integer.
 * Used to represent weapon levels and capacity.
 * @author jivan
 *
 */
public class Resource {
/// FIELDS
    private int level;
    private int max;
    
/// CONSTRUCTORS
    
    /**
     * Create a resource with given maximum and initial levels.
     */
    public Resource (int max, int level) {
        setMax(max);
        set(level);
    }
    
    /**
     * Create a resource with given maximum level, with initial level set to 0.
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
     * @param   level   The level to set this instance to.
     *              If it is below 0, the level will be set to 0.
     *              If it is above the current maximum level,
     *              it will be set to the maximum level.
     */
    public void set(int level) {
        this.level =
            level < 0   ?   0:
            level > max ?   max:
                            level;
    }
    
    /**
     * Change the level of this instance by the given amount.
     * @param   delta   The amount to change this instance's level by.
     *              If positive, it is increased.
     *              If negative, it is decreased.
     */
    public void change(int delta) {
        set(level + delta);
    }
    
    /**
     * Increase this instance's level by 1.
     */
    public void up() {
        this.change(1);
    }
    
    /**
     * Decrease this instance's level by 1.
     */
    public void down() {
        this.change(-1);
    }
    
    /**
     * Set/change the maximum level of this instance.
     * @param   max The new maximum level.
     */
    public void setMax(int max) {
        this.max = max;
    }
}
