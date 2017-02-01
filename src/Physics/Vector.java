package Vector;

public class Vector {
// fields
    private final double x, y, z;
    
// constructors
    
    /**
     * Create a new vector with the given components.
     * @param x The x component.
     * @param y The y component.
     * @param z The z component.
     */
    public Vector (double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
// getters
    /**
     * Get the x-component of the vector
     * @return Returns the x-component of the vector.
     */
    public double getX() {
        return x;
    }
    
    /**
     * Get the y-component of the vector
     * @return Returns the y-component of the vector.
     */
    public double getY() {
        return y;
    }
    
    /**
     * Get the z-component of the vector
     * @return Returns the z-component of the vector.
     */
    public double getZ() {
        return z;
    }
    
// algebraic methods
    
    /**
     * Scales the vector by a given scalar.
     * @param scalar The scalar.
     * @return The requested scalar multiple of the vector.
     */
    public Vector scale(double scalar) {
        return new Vector (
            scalar * x,
            scalar * y,
            scalar * z
        );
    }
    
    /**
     * Add this vector with another; get their sum.
     * @param v The vector to add.
     * @return The sum of the two vectors involved.
     */
    public Vector add(Vector v) {
        return new Vector (
            x + v.getX(),
            y + v.getY(),
            z + v.getZ()
        );
    }
    
    /**
     * Dot this vector with another; get their scalar product.
     * @param v The vector to dot with.
     * @return The dot product (scalar product) of the two vectors.
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
     * @param v The vector to cross with.
     * @return The cross product (vector product) of the two vectors, in the order given.
     */
    public Vector cross(Vector v) {
        double resultX = y * v.getZ() - z * v.getY();
        double resultY = z * v.getX() - x * v.getZ();
        double resultZ = x * v.getY() - y * v.getX();
        
        return new Vector (resultX, resultY, resultZ);
    }
}