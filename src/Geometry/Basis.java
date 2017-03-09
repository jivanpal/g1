package Geometry;

import java.io.Serializable;

/**
 * A class of objects that can describe an orthogonal basis (coordinate system)
 * that can be reached from a reference basis simply by way of translating and
 * rotating the axes, and thus without any scaling, shearing or skewing.
 * 
 * These objects can be used as homogeneous matrices for the purposes of
 * implementing graphics, as the only bases used when implementing graphics
 * using homogeneous matrices are those achieved via translation and rotation
 * alone.
 * 
 * In the scope of this document, XYZ refers to the global basis, O is the
 * origin of XYZ, and I, J, and K refer to the unit vectors in the positive
 * directions of X, Y, and Z, respectively. X'Y'Z' is the local basis (that is,
 * the basis described by an instance of this class), O' is the origin of X'Y'Z',
 * and I', J', and K' are the corresponding unit vectors. R' is the rotation that
 * moves the XYZ axes to align with those of X'Y'Z', and as such, is also the
 * orientation of X'Y'Z'.
 * 
 * @author jivan
 */
public class Basis implements Serializable {
/// CONSTANTS
    /**
     * The basis XYZ; the global basis.
     */
    public static final Basis GLOBAL = new Basis (Vector.ZERO, Rotation.NONE);
    
/// FIELDS
    private Vector o;
    private Rotation r;
    
/// CONSTRUCTORS
    /**
     * Create a basis X'Y'Z' with a given origin O' and orientation R'.
     * @param   origin      O', the origin of X'Y'Z' expressed in XYZ.
     * @param   orientation R', the rotation that moves XYZ to align with those of X'Y'Z'.
     */
    public Basis(Vector origin, Rotation orientation) {
        this.o = origin;
        this.r = orientation;
    }
    
/// INSTANCE METHODS
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((r == null) ? 0 : r.hashCode());
        result = prime * result + ((o == null) ? 0 : o.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Basis b = (Basis) obj;
        return o.equals(b.getO())
            && r.equals(b.getR());
    }
    
// Getters
    
    /**
     * Get O' in XYZ.
     * @return O'—the position vector of the origin of X'Y'Z' with respect to
     *      XYZ—expressed in XYZ.
     */
    public Vector getO() {
        return o;
    }
    
    /**
     * Get R' in XYZ.
     * @return R'—the orientation of X'Y'Z'; the rotation that moves the XYZ axes
     *      to align with those of X'Y'Z'—expressed in XYZ.
     */
    public Rotation getR() {
        return r;
    }
    
    /**
     * Get I' in XYZ.
     * @return I'—the unit vector in the direction of positive X'—expressed in XYZ.
     */
    public Vector getI() {
        return r.apply(Vector.I);
    }
    
    /**
     * Get J' in XYZ.
     * @return J'—the unit vector in the direction of positive X'—expressed in XYZ.
     */
    public Vector getJ() {
        return r.apply(Vector.J);
    }
    
    /**
     * Get K' in XYZ.
     * @return K'—the unit vector in the direction of positive X'—expressed in XYZ.
     */
    public Vector getK() {
        return r.apply(Vector.K);
    }
    
// Setters
    
    /**
     * Change O'; change the origin of X'Y'Z'.
     * @param newOrigin the position vector of the new origin O' expressed in XYZ. 
     */
    public void setO(Vector newOrigin) {
        o = newOrigin;
    }
    
    /**
     * Change R'; change the origin of X'Y'Z'.
     * @param newOrigin the position vector of the new origin O' expressed in XYZ. 
     */
    public void setR(Rotation newOrientation) {
        r = newOrientation;
    }
    
// Transformations
    
    /**
     * Given the position vector expressed in X'Y'Z' of a point P, get the position
     * vector of P expressed in XYZ.
     * @param p the position vector of P expressed in X'Y'Z'.
     * @return the position vector of P expressed in XYZ.
     */
    public Vector globalisePoint(Vector p) {
        return r.apply(p).plus(o);
    }
    
// Change of basis for position vectors
    
    /**
     * Given the position vector expressed in XYZ of a point P, get the position
     * vector of P expressed in X'Y'Z'.
     * @param p the position vector of P expressed in XYZ.
     * @return the position vector of P expressed in X'Y'Z'.
     */
    public Vector localisePoint(Vector p) {
        return r.inverse().apply(p.minus(o));
    }
    
    /**
     * Given an array of position vectors of points expressed in X'Y'Z', mutate
     * the array so that the position vectors are now expressed in XYZ.
     * @param points the array of position vectors to mutate.
     */
    public void globalise(Vector[] points) {
        for (int i = 0; i < points.length; i++) {
            points[i] = globalisePoint(points[i]);
        }
    }
    
    /**
     * Given an array of position vectors of points expressed in XYZ, mutate
     * the array so that the position vectors are now expressed in X'Y'Z'.
     * @param points the array of position vectors to mutate.
     */
    public void localise(Vector[] points) {
        for (int i = 0; i < points.length; i++) {
            points[i] = localisePoint(points[i]);
        }
    }
    
// Change of basis for direction vectors
    
    /**
     * Given a vector describing a direction D with respect to the orientation of X'Y'Z',
     * get the vector describing D with respect to the orientation of XYZ. 
     * @param d the direction of D with respect to X'Y'Z'. 
     * @return the direction of D with respect to XYZ.
     */
    public Vector globaliseDirection(Vector d) {
        return r.apply(d);
    }
    
    /**
     * Given a vector describing a direction D with respect to the orientation of XYZ,
     * get the vector describing D with respect to the orientation of X'Y'Z'. 
     * @param d the direction of D with respect to XYZ. 
     * @return the direction of D with respect to X'Y'Z'.
     */
    public Vector localiseDirection(Vector d) {
        return r.inverse().apply(d);
    }
    
// Matrix representation
    
    /**
     * Get an array representation of the 4x4 homogeneous matrix that
     * maps vectors expressed in XYZ to the same vectors expressed in X'Y'Z'.
     */
    public double[][] getMatrixFromGlobalToLocal() {
        return new double[][] {
            { getI().getX(), getI().getY(), getI().getZ(), 0 },
            { getJ().getX(), getJ().getY(), getJ().getZ(), 0 },
            { getK().getX(), getK().getY(), getK().getZ(), 0 },
            { getO().getX(), getO().getY(), getO().getZ(), 1 }
        };
    }
    
// Composition
    
    /**
     * Compose this basis with another. Given another basis X''Y''Z'', get the unique
     * basis X'''Y'''Z''' such that X'''Y'''Z''' is to X''Y''Z'' as X'Y'Z' is to XYZ.
     * @param b the basis X''Y''Z''.
     * @return the basis X'''Y'''Z'''.
     */
    public Basis then(Basis b) {
        return new Basis(r.apply(b.getO()).plus(o), b.getR().then(r));
    }
}
