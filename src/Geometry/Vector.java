package Geometry;

public class Vector {
/// CONSTANTS
    
    /**
     * The zero vector, (0,0,0).
     */
    public static final Vector ZERO    = new Vector (0, 0, 0);
    
    /**
     * The unit vector in the x-direction, (1,0,0).
     */
    public static final Vector I       = new Vector (1, 0, 0);
    
    /**
     * The unit vector in the y-direction, (0,1,0).
     */
    public static final Vector J       = new Vector (0, 1, 0);
    
    /**
     * The unit vector in the z-direction, (0,0,1).
     */
    public static final Vector K       = new Vector (0, 0, 1);
    
/// FIELDS
    private final double x, y, z;
    
/// CONSTRUCTORS
    
    /**
     * Create a new vector with the given components.
     * @param   x   The x component.
     * @param   y   The y component.
     * @param   z   The z component.
     */
    public Vector (double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
/// STATIC METHODS
    
    /**
     * Get the sum of an array of vectors.
     * @param   v   An array of vectors to sum.
     * @return  Returns the sum of the vectors in `v`.
     */
    public static Vector sum(Vector[] v) {
        return _sum(v, v.length);
    }
    
    /**
     * Helper method for `sum(Vector[])`.
     */
    private static _sum(Vector[] v, int n) {
        return n == 0   ? Vector.ZERO   : v[n-1].plus(_sum(v, n-1));
    }
    
/// INSTANCE METHODS
    
    /**
     * Check whether this vector is equal to another vector.
     * @param   v   The vector to compare with.
     * @return  Returns true when all corresponding components
     *          of the two vectors are equal.
     */
    public boolean equals(Vector v) {
        return x == v.getX()
            && y == v.getY()
            && z == v.getZ();
    }
    
// Getters
    
    /**
     * Get the x-component of the vector.
     * @return  Returns the x-component of the vector.
     */
    public double getX() {
        return x;
    }
    
    /**
     * Get the y-component of the vector.
     * @return  Returns the y-component of the vector.
     */
    public double getY() {
        return y;
    }
    
    /**
     * Get the z-component of the vector.
     * @return  Returns the z-component of the vector.
     */
    public double getZ() {
        return z;
    }
    
// Properties / Nullary Methods
    
    /**
     * Get the length of the vector.
     * @return  Returns the length (magnitude) of the vector.
     */
    public double length() {
        return Math.sqrt(x^2 + y^2 + z^2);
    }
    
    /**
     * Get the unit vector in the same direction as this one.
     * @return  Returns the vector scaled down by its length.
     *          If this vector is the zero vector, return the zero vector.
     */
    public Vector normalise() {
        return this.equals(Vector.ZERO) ? this  : this.scale( 1 / length() );
    }
    
    /**
     * Get the additive inverse of the vector; get its negation.
     * @return  Returns the vector scaled by -1.
     */
    public Vector negate() {
        return this.scale(-1);
    }
    
// Algebraic Methods
    
    /**
     * Get a scalar multiple of the vector.
     * @param   scalar  The proportion to scale the vector by.
     * @return  Returns the corresponding scalar multiple of the vector.
     */
    public Vector scale(double scalar) {
        return new Vector (
            scalar * x,
            scalar * y,
            scalar * z
        );
    }
    
    /**
     * Add another vector to this one; get their sum.
     * @param   v   The vector to add.
     * @return  Returns the sum of the two vectors.
     */
    public Vector plus(Vector v) {
        return new Vector (
            x + v.getX(),
            y + v.getY(),
            z + v.getZ()
        );
    }
    
    /**
     * Subtract another vector from this one; get their difference.
     * @param   v   The vector to subtract.
     * @return  Returns the difference of the two vectors.
     */
    public Vector minus(Vector v) {
        return this.plus(v.negate());
    }
    
    /**
     * Dot this vector with another; get their scalar product.
     * @param   v   The vector to dot with.
     * @return  Returns the dot product (scalar product) of the two vectors.
     */
    public double dot(Vector v) {
        return  x * v.getX()
            +   y * v.getY()
            +   z * v.getZ();
    }
    
    /**
     * Cross this vector with another; get their vector product.
     * Note that, given vectors `u` and `v`, `u.cross(v)` returns
     * the result of the mathematical operation u×v, whereas
     * `v.cross(u)` reuturns the result v×u = -u×v.
     *
     * @param   v   The vector to cross with.
     * @return  Returns the cross product (vector product) of this
     *          vector with the argument vector.
     */
    public Vector cross(Vector v) {
        return new Vector (
            y * v.getZ() - z * v.getY(),
            z * v.getX() - x * v.getZ(),
            x * v.getY() - y * v.getX()
        );
    }
    
    /**
     * Get the vector with components modulo the corresponding
     * component of the argument vector.
     *
     * In other words, if `this` is interpreted as a displacement
     * from the origin in a looping space with dimensions described by
     * `v`, return the smallest postive vector that gives
     * the same displacement as `this`. This is useful for implementing
     * the looping map behaviour.
     *
     * @param   v   The vector describing the dimensions of the space.
     * @return  Returns the smallest congruent vector in that space.
     */
    public Vector modulo(Vector v) {
        return new Vector(
            x % v.getX() + (x % v.getX() < 0 ? v.getX() : 0),
            y % v.getY() + (y % v.getY() < 0 ? v.getY() : 0),
            z % v.getZ() + (z % v.getZ() < 0 ? v.getZ() : 0)
        )
    }
}
