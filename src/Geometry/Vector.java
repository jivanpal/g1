package Geometry;

import java.io.Serializable;

/**
 * A class of objects to describe 3D Euclidean vectors.
 * @author jivan
 *
 */
public class Vector implements Serializable {
/// CONSTANTS

    /**
     * The zero vector, (0,0,0).
     */
    public static final Vector ZERO = new Vector(0, 0, 0);

    /**
     * The unit vector in the x-direction, (1,0,0).
     */
    public static final Vector I    = new Vector(1, 0, 0);

    /**
     * The unit vector in the y-direction, (0,1,0).
     */
    public static final Vector J    = new Vector(0, 1, 0);

    /**
     * The unit vector in the z-direction, (0,0,1).
     */
    public static final Vector K    = new Vector(0, 0, 1);

/// FIELDS
    private final double x, y, z;

/// CONSTRUCTORS

    /**
     * Create a new vector with the given components.
     * @param   x   The x component.
     * @param   y   The y component.
     * @param   z   The z component.
     */
    public Vector(double x, double y, double z) {
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
    private static Vector _sum(Vector[] v, int n) {
        return n == 0 ? Vector.ZERO : v[n - 1].plus(_sum(v, n - 1));
    }
    
    /**
     * Get the determinant of the 3x3 matrix whose columns are described by the given vectors.
     * @param col1 the vector describing the first column.
     * @param col2 the vector describing the second column.
     * @param col3 the vector describing the third column.
     * @return the determinant of the matrix described by the vectors.
     */
    public static double det(Vector col1, Vector col2, Vector col3) {
    	return col1.x*(col2.y*col3.z - col2.z*col3.y) + col1.y*(col2.z*col3.x - col2.x*col3.z) + col1.z*(col2.x*col3.y - col2.y*col3.x);
    }

/// INSTANCE METHODS

// Overrides
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Vector v = (Vector) obj;
        return x == v.getX()
            && y == v.getY()
            && z == v.getZ();
    }
    
    /**
     * Get a human-readable version of the vector.
     * @return  the string "(x,y,z)", where 'x', 'y', and 'z' are the
     *          values of the vector's components.
     */
    public String toString() {
        return "("+String.format("%.5f", x)+", "+String.format("%.5f", y)+", "+String.format("%.5f", z)+")";
    }

// Getters

    /**
     * Get the x-component of the vector.
     */
    public double getX() {
        return x;
    }

    /**
     * Get the y-component of the vector.
     */
    public double getY() {
        return y;
    }

    /**
     * Get the z-component of the vector.
     */
    public double getZ() {
        return z;
    }

// Properties / Nullary Methods

    /**
     * Get the length/norm/magnitude of the vector.
     */
    public double length() {
        return Math.sqrt(x*x + y*y + z*z);
    }

    /**
     * Get the unit vector in the same direction as this one.
     * @return  Returns the vector with length 1 whose direction
     *          is the same as this one. If this vector is the zero
     *          vector, return the zero vector.
     */
    public Vector normalise() {
        return this.equals(Vector.ZERO) ? this  : this.scale(1 / length());
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
        return new Vector(scalar * x, scalar * y, scalar * z);
    }

    /**
     * Add another vector to this one; get their sum.
     * @param   v   The vector to add.
     * @return  Returns the sum of the two vectors.
     */
    public Vector plus(Vector v) {
        return new Vector(x + v.getX(), y + v.getY(), z + v.getZ());
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
     * 
     * @param   v   The vector to dot with.
     * @return  Returns the dot product (scalar product) of the two vectors.
     */
    public double dot(Vector v) {
        return x * v.getX() + y * v.getY() + z * v.getZ();
    }

    /**
     * Cross this vector with another; get their vector product. Note that,
     * given vectors `u` and `v`, `u.cross(v)` returns the result of the
     * mathematical operation u×v, whereas `v.cross(u)` reuturns the result v×u
     * = -u×v.
     * 
     * @param   v   The vector to cross with.
     * @return  Returns the cross product (vector product) of this vector with
     *          the argument vector.
     */
    public Vector cross(Vector v) {
        return new Vector(y * v.getZ() - z * v.getY(), z * v.getX() - x * v.getZ(), x * v.getY() - y * v.getX());
    }

    /**
     * Get the angle that this vector makes with another vector.
     * @param   v   The other vector.
     * @return  the non-reflex angle spanned in the plane of the two vectors when they
     *      are placed tip-to-tip with each other, measured in radians. As an example,
     *      if <i>u</i> = (2, 0, 0), and <i>v</i> = (-5, 5, 0), then <i>u</i>.angleWith(<i>v</i>)
     *      will return the double-approximation of 3π/4. Since this value is computed
     *      via the dot product, and the dot product is commutative, this operator is
     *      also commutative, <i>i.e.</i> order does not matter; for any <i>u</i> and
     *      <i>v</i>, <i>u</i>.angleWith(<i>v</i>) is equal to <i>v</i>.angleWith(<i>u</i>). 
     */
    public double angleWith(Vector v) {
        return Math.acos(this.dot(v) / ( this.length() * v.length() ));
    }
    
    /**
     * Get the projection of this vector onto another vector.
     * @param   v   The vector to project this vector onto.
     * @return  the projection of this vector onto `v`. That is, the component of this
     *              vector that lies on the line described by `v`.
     */
    public Vector proj(Vector v) {
        return v.scale( v.dot(this) / (v.length() * v.length()) );
    }
}
