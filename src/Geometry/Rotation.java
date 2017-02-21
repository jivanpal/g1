package Geometry;

import java.io.Serializable;

/**
 * A class of objects that describe rotations with respect to the origin of an
 * initial coordinate system. These are described internally using quaternions.
 * @author jivan
 */
public class Rotation implements Serializable {
/// CONSTANTS
    /**
     * The rotation object that describes no rotation; the rotation that does
     * nothing; the identity rotation.
     */
    public static final Rotation NONE = new Rotation(Vector.ZERO, 0);

/// FIELDS
    private Quaternion rotation;

/// CONSTRUCTORS

    /**
     * Given a quaternion that describes a rotation by extension of Euler's
     * equation, create an object to describe that rotation.
     * 
     * @param   rotation    The quaternion representing this rotation.
     */
    public Rotation(Quaternion rotation) {
        this.rotation = rotation;
    }

    /**
     * Given a vector whose direction describes an axis, and given an angle,
     * create an object describing a rotation about that axis by that angle.
     * 
     * @param   axis    A vector whose direction is the axis of rotation.
     * @param   angle   The angle by which to rotate about the axis, in radians.
     */
    public Rotation(Vector axis, double angle) {
        rotation = new Quaternion(
            Math.cos(angle / 2),
            axis.normalise().scale(Math.sin(angle / 2))
        );
    }

    /**
     * Given a vector whose direction describes an axis, and whose length
     * describes an angle, create an object describing a rotation about that
     * axis by that angle.
     * 
     * @param   axisAngle   A vector whose direction is the axis of rotation, and whose
     *              length is the angle by which to rotate about that axis, in radians.
     */
    public Rotation(Vector axisAngle) {
        this(axisAngle, axisAngle.length());
    }

    /**
     * Given the "direction cosines" of an axis of rotation, and an angle by
     * which to rotate about that axis, create an object describing that
     * rotation.
     *
     * @param   xCos    The cosine of the angle between the axis of rotation and the x-axis.
     * @param   yCos    The cosine of the angle between the axis of rotation and the y-axis.
     * @param   zCos    The cosine of the angle between the axis of rotation and the z-axis.
     * @param   angle   The angle by which to rotate about the axis counter-clockwise when
     *              viewed top-down, measured in radians.
     */
    public Rotation(double xCos, double yCos, double zCos, double angle) {
        this(new Vector(xCos, yCos, zCos), angle);
    }

    /**
     * Given a triple of Euler angles, create an object describing that
     * rotation.
     * 
     * All angles are to be given in radians, as if rotating counter-clokwise
     * about the given axis when viewed top-down. The angles describe a set of
     * rotations to move from an initial coordinate system, xyz, to a final
     * coordinate system, XYZ, via intermediate coordinate systems x'y'z and
     * x''y''z''.
     * 
     * @param   alpha   The angle to rotate about the z-axis by in xyz to get to x'y'z'.
     * @param   beta    The angle to rotate about the x'-axis by in x'y'z' to get to x''y''z''.
     * @param   gamma   The angle to rotate about the z''-axis by in x''y''z'' to get to XYZ.
     */
    public Rotation(double alpha, double beta, double gamma) {
        rotation = new Rotation(Vector.K, gamma).then(new Rotation(Vector.I, beta).then(new Rotation(Vector.K, alpha)))
                .getQuaternion();
    }

/// INSTANCE METHODS

// Overrides

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rotation == null) ? 0 : rotation.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Rotation r = (Rotation) obj;
        return rotation.equals(r.getQuaternion());
    }

// Getters

    /**
     * Get the quaternion that describes this rotation.
     * 
     * @return  Returns the quaternion that describes this rotation, as per the
     *          standard extension of Euler's equation.
     */
    public Quaternion getQuaternion() {
        return rotation;
    }

    /**
     * Get the axis-angle representation of this rotation.
     *
     * @return Returns the vector whose direction is the axis of rotation and
     *         whose length is the angle of rotation, in radians,
     *         counter-clockwise about the axis when viewed-top-down.
     */
    public Vector getAxisAngle() {
        return rotation.getVector().normalise().scale(2 * Math.acos(rotation.getScalar()));
    }

    /**
     * Get the unit vector in the x-direction with respect to the coordinate
     * system described by this rotation.
     */
    public Vector getXAxis() {
        return this.apply(Vector.I);
    }

    /**
     * Get the unit vector in the y-direction with respect to the coordinate
     * system described by this rotation.
     */
    public Vector getYAxis() {
        return this.apply(Vector.J);
    }

    /**
     * Get the unit vector in the z-direction with respect to the coordinate
     * system described by this rotation.
     */
    public Vector getZAxis() {
        return this.apply(Vector.K);
    }
    
    /**
     * Get the inverse of this rotation. That is, the rotation that when performed
     * after this one is equivalent to performing no rotation. Formally, the rotation
     * <i>r</i> such that this.then(<i>r</i>) is equal to Rotation.NONE.
     * @return
     */
    public Rotation inverse() {
        return new Rotation(
            new Quaternion(
                rotation.getScalar(),
                rotation.getVector().negate()
            )
        );
    }

// Application

    /**
     * Get the position vector of a point after this rotation is applied to it.
     * @param   point   The position vector of the point to be rotated.
     * @return  Returns the position vector of the point after rotation.
     */
    public Vector apply(Vector point) {
        return new Quaternion(0, point).conjugatedBy(rotation).getVector();
    }

    /**
     * Mutate an array of position vectors by applying this rotation to them.
     * @param   points  An array of the position vectors of the points to be rotated.
     */
    public void apply(Vector[] points) {
        _apply(points, points.length);
    }

    /**
     * Helper method for `apply(Vector[])`.
     */
    private void _apply(Vector[] point, int n) {
        if (n != 0) {
            point[n - 1] = apply(point[n - 1]);
            _apply(point, n - 1);
        }
    }

// Composition

    /**
     * Get the rotation that is equivalent to doing `this`, then `that.
     * 
     * @param   that  The rotation to perform after this one.
     * @return  Returns the composition of this rotation with the argument
     *          rotation `that`, i.e. the rotation that does `this`, and then
     *          does `that.
     */
    public Rotation then(Rotation that) {
        return new Rotation(that.getQuaternion().times(rotation));
    }
}
