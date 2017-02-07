package Geometry;

public class Rotation {
/// CONSTANTS
    public static final Rotation NONE = new Rotation(Vector.ZERO, 0);
    
/// FIELDS
    private Quaternion rotation;
    
/// CONSTRUCTORS
    
    /**
     * Given a quaternion that describes a rotation by extension of Euler's
     * equation, create an object to describe that rotation.
     */
    public Rotation (Quaternion rotation) {
        this.rotation = rotation;
    }
    
    /**
     * Given a vector whose direction describes an axis, and given an angle,
     * create an object describing a rotation about that axis by that angle.
     * 
     * @param   axis    A vector whose direction is the axis of rotation.
     * @param   angle   The angle by which to rotate about the axis, in radians.
     */
    public Rotation (Vector axis, double angle) {
        rotation = new Quaternion (
            Math.cos(angle/2),
            axis.normalise().scale( Math.sin(angle/2) )
        );
    }
    
    /**
     * Given a vector whose direction describes an axis, and whose length
     * describes an angle, create an object describing a rotation about that
     * axis by that angle.
     * 
     * @param   axisAngle  A vector whose direction is the axis of rotation,
     *              and whose length is the angle by which to rotate about
     *              that axis, in radians.
     */
    public Rotation (Vector axisAngle) {
        this (axisAngle, axisAngle.length());
    }
    
    /**
     * Given the "direction cosines" of an axis of rotation, and an angle by
     * which to rotate about that axis, create an object describing that rotation.
     *
     * @param   xCos   The cosine of the angle between the axis of rotation and the x-axis.
     * @param   yCos   The cosine of the angle between the axis of rotation and the y-axis.
     * @param   zCos   The cosine of the angle between the axis of rotation and the z-axis.
     * @param   angle   The angle by which to rotate about the axis counter-clockwise when
     *              viewed top-down, in radians.
     */
    public Rotation (double xCos, double yCos, double zCos, double angle) {
        this (new Vector(xCos, yCos, zCos), angle);
    }
    
    /**
     * Given a triple of Euler angles, create an object describing that rotation.
     * 
     * All angles are to be given in radians, as if rotating counter-clokwise about
     * the given axis when viewed top-down. The angles describe a set of rotations
     * to move from an initial co-ordinate system, xyz, to a final co-ordinate
     * system, XYZ, via intermediate co-ordinate systems x'y'z and x''y''z''.
     * 
     * @param   alpha   The angle to rotate about the z-axis by in xyz to get to x'y'z'.
     * @param   beta    The angle to rotate about the x'-axis by in x'y'z' to get to x''y''z''.
     * @param   gamma   The angle to rotate about the z''-axis by in x''y''z'' to get to XYZ.
     */
    public Rotation (double alpha, double beta, double gamma) {
        rotation =
            new Rotation (Vector.K, gamma).then(
                new Rotation(Vector.I, beta).then(
                    new Rotation(Vector.K, alpha)
                )
            ).getQuaternion();
    }
    
/// INSTANCE METHODS

    /**
     * Check whether a rotations is equal to another rotations.
     * @param   r   The rotation to compare with.
     * @return  Returns true when the two rotations have the same result.
     */
    public boolean equals(Rotation r) {
        return rotation.equals(r.getQuaternion());
    }

// Getters
    
    /**
     * Get the quaternion that dscribes this rotation.
     */
    public Quaternion getQuaternion() {
        return rotation;
    }
    
    /**
     * Get the vector whose direction is the axis of rotation and whose length
     * is the angle of rotation, in radians, counter-clokwise about the axis
     * when viewed-top-down.
     */
    public Vector getAxisAngle() {
        return rotation.getVector().normalise().scale( 2 * Math.acos(rotation.getScalar()) );
    }
    
// Enaction
    
    /**
     * Get the position vector of a point after being rotated by this `Rotation`.
     * @param   point   The position vector of the point to be rotated.
     * @return  Returns the position vector of the point after rotation.
     */
    public Vector rotate(Vector point) {
        return new Quaternion(0, point).conjugatedBy(rotation).getVector();
    }
    
    /**
     * Get the position vectors of an array of points after being rotated by this `Rotation` by mutating the array.
     * @param   points  An array of the position vectors of the points to be rotated.
     */
    public void rotate(Vector[] points) {
        _rotate(points, points.length);
    }
    
    /**
     * Helper method for `rotate(Vector[])`.
     */
    private void _rotate(Vector[] point, int n) {
        if (n != 0) {
            point[n-1] = rotate(point[n-1]);
            _rotate(point, n-1);
        }
    }
    
// Composition of Rotations
    
    /**
     * Get the rotation that is equivalent to doing `this`, then `that.
     * @param   that    The rotation to perform after this one.
     * @return  Returns the composition of this rotation with the argument
     *          rotation `that`, i.e. the rotation that does `this`, and
     *          then does `that.
     */
    public Rotation then(Rotation that) {
        return new Rotation (that.getQuaternion().times(rotation));
    }
    
    /**
     * 
     */
    
    
}