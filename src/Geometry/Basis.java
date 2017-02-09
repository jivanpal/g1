package Geometry;

/**
 * A class of objects that can describe an orthogonal basis (coordinate system)
 * that can be reached from a reference basis simply by way of translating and
 * rotating the axes, and thus without any scaling, shearing or skewing.
 * 
 * These objects can be used as homogeneous matrices for the purposes of
 * implementing graphics, as the only degrees of freedom for the bases used when
 * implementing graphics using matrices are those achieved via translation and
 * rotation alone.
 * 
 * @author jivan
 *
 */
public class Basis {
/// CONSTANTS
    /**
     * The identity basis; the basis reached by doing nothing
     * from the global basis.
     */
    public static final Basis I = new Basis (Vector.ZERO, Rotation.NONE);
    
/// FIELDS
    private final Vector origin;
    private final Rotation orientation;

/// CONSTRUCTORS
    /**
     * Create an object describing a basis with a given origin and orientation.
     * @param   origin      The origin of the basis with respect to the initial basis.
     * @param   orientation A `Rotation` that describes the orientation of the basis with
     *              respect to the initial basis.
     */
    public Basis(Vector origin, Rotation orientation) {
        this.origin = origin;
        this.orientation = orientation;
    }

/// INSTANCE METHODS

// Getters

    /**
     * Get the position vector of the origin of this basis with respect to the
     * global basis.
     */
    public Vector getOrigin() {
        return origin;
    }

    /**
     * Get the `Rotation` object the describes the orientation of this basis
     * with respect to the global basis.
     */
    public Rotation getOrientation() {
        return orientation;
    }

    /**
     * Get the direction vector of the x-axis of this basis with respect to the
     * global basis.
     */
    public Vector getXAxis() {
        return orientation.rotate(Vector.I);
    }

    /**
     * Get the direction vector of the y-axis of this basis with respect to the
     * global basis.
     */
    public Vector getYAxis() {
        return orientation.rotate(Vector.J);
    }

    /**
     * Get the direction vector of the z-axis of this basis with respect to the
     * global basis.
     */
    public Vector getZAxis() {
        return orientation.rotate(Vector.K);
    }

    /**
     * Get a 4x4 array representation of the 4x4 homogeneous matrix that
     * describes the coordinate transformation to reach this basis from the
     * global basis.
     */
    public double[][] getMatrix() {
        return new double[][] { { getXAxis().getX(), getXAxis().getY(), getXAxis().getZ(), 0 },
                { getYAxis().getX(), getYAxis().getY(), getYAxis().getZ(), 0 },
                { getZAxis().getX(), getZAxis().getY(), getZAxis().getZ(), 0 },
                { getOrigin().getX(), getOrigin().getY(), getOrigin().getZ(), 1 } };
    }

// Composition

    /**
     * Get the `Basis` object that describes the basis `b` with respect to the
     * global basis, where `b` is that basis described with respect to
     * <i>this</i> basis.
     */
    public Basis then(Basis b) {
        return new Basis(origin.plus(orientation.rotate(b.getOrigin())), orientation.then(b.getOrientation()));
    }
}
