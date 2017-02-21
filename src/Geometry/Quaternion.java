package Geometry;

import java.io.Serializable;

/**
 * A class of objects that describe Hamiltonian quaternions. These are useful
 * for implementing rotations in a way that reduces the number of degrees of
 * freedom whilst also avoiding problems such as gimbal lock.
 * 
 * @author jivan
 *
 */
public class Quaternion implements Serializable {
/// CONSTANTS
    public static final Quaternion ZERO = new Quaternion (0, 0, 0, 0);
    public static final Quaternion UNIT = new Quaternion (1, 0, 0, 0);
    public static final Quaternion I    = new Quaternion (0, 1, 0, 0);
    public static final Quaternion J    = new Quaternion (0, 0, 1, 0);
    public static final Quaternion K    = new Quaternion (0, 0, 0, 1);

/// FIELDS
    private final double w, x, y, z;

// CONSTRUCTORS

    /**
     * Create the quaternion w + ix + yj + zk; create a quaternion with the given coefficients.
     * @param   w   The 1-coefficient; the real/scalar part.
     * @param   x   The i-coefficient; the first imaginary part.
     * @param   y   The j-coefficient; the second imaginary part.
     * @param   z   The k-coefficient; the third imaginary part.
     */
    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Create a quaternion with the given scalar and vector parts.
     * @param   scalar  The scalar part.
     * @param   vector  The vector part.
     */
    public Quaternion(double scalar, Vector vector) {
        w = scalar;
        x = vector.getX();
        y = vector.getY();
        z = vector.getZ();
    }

/// STATIC METHODS

    /**
     * Get the sum of an array of quaternions.
     * @param   q   An array of quaternions to sum.
     * @return  Returns the sum of the quaternions in `q`.
     */
    public Quaternion sum(Quaternion[] q) {
        return _sum(q, q.length);
    }

    /**
     * Helper method for `sum(Quaternion[])`.
     */
    private Quaternion _sum(Quaternion[] q, int n) {
        return n == 0   ? Quaternion.ZERO   : q[n-1].plus(_sum(q, n-1));
    }

/// INSTANCE METHODS

// Overrides

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(w);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        Quaternion q = (Quaternion) obj;
        return w == q.getW()
            && x == q.getX()
            && y == q.getY()
            && z == q.getZ();
    }

// Getters

    /**
     * Get the 1-coefficient/real part of the quaternion.
     */
    public double getW() {
        return w;
    }

    /**
     * Get the i-coefficient/first imaginary part of the quaternion.
     */
    public double getX() {
        return x;
    }

    /**
     * Get the j-coefficient/second imaginary part of the quaternion.
     */
    public double getY() {
        return y;
    }

    /**
     * Get the k-coefficient/third imaginary part of the quaternion.
     */
    public double getZ() {
        return  z;
    }

    /**
     * Get the scalar part of the quaternion. Same as `getW()`.
     */
    public double getScalar() {
        return w;
    }

    /**
     * Get the vector part of the quaternion.
     */
    public Vector getVector() {
        return new Vector(x, y, z);
    }

// Properties / Nullary Methods

    /**
     * Get the length/norm/magnitude of the quaternion.
     */
    public double length() {
        return Math.sqrt(w * w + x * x + y * y + z * z);
    }

    /**
     * Get the conjugate of the quaternion.
     * @return Returns this quaternion, but with negated vector part.
     */
    public Quaternion conjugate() {
        return new Quaternion(this.getScalar(), this.getVector().negate());
    }

    /**
     * Get the unit quaternion in the same direction as this one.
     * @return  Returns the quaternion with length 1 whose direction
     *          is the same as this one. If this quaternion is the zero
     *          quaternion, return the zero quaternion.
     */
    public Quaternion normalise() {
        return this.equals(Quaternion.ZERO) ? this  : this.times(1 / length());
    }

    /**
     * Get the additive inverse of the quaternion; get its negation.
     * @return Returns the quaternion scaled by -1.
     */
    public Quaternion negate() {
        return this.times(-1);
    }

// Algebraic Methods

    /**
     * Get a scalar multiple of the quaternion.
     * @param   scalar  The proportion to scale the quaternion by.
     * @return  Returns the corresponding scalar multiple of the quaternion.
     */
    public Quaternion scale(double scalar) {
        return new Quaternion(scalar * w, scalar * x, scalar * y, scalar * z);
    }

    /**
     * Multiply the quaternion by a scalar. Same as `scale(double)`.
     */
    public Quaternion times(double scalar) {
        return this.scale(scalar);
    }

    /**
     * Add another quaternion to this one; get their sum.
     * 
     * @param   q   The quaternion to add.
     * @return  Returns the sum of the two quaternions.
     */
    public Quaternion plus(Quaternion q) {
        return new Quaternion(
            w + q.getW(),
            x + q.getX(),
            y + q.getY(),
            z + q.getZ()
        );
    }

    /**
     * Subtract another quaternion from this one; get their difference.
     * 
     * @param   q   The quaternion to subtract.
     * @return  Returns the difference of the two quaternions.
     */
    public Quaternion minus(Quaternion q) {
        return this.plus(q.negate());
    }

    /**
     * Multiply this quaternion by another; get their product.
     * 
     * @param   q   The quaternion to multiply this one by.
     * @return  Returns the Hamilton product of the two quaternions.
     */
    public Quaternion times(Quaternion q) {
        return new Quaternion(
            w * q.getW() - x * q.getX() - y * q.getY() - z * q.getZ(),
            w * q.getX() + x * q.getW() + y * q.getZ() - z * q.getY(),
            w * q.getY() - x * q.getZ() + y * q.getW() + z * q.getX(),
            w * q.getZ() + x * q.getY() - y * q.getX() + z * q.getW()
        );
    }

    /**
     * Conjugate this quaternion by another. This is useful for implementing
     * rotations described by quaternions.
     * 
     * @param   q   The quaternion by which to conjugate this one.
     * @return  Given this quaternion is `p`, and `x*` denotes the conjugate of
     *          the quaternion `x` (i.e. the result of `x.conjugate()`), this
     *          method returns `q p q*`.
     */
    public Quaternion conjugatedBy(Quaternion q) {
        return q.times(this.times(q.conjugate()));
    }
}
